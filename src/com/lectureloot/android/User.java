package com.lectureloot.android;

import java.sql.Date;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.lectureloot.background.HttpGetCourseList;
import com.lectureloot.background.HttpGetCourses;
import com.lectureloot.background.HttpGetMeetingList;
import com.lectureloot.background.HttpGetSessions;
import com.lectureloot.background.HttpGetUser;
import com.lectureloot.background.HttpGetWagers;
import com.lectureloot.background.UserListner;

import android.content.Context;
import android.util.Log;


public class User {
	public static final String URL_BASE = "http://lectureloot.eu1.frbit.net/api/v1";	
	private static User mInstance = null;
	private boolean loadedFlag;
	public  boolean loginFlag;
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
	private ArrayList<Meeting> mMeetingList;

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
		loadedFlag = false;
		loginFlag = false;
	}

	public static User getInstance(){
		/* create new user and load data, or return existing user */	
		if(mInstance == null){
			mInstance = new User();
		}
		return mInstance;
	}

	public boolean loadFromFile(){
	/* Method will attempt to load as much data from as many files as possible,
	 * and will return false if any load failed, but only after the loads are done */
		
		//try to load as much as possible anyway, don't short circut
		boolean loaded = loadCoursesFromFile();
		loaded = loadMeetingsFromFile() && loaded;
		loaded = loadSessionsFromFile() && loaded;
		try{
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
			String[] courseIDs = in.readLine().split(":");

			Log.i("Load File:", "User Loaded");

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

			Log.i("Load File:", "Wagers Loaded");

			//close user file and open the next one
			in.close();
			fis.close();
			
			//only continue if data is valid
			if(!loaded) return false;

			new Thread(new Runnable(){
				public void run(){
					//don't care if this works, validateData will catch it
					doLogin();	
				}
			}).start();

			Meeting.resolveMeetings(mMeetingList,mCourseList);

			for(int i=1;i<courseIDs.length;++i)
				mCourses.add(mCourseList.get(Integer.parseInt(courseIDs[i])-1));

			Log.i("Load File:", "Load Succeessful");

			loadedFlag = true;

			return true; //load successful

			//If the file doesn't exist, get data from server then make it
		} catch (FileNotFoundException e){
			Log.i("LoadFile:",e.toString());
		} catch (Exception e){
			Log.w("LoadFile:",e.toString());
		}
		Log.i("Load File:", "Load Failed");
		return false; //load failed
	}

	public boolean loadCoursesFromFile(){
		try{
			FileInputStream fis = MainActivity.mContext.openFileInput("courseList.dat");
			BufferedReader in = new BufferedReader(new InputStreamReader(fis));	

			//grab the courses
			ArrayList<Course> courses = new ArrayList<Course>();
			String[] inLine = in.readLine().split(":");	//get the input data
			while(!inLine[0].equals("END")){
				Course course = new Course();	//creat new course

				//Fill course with data
				course.setCourseId(Integer.parseInt(inLine[1]));
				course.setCoursePrefix(in.readLine().split(":")[1]);
				course.setCourseNum(in.readLine().split(":")[1]);
				course.setSectionNumber(in.readLine().split(":")[1]);
				course.setCredits(in.readLine().split(":")[1]);
				course.setInstructor(in.readLine().split(":")[1]);
				course.setCourseTitle(in.readLine().split(":")[1]);
				course.setSemester(in.readLine().split(":")[1]);
				course.setYear(in.readLine().split(":")[1]);

				courses.add(course);	//add the course to the arrayList
				inLine = in.readLine().split(":");
			}
			this.mCourseList = courses;	//add courses

			Log.i("Load File:", "Courses Loaded");

			//close user file and open the next one
			in.close();
			fis.close();
			return true;

		} catch (FileNotFoundException e){
			Log.i("LoadFile:",e.toString());
		} catch (Exception e){
			Log.w("LoadFile:",e.toString());
		}
		Log.i("Load File:", "Course Load Failed");
		return false; //load failed
	}

	public boolean loadMeetingsFromFile(){
		try{
			FileInputStream fis = MainActivity.mContext.openFileInput("meetingList.dat");
			BufferedReader in = new BufferedReader(new InputStreamReader(fis));	

			//parse the meetings
			ArrayList<Meeting> meetings = new ArrayList<Meeting>();
			String[] inLine = in.readLine().split(":");	//get the input data
			while(!inLine[0].equals("END")){
				Meeting meeting = new Meeting();	//creat new meeting

				//Fill meeting with data
				meeting.setMeetingId(Integer.parseInt(inLine[1]));
				meeting.setCourseId(Integer.parseInt(in.readLine().split(":")[1]));
				meeting.setBuildingCode(in.readLine().split(":")[1]);
				meeting.setLatitude(Double.parseDouble(in.readLine().split(":")[1]));
				meeting.setLongitude(Double.parseDouble(in.readLine().split(":")[1]));
				meeting.setRoomNumber(in.readLine().split(":")[1]);
				meeting.setMeetingDay(in.readLine().split(":")[1]);
				meeting.setPeriod(in.readLine().split(":")[1]);

				meetings.add(meeting);	//add the meeting to the arrayList
				inLine = in.readLine().split(":");
			}
			this.mMeetingList = meetings;	//add meetings

			//close user file and open the next one
			in.close();
			fis.close();
			return true;

		} catch (FileNotFoundException e){
			Log.i("LoadFile:",e.toString());
		} catch (Exception e){
			Log.w("LoadFile:",e.toString());
		}
		Log.i("Load File:", "Meeting Load Failed");
		return false; //load failed
	}

	public boolean loadSessionsFromFile(){
		try{
			FileInputStream fis = MainActivity.mContext.openFileInput("sessions.dat");
			BufferedReader in = new BufferedReader(new InputStreamReader(fis));	

			//parse the sessions
			ArrayList<Sessions> sessions = new ArrayList<Sessions>();
			String[] inLine = in.readLine().split(":");	//get the input data
			while(!inLine[0].equals("END")){
				Sessions session = new Sessions();	//creat new session

				//Fill session with data
				session.setSessionId(Integer.parseInt(inLine[1]));
				session.setStartDate(Date.valueOf(in.readLine().split(":")[1]));
				session.setEndDate(Date.valueOf(in.readLine().split(":")[1]));

				sessions.add(session);	//add the session to the arrayList
				inLine = in.readLine().split(":");
			}
			this.mSessions = sessions;	//add sessions

			in.close();
			fis.close();
			return true;
			
		} catch (FileNotFoundException e){
			Log.i("LoadFile:",e.toString());
		} catch (Exception e){
			Log.w("LoadFile:",e.toString());
		}
		Log.i("Load File:", "Session Load Failed");
		return false; //load failed
	}

	public synchronized boolean writeToFile(){
		/* method will write the user class to the file, only one write allowed at a time */
		try{			
			FileOutputStream out = MainActivity.mContext.openFileOutput("user.dat", Context.MODE_PRIVATE);

			//write user data
			out.write(("ID:" + mUserId + "\n").getBytes());
			out.write(("First:" + mFirstName + "\n").getBytes());
			out.write(("Last:" + mLastName + "\n").getBytes());
			out.write(("Email:" + mEmail + "\n").getBytes());
			out.write(("Password:" + mPassword + "\n").getBytes());
			out.write(("Points:" + mPoints + "\n").getBytes());
			out.write(("WageredPoints:" + mWageredPoints + "\n").getBytes());

			//write course data
			out.write("Courses".getBytes());
			for(int i = 0;i<mCourses.size();++i){
				out.write((":" + mCourses.get(i).getCourseId()).getBytes());
			}
			out.write("\n".getBytes());	//newline

			out.write(("NumWagers:" + mWagers.size() + "\n").getBytes());
			for(int i=0; mWagers.size() > i;++i){				
				out.write(("Wager:" + mWagers.get(i).getWagerId()).getBytes());
				out.write((":" + mWagers.get(i).getWagerSessionCode()).getBytes());
				out.write((":" + mWagers.get(i).getWagerPerMeeting()).getBytes());
				out.write((":" + mWagers.get(i).getTotalMeetings()).getBytes());
				out.write((":" + mWagers.get(i).getTotalWager() + "\n").getBytes());
			}			

			//close the file
			out.close();


			//write the course-list
			out = MainActivity.mContext.openFileOutput("courseList.dat", Context.MODE_PRIVATE);	

			//write course data
			for(int i = 0;i<mCourseList.size();++i){
				out.write(("Course:" + mCourseList.get(i).getCourseId() + "\n").getBytes());
				out.write(("deptCode:" + mCourseList.get(i).getCoursePrefix() + "\n").getBytes());
				out.write(("courseNum:" + mCourseList.get(i).getCourseNum() + "\n").getBytes());
				out.write(("sectionNum:" + mCourseList.get(i).getSectionNumber() + "\n").getBytes());
				out.write(("credits:" + mCourseList.get(i).getCredits() + "\n").getBytes());
				out.write(("instructor:" + mCourseList.get(i).getInstructor() + "\n").getBytes());
				out.write(("title:" + mCourseList.get(i).getCourseTitle() + "\n").getBytes());
				out.write(("semester:" + mCourseList.get(i).getSemester() + "\n").getBytes());
				out.write(("year:" + mCourseList.get(i).getYear() + "\n").getBytes());
			}			
			out.write("END".getBytes());

			//close the file
			out.close();


			//write the meeting list
			out = MainActivity.mContext.openFileOutput("meetingList.dat", Context.MODE_PRIVATE);	

			//write course data
			for(int k=0;k<mMeetingList.size();++k){
				out.write(("Meeting:" + mMeetingList.get(k).getMeetingId()+"\n").getBytes());
				out.write(("CourseID:" + mMeetingList.get(k).getCourseId()+"\n").getBytes());
				out.write(("buildingCode:" + mMeetingList.get(k).getBuildingCode()+"\n").getBytes());
				out.write(("Lat:" + mMeetingList.get(k).getLatitude()+"\n").getBytes());
				out.write(("Long:" + mMeetingList.get(k).getLongitude()+"\n").getBytes());
				out.write(("Room:" + mMeetingList.get(k).getRoomNumber()+"\n").getBytes());
				out.write(("Day:" + mMeetingList.get(k).getMeetingDay()+"\n").getBytes());
				out.write(("Period:" + mMeetingList.get(k).getPeriod()+"\n").getBytes());
			}
			out.write("END".getBytes());

			//close the file
			out.close();


			//write the sessions
			out = MainActivity.mContext.openFileOutput("sessions.dat", Context.MODE_PRIVATE);	

			//write course data
			for(int k=0;k<mSessions.size();++k){
				out.write(("Session:" + mSessions.get(k).getSessionId()+"\n").getBytes());
				out.write(("StartDate:" + mSessions.get(k).getStartDate()+"\n").getBytes());
				out.write(("EndDate:" + mSessions.get(k).getEndDate()+"\n").getBytes());
			}
			out.write("END".getBytes());

			//close the file
			out.close();

			Log.i("WriteFile:","All Data Written");


		} catch (Exception e) {
			Log.i("File:",e.toString());
			return false;
		}
		return true;
	}

	public boolean doLogin(final String email, final String password){
		/*automaticly log the user in and generate authToken (will block)*/	
		
		//get the info from the server
		URL url;
		try {
			String urlParamaters = "emailAddress=" + email + "&password=" + password;
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
					mPassword = password;
					mEmail = email;
					loginFlag = true;
					return true;
				}
			} catch (JSONException e) {
				Log.i("Login:", "Login Failed");		//DEBUG
			} catch (ClassCastException e){
				Log.w("Login:",e.toString());
			}finally {
				urlConnection.disconnect();
			}
		} catch (MalformedURLException e) {
			Log.i("Login:", "Login Failed");		//DEBUG
		} catch (IOException e) {
			Log.i("Login:", "Login Failed");		//DEBUG
		}
		return false;
	}

	public boolean doLogin(){
		/*automaticly log the user in and generate authToken (will block)*/
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
					loginFlag = true;
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
				if(input.getString("message").equals("Success, the user was registered")){
					mAuthToken = input.getString("token");	//registered successfully
					mUserId = input.getString("user_id");
					Log.i("Register:","Registration Successful");
					loginFlag = true;
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

	public boolean doRegister(final String email, final String password, final String first, final String last){
		/*try to register the user and generate an auth token (will block)*/
		
		//get the info from the server
		URL url;
		try {
			String urlParamaters = "emailAddress=" + email + "&password=" + password + "&firstName=" + first + "&lastName=" + last + "&pointBalance=0";
			url = new URL(URL_BASE + "/users?" + urlParamaters);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestMethod("POST");          
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				JSONTokener tokener = new JSONTokener(in.readLine());
				JSONObject input = (JSONObject) tokener.nextValue();
				if(input.getString("message").equals("Success, the user was registered")){
					mAuthToken = input.getString("token");	//registered successfully
					mUserId = input.getString("user_id");
					Log.i("Register:","Registration Successful");
					loginFlag = true;
					mEmail = email;
					mPassword = password;
					mFirstName = first;
					mLastName = last;
					return true;
				} else {
					Log.i("Register:",input.getString("message"));
				}
			} catch (JSONException e) {
				//Toast
			} finally {
				urlConnection.disconnect();
			}
		} catch (MalformedURLException e) {
			Log.i("Register:",e.toString());
		} catch (IOException e) {
			Log.i("Register:",e.toString());
		} catch (ClassCastException e){
			Log.i("Register:",e.toString());
		}

		Log.i("Register:","Registration Failed");
		return false;	//logged in successfully
	}

	public boolean register(){
		/*prompt for Email/Name/password from the UI element and try to register */		

		//TODO: Throw to UI element to get infos

		return doRegister();	//call login method


	}

	public void clearData(boolean userData, boolean courseData, boolean meetingData, boolean sessionData){
		if(userData) MainActivity.mContext.deleteFile("user.dat");	//clear the user's data
		if(meetingData) MainActivity.mContext.deleteFile("meetingList.dat");	//clear the user's data
		if(courseData) MainActivity.mContext.deleteFile("courseList.dat");	//clear the course data
		if(sessionData) MainActivity.mContext.deleteFile("sessions.dat");	//clear the user's data
	}

	public void loadUserData(boolean forceUpdate){
		/* Method will load user data from the serer in seperate threads, but will block until done */

		UserListner listner = new UserListner(this);  //setup the listner for the return

		Log.i("LoadUserData","Entered");

		if(forceUpdate || mCourseList.size() == 0){
			//get the full course list from the server 
			String courseListUrl = "http://lectureloot.eu1.frbit.net/api/v1/courses";
			HttpGetCourseList courseListTask = new HttpGetCourseList(mAuthToken);
			courseListTask.setHttpGetFinishedListener(listner);
			courseListTask.execute(new String[] {courseListUrl});

			listner.waitForThreads();
		}

		if(forceUpdate || mMeetingList.size() == 0){
			//get the full meeting list from the server 
			String meetingListUrl = "http://lectureloot.eu1.frbit.net/api/v1/meetings";
			HttpGetMeetingList meetingListTask = new HttpGetMeetingList(mAuthToken);
			meetingListTask.setHttpGetFinishedListener(listner);
			meetingListTask.execute(new String[] {meetingListUrl});

			listner.waitForThreads();
		}

		//attach the meetings to the courses
		Meeting.resolveMeetings(mMeetingList,mCourseList);

		//can't skip this (load the user)
		String userUrl = "http://lectureloot.eu1.frbit.net/api/v1/users/" + mUserId;
		HttpGetUser userTask = new HttpGetUser(mAuthToken);
		userTask.setHttpGetFinishedListener(listner);
		userTask.execute(new String[] {userUrl});		

		//load the courses from the server
		String courseUrl = "http://lectureloot.eu1.frbit.net/api/v1/users/" + mUserId + "/courses";
		HttpGetCourses courseTask = new HttpGetCourses(mAuthToken, null);
		courseTask.setHttpGetFinishedListener(listner);
		courseTask.execute(new String[] {courseUrl});

		//get the wagers frm the server
		String wagerUrl = "http://lectureloot.eu1.frbit.net/api/v1/users/" + mUserId + "/wagers";
		HttpGetWagers wagerTask = new HttpGetWagers(mAuthToken);
		wagerTask.setHttpGetFinishedListener(listner);
		wagerTask.execute(new String[] {wagerUrl});

		if(forceUpdate || mSessions.size() == 0){
			//get the sessions frm the server
			String sessionUrl = "http://lectureloot.eu1.frbit.net/api/v1/sessions";
			HttpGetSessions sessionTask = new HttpGetSessions(mAuthToken);
			sessionTask.setHttpGetFinishedListener(listner);
			sessionTask.execute(new String[] {sessionUrl});
		}
		Log.i("LoadUserData","Completed");

		listner.waitForThreads();

		loadedFlag = true;
	}

	public boolean validateData(){
	/* method will check if user data is valid, and will try to correct it if it's bad *
	 * Will return false only in the case of a critical failure						   */
		User user = new User();		
		
		//check if user exists on server
		if(!user.doLogin(mEmail,mPassword)){
			clearData(true, false, false, true); //delete user specific files only
			return false;
		}
		
		//get the data for the user and force the update
		user.loadUserData(true);
		
		//check if the data stored in the files is ok
		if(!user.mCourses.equals(mCourses))   clearData(false,true,false,false);
		if(!user.mMeetings.equals(mMeetings)) clearData(false,false,true,false);
		if(!user.mSessions.equals(mSessions)) clearData(false,false,false,true);
		
		//compare to the list
		if(!user.equals(this)){
			User.mInstance = user;	//set the new user instance
			user.clearData(true,false,false,false);	//clear the bad data
			user.writeToFile();		//write updated data we grabbed
			return false;
			//would prefer reloading screen and returning true, but idk how
		}
		return true;
	}
	
	@Override
	public boolean equals(Object o){
		User user;
		
		//make sure object is actually a user
		try{
			user = (User) o;
		} catch (ClassCastException e){
			return false;
		}
		
		//return result of long list of comparisons
		return (user.mUserId.equals(mUserId) 		&& user.mFirstName.equals(mFirstName) 		&&
				user.mLastName.equals(mLastName) 	&& user.mAuthToken.equals(mAuthToken) 		&&
				user.mEmail.equals(mEmail)		 	&& user.mPassword.equals(mPassword) 		&&
				user.mPoints == mPoints 			&& user.mWageredPoints == mWageredPoints	&&
				user.mWagers.equals(mWagers) 		&& user.mCourses.equals(mCourses) 			&&
				user.mMeetings.equals(mMeetings) 	&& user.mSessions.equals(mSessions) 		&&
				user.mCourseList.equals(mCourseList)&& user.mMeetingList.equals(mMeetingList));
	}
	
	/* GETTERS */
	public boolean loaded(){
		return loadedFlag;
	}

	public boolean loggedIn(){
		return loginFlag;
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

	public ArrayList<Meeting> getMeetingList(){
		return mMeetingList;
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

	public void setMeetingList(ArrayList<Meeting> meetingList) {
		mMeetingList = meetingList;
	}
}
