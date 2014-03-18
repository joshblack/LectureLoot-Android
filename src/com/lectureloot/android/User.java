package com.lectureloot.android;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.lectureloot.background.HttpGet;
import com.lectureloot.background.HttpGetCourses;
import com.lectureloot.background.HttpGetWagers;
import com.lectureloot.background.UserListner;

import android.text.format.Time;

@SuppressWarnings("unused")
public class User {
	private static User mInstance = null;
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
	
	/* CONSTRUCTOR FOR USER */
	private User(){
		mFirstName = "";
		mLastName = "";
		mEmail = "";
		mAuthToken = "";
		mPassword = "";
		mPoints = 0;
		mWageredPoints = 0;
		mCourses = new ArrayList<Course>();
		mMeetings = new ArrayList<Meeting>();
		mWagers = new ArrayList<Wager>();
	}
	
	/* create new user and load data, or return existing user */
	public static User getInstance(){
		if(mInstance == null){
            mInstance = new User();
            mInstance.load();
        }
        return mInstance;
	}
	
	/* CONSTRUCTOR FOR User WITH SUPPLIED NAME */
	
	private User(String name){
		mFirstName = name;
		mAuthToken = "";
		mPoints = 0;
		mWageredPoints = 0;
		//mWagers = null;
		mCourses = null;
		mMeetings = null;
	}
	
	public static User getInstance(String name){
		//this is for development purposes
		if(mInstance == null){
            mInstance = new User(name);
        }
        return mInstance;
	}
	
	/* load() will either get data from file, or from database if no file exists */
	public void load(){
		//try to load data from file
		if(!loadFromFile()){
			if(!login())	//try login to server (generate auth token)
				register();	//register user instead if login fails
			
			loadUserData();	//either way, get the data from the server afterwards
		}
	}

	public boolean loadFromFile(){
		//try to open file. If it exists, load the data
		try{
			FileInputStream fis = MainActivity.mContext.openFileInput("user.dat");
			BufferedReader in = new BufferedReader(new InputStreamReader(fis));	

			//grab the top level data
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

			return true; //load successful

			//If the file doesn't exist, get data from server then make it
		} catch (FileNotFoundException e){
			//file doesn not exist, do nothing
		} catch (NumberFormatException e) {
			//TOAST
		} catch (IOException e) {
			//TOAST
		}
		return false; //load failed
	}

	public synchronized boolean writeToFile(){
		/* method will write the user class to the file, only one write allowed at a time */
		try{
			FileOutputStream out = MainActivity.mContext.openFileOutput("user.dat", 0);	
			
			//write user data
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
			url = new URL("http://lectureloot.com/api/v1/users/login?" + urlParamaters);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			   try {
				 BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				 JSONTokener tokener = new JSONTokener(in.readLine());
			     JSONObject input = (JSONObject) tokener.nextValue();
			     if(input.get("messgage").equals("Success, valid credentials")){
			    	 mAuthToken = input.getString("token");	//logged in successfully
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
		}
		
		return false;	//logged in successfully
	}
	
	public boolean doRegister(){
		/*try to register the user and generate an auth token (will block)*/
		
		//TODO: display some sort of 'logging in' popup here (currently just blocking)
		
		//get the info from the server
		URL url;
		try {
			String urlParamaters = "emailAddress=" + mEmail + "&password=" + mPassword + "&firstName=" + mFirstName + "&lastName=" + mLastName;
			url = new URL("http://lectureloot.com/api/v1/users?" + urlParamaters);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			   try {
				 BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				 JSONTokener tokener = new JSONTokener(in.readLine());
			     JSONObject input = (JSONObject) tokener.nextValue();
			     if(input.get("messgage").equals("Success, valid credentials")){
			    	 mAuthToken = input.getString("token");	//registered successfully
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
		}
		
		return false;	//logged in successfully
	}
	
	public boolean login(){
		/*prompt for Email/password from the UI element and try to login */		
		
		//TODO: Throw to UI element to get Username/Password
		
		return doLogin();	//call login method
	}
	
	public boolean register(){
		/*prompt for Email/Name/password from the UI element and try to register */		
		
		//TODO: Throw to UI element to get infos
		
		return doRegister();	//call login method
		
		
	}
	
	public void loadUserData(){
		UserListner listner = new UserListner();  //setup the listner for the return
		
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
	}
	
	public void checkIn(){
		//get the user's location with gps
		//double check that with the location of the meeting
		
		//if it's close enough, check the user into the a meeting
	}
	
	public Meeting getUpcomingMeeting(){
		//go through the meetings arraylist and get the next meeting
		
		//based on the current time and checked in courses
		return null;
	}
	
	public void addCourse(Course newCourse){
		
	}
	
	public void addMeeting(Meeting newMeeting){
		
	}
	
	public void addToPoints(int newPoints){
		mPoints += newPoints;
	}
	
	//TODO: Examine security/completness of Get/Set methods
	
	/* GETTERS */

	public String getAuthToken(){
		return mAuthToken;
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

	/* SETTERS */

	public void setFirstName(String name) {
		mFirstName = name;
	}
	
	public void setLastName(String name) {
		mFirstName = name;
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

}
