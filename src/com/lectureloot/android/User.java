package com.lectureloot.android;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.security.auth.login.LoginException;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lectureloot.background.*;

public class User {
	public static final String URL_BASE = "http://lectureloot.eu1.frbit.net/api/v1";	
	private static User mInstance = null;
	private boolean busyFlag;
	private String mUserId;
	private String mFirstName;
	private String mLastName;
	private String mEmail;
	private String mPassword;
	private String mAuthToken;
	private int mPoints;
	private int mWageredPoints;
	private ArrayList<Wager> mWagers;
	private ArrayList<Course> mCourses;
	private ArrayList<Meeting> mMeetings;
	private ArrayList<Sessions> mSessions;
	private ArrayList<Course> mCourseList;

	/* CONSTRUCTOR FOR USER */
	private User(){
		mFirstName = " ";
		mLastName = " ";
		mUserId = " ";
		mEmail = " ";
		mAuthToken = " ";
		mPassword = " ";
		mPoints = 0;
		mWageredPoints = 0;
		mCourses = new ArrayList<Course>();
		mMeetings = new ArrayList<Meeting>();
		mWagers = new ArrayList<Wager>();
		mSessions = new ArrayList<Sessions>();
		mCourseList = new ArrayList<Course>();
		busyFlag = false;
	}

	public static User getInstance(){
		/* create new user and load data, or return existing user */	
		if(mInstance == null){
			mInstance = new User();
			mInstance.load();
		}
		return mInstance;
	}

	/* Methods By Josh */
	public void load(){
		/* load() will either get data from file, or from database if no file exists */
		
		busyFlag = true;	//user is being loaded
		
		//load data in seperate thread
		Thread loadThread = new Thread(new Runnable(){
			public void run(){
				Looper.prepare();
				if(!loadFromFile()){	//try to get from file
					if(!login())		//try login to server (generate auth token)
						register();		//register user

					loadUserData();		//either way, get the data from the server afterwards
				}
				busyFlag = false;
			}
		});
		
		loadThread.start();
	}

	public boolean loadFromFile(){
		//try to open file. If it exists, load the data
		try{
			MainActivity.mContext.deleteFile("user.dat");
			FileInputStream fis = MainActivity.mContext.openFileInput("user.dat");
			BufferedReader in = new BufferedReader(new InputStreamReader(fis));	

			//grab the top level data
			this.mUserId = in.readLine().split(":")[1];
			this.mFirstName = in.readLine().split(":")[1];
			this.mLastName = in.readLine().split(":")[1];
			this.mEmail = in.readLine().split(":")[1];
			this.mPassword = in.readLine().split(":")[1];
			this.mPoints = Integer.parseInt(in.readLine().split(":")[1]);
			this.mWageredPoints = Integer.parseInt(in.readLine().split(":")[1]);

			//grab the courses
			int numCourses = Integer.parseInt(in.readLine().split(":")[1]);
			ArrayList<Course> courses = new ArrayList<Course>();
			while(numCourses-- > 0){
				Course course = new Course();	//creat new course
				String[] inLine = in.readLine().split(":");	//get the input data

				//Fill course with data
				course.setCourseId(Integer.parseInt(inLine[1]));
				course.setCourseCode(inLine[2]);
				course.setCourseTitle(inLine[3]);
				course.setSectionNumber(inLine[4]);
				course.setCredits(inLine[5]);
				course.setInstructor(inLine[6]);

				//get the course's meetings
				ArrayList<Meeting> meetings = new ArrayList<Meeting>();
				int numMeetings = Integer.parseInt(inLine[6]);
				while(numMeetings-- >0){
					Meeting meeting = new Meeting();
					inLine = in.readLine().split(":");	//get intput data

					//fill in meeting data
					meeting.setMeetingId(Integer.parseInt(inLine[1]));
					meeting.setBuildingCode(inLine[2]);
					meeting.setRoomNumber(inLine[3]);
					meeting.setMeetingDay(inLine[4]);
					meeting.setPeriod(inLine[5]);
					meeting.setCourseId(course.getCourseId());

					meetings.add(meeting);	//add to arrayList
				}
				course.setMeetings(meetings);	//add the meetings to the course
				courses.add(course);	//add the course to the arrayList
			}
			this.mCourses = courses;	//add courses

			//get the Wagers
			ArrayList<Wager> wagers = new ArrayList<Wager>();
			int numWagers = Integer.parseInt(in.readLine().split(":")[1]);
			while(numWagers-- >0){
				Wager wager = new Wager();
				String[] inLine = in.readLine().split(":");	//get intput data

				//fill in meeting data
				wager.setWagerId(Integer.parseInt(inLine[1]));
				wager.setWagerSessionCode(Integer.parseInt(inLine[2]));
				wager.setWagerPerMeeting(Integer.parseInt(inLine[3]));
				wager.setTotalMeetings(Integer.parseInt(inLine[4]));
				wager.setTotalWager(Integer.parseInt(inLine[5]));

				wagers.add(wager);	//add to arrayList
			}
			this.mWagers = wagers;

			doLogin();

			in.close();
			fis.close();

			Log.i("Load File:", "Load Succeessful\n" + mFirstName + " " + mLastName + " " + mEmail);
			return true; //load successful

			//If the file doesn't exist, get data from server then make it
		} catch (FileNotFoundException e){
			//file doesn not exist, do nothing
		} catch (NumberFormatException e) {
			//TOAST
		} catch (IOException e) {
			//TOAST
		} catch (ArrayIndexOutOfBoundsException e){
			Log.i("LoadFile:",e.toString());
		}
		Log.i("Load File:", "Load Failed");
		return false; //load failed
	}

	public synchronized boolean writeToFile(){
		/* method will write the user class to the file, only one write allowed at a time */
		try{
			FileOutputStream out = MainActivity.mContext.openFileOutput("user.dat", 0);	

			//write user data
			out.write(("ID:" + mUserId + "\n").getBytes());
			out.write(("First:" + mFirstName + "\n").getBytes());
			out.write(("Last:" + mLastName + "\n").getBytes());
			out.write(("Email:" + mPassword + "\n").getBytes());
			out.write(("Password:" + mPassword + "\n").getBytes());
			out.write(("Points:" + mPoints + "\n").getBytes());
			out.write(("WageredPoints:" + mWageredPoints + "\n").getBytes());
			out.write(("NumCourses:" + mCourses.size() + "\n").getBytes());	

			//write course data
			for(int i = 0;i<mCourses.size();++i){
				out.write(("Course:" + mCourses.get(i).getCourseId()).getBytes());
				out.write((":" + mCourses.get(i).getCourseCode()).getBytes());
				out.write((":" + mCourses.get(i).getCourseTitle()).getBytes());
				out.write((":" + mCourses.get(i).getSectionNumber()).getBytes());
				out.write((":" + mCourses.get(i).getCredits()).getBytes());
				out.write((":" + mCourses.get(i).getInstructor()).getBytes());

				ArrayList<Meeting> meetings = new ArrayList<Meeting>();				
				out.write((":" + meetings.size()+"\n").getBytes());

				//write meeting data (ignoring time)
				for(int k=0;k<meetings.size();++k){
					out.write(("Meeting:" + meetings.get(k).getMeetingId()).getBytes());
					out.write((":" + meetings.get(k).getBuildingCode()).getBytes());
					out.write((":" + meetings.get(k).getRoomNumber()).getBytes());
					out.write((":" + meetings.get(k).getMeetingDay()).getBytes());
					out.write((":" + meetings.get(k).getPeriod()+"\n").getBytes());
				}
			}

			out.write(("NumWagers:" + mWagers.size() + "\n").getBytes());
			for(int i = mWagers.size();i >0;--i){				
				out.write(("Wager:" + mWagers.get(i).getWagerId()).getBytes());
				out.write((":" + mWagers.get(i).getWagerSessionCode()).getBytes());
				out.write((":" + mWagers.get(i).getWagerPerMeeting()).getBytes());
				out.write((":" + mWagers.get(i).getTotalMeetings()).getBytes());
				out.write((":" + mWagers.get(i).getTotalWager() + "\n").getBytes());
			}			

			//close the file
			out.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public boolean doLogin(){
		/*automaticly log the user in and generate authToken (will block)*/

		//TODO: display some sort of 'logging in' popup here (currently just blocking)

		//get the info from the server
		URL url;
		try {
			String urlParamaters = "emailAddress=" + mEmail + "&password=" + mPassword;
			url = new URL(URL_BASE + "/users/login?" + urlParamaters);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("POST");
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				JSONTokener tokener = new JSONTokener(in.readLine());
				JSONObject input = (JSONObject) tokener.nextValue();
				if(input.getString("message").equals("Success, valid credentials")){
					mAuthToken = input.getString("token");	//logged in successfully
					mUserId = input.getString("user_id");
					Log.i("Login:", "Login Succeeded");		//DEBUG
					return true;
				}
			} catch (JSONException e) {
				//Toast
			} catch (ClassCastException e){
				Log.w("Login:",e.toString());
			}finally {
				urlConnection.disconnect();
			}
		} catch (MalformedURLException e) {
			//Toast
		} catch (IOException e) {
			//Toast
		}
		Log.i("Login:", "Login Failed");		//DEBUG
		return false;		
	}

	public boolean doRegister(){
		/*try to register the user and generate an auth token (will block)*/

		//TODO: display some sort of 'logging in' popup here (currently just blocking)

		//get the info from the server
		URL url;
		try {
			String urlParamaters = "emailAddress=" + mEmail + "&password=" + mPassword + "&firstName=" + mFirstName + "&lastName=" + mLastName;
			url = new URL(URL_BASE + "/users?" + urlParamaters);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("POST");
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				JSONTokener tokener = new JSONTokener(in.readLine());
				JSONObject input = (JSONObject) tokener.nextValue();
				if(input.get("message").equals("Success, the user was registered")){
					mAuthToken = input.getString("token");	//registered successfully
					mUserId = input.getString("user_id");
					Log.i("Register:","Registration Successful");
					return true;
				}
			} catch (JSONException e) {
				//Toast
			} finally {
				urlConnection.disconnect();
			}
		} catch (MalformedURLException e) {
			//Toast
		} catch (IOException e) {
			//Toast
		} catch (ClassCastException e){
			Log.i("Register:",e.toString());
		}

		Log.i("Register:","Registration Failed");
		return false;	//logged in successfully
	}

	public boolean login(){
	/*prompt for Email/password from the UI element and try to login */
		
		//TODO: Throw to UI element for login
		
		/*Test Data
		 * mEmail = joshS@ufl.edu
		 * mPassword = password;
		 */
		
		return doLogin();
	}

	public boolean register(){
	/*prompt for Email/Name/password from the UI element and try to register */		

		//TODO: Throw to UI element to get infos

		return doRegister();	//call login method


	}

	public void loadUserData(){
		/* Method will load user data from the serer in seperate threads, but will block until done */
		UserListner listner = new UserListner(this);  //setup the listner for the return

		//load the courses from the server
		String courseUrl = "http://lectureloot.eu1.frbit.net/api/v1/users/" + mUserId + "/courses";
		HttpGetCourses courseTask = new HttpGetCourses(mAuthToken);
		courseTask.setHttpGetFinishedListener(listner);
		courseTask.execute(new String[] {courseUrl});

		//get the wagers frm the server
		String wagerUrl = "http://lectureloot.eu1.frbit.net/api/v1/users/" + mUserId + "/wagers";
		HttpGetWagers wagerTask = new HttpGetWagers(mAuthToken);
		wagerTask.setHttpGetFinishedListener(listner);
		wagerTask.execute(new String[] {wagerUrl});

		//get the sessions frm the server
		String sessionUrl = "http://lectureloot.eu1.frbit.net/api/v1/sessions";	//TODO: check this later
		HttpGetSessions sessionTask = new HttpGetSessions(mAuthToken);
		sessionTask.setHttpGetFinishedListener(listner);
		sessionTask.execute(new String[] {sessionUrl});

		//get the full course list from the server (does not block) 
		String courseListUrl = "http://lectureloot.eu1.frbit.net/api/v1/courses";
		HttpGetCourseList courseListTask = new HttpGetCourseList(mAuthToken);
		courseListTask.setHttpGetFinishedListener(listner);
		courseListTask.execute(new String[] {courseListUrl});

		//wait for threads to finish before continuing
		listner.waitForThreads(); 
	}

	public void addCourseFromList(Course course){
		/* method to resolve incomplete course from courseList and add to user (DOESN'T POST, DOESN'T BLOCK) */
		UserListner listner = new UserListner(this);  //setup the listner for the return

		//load the courses from the server
		String courseUrl = "http://lectureloot.eu1.frbit.net/api/v1/courses/" + course.getCourseId();
		HttpGetCourses courseTask = new HttpGetCourses(mAuthToken);
		courseTask.setHttpGetFinishedListener(listner);
		courseTask.execute(new String[] {courseUrl});
	}

	/* GETTERS */
	public boolean isBusy(){
		return busyFlag;
	}
	
	public String getUserId(){
		return mUserId;
	}

	public String getName() {
		return mFirstName + " " + mLastName;
	}

	public String getFirstName() {
		return mFirstName;
	}

	public String getLastName() {
		return mLastName;
	}

	public String getEmail(){
		return mEmail;
	}

	public String getAuthToken(){
		return mAuthToken;
	}

	public int getPoints() {
		return mPoints;
	}

	public int getWageredPoints() {
		return mWageredPoints;
	}

	public ArrayList<Wager> getWagers() {
		return mWagers;
	}

	public ArrayList<Course> getCourses() {
		return mCourses;
	}

	public ArrayList<Meeting> getMeetings() {
		return mMeetings;
	}

	public ArrayList<Sessions> getSessions(){
		return mSessions;
	}

	public ArrayList<Course> getCourseList(){
		return mCourseList;
	}

	/* SETTERS */

	public void setFirstName(String name) {
		mFirstName = name;
	}

	public void setLastName(String name) {
		mFirstName = name;
	}

	public void setEmail(String email){
		mEmail = email;
	}

	public void setPassword(String password){
		mPassword = password;
	}

	public void setPoints(int points) {
		mPoints = points;
	}

	public void setWageredPoints(int wageredPoints) {
		mWageredPoints = wageredPoints;
	}

	public void setWagers(ArrayList<Wager> wagers) {
		mWagers = wagers;
	}	

	public void setCourses(ArrayList<Course> courses) {
		mCourses = courses;
	}

	public void setMeetings(ArrayList<Meeting> meetings) {
		mMeetings = meetings;
	}

	public void setSessions(ArrayList<Sessions> sessions) {
		mSessions = sessions;
	}

	public void setCourseList(ArrayList<Course> courseList) {
		mCourseList = courseList;
	}
}
