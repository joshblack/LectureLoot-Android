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

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.lectureloot.background.HttpGet;

import android.text.format.Time;

@SuppressWarnings("unused")
public class User {
	private static User mInstance = null;
	private String mName;
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
		mName = "";
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
		mName = name;
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
		//try to open file. If it exists, load the data
		try{
			FileInputStream fis = MainActivity.mContext.openFileInput("user.dat");
			BufferedReader in = new BufferedReader(new InputStreamReader(fis));	
			
			//grab the top level data
			this.mName = in.readLine().split(":")[1];
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
				wager.setWagerSessionCode(Integer.parseInt(inLine[1]));
				wager.setWagerPerMeeting(Integer.parseInt(inLine[2]));
				wager.setTotalMeetings(Integer.parseInt(inLine[3]));
				wager.setTotalWager(Integer.parseInt(inLine[4]));
				
				wagers.add(wager);	//add to arrayList
			}
			this.mWagers = wagers;
			
			in.close();
			fis.close();
			
		//If the file doesn't exist, get data from server then make it
		} catch (FileNotFoundException e){
			login();	//login to server (generate auth token)
			//grab data from server I HAVE NO IDEA HOW TO DO THIS
			//block until done		WRITE THIS TOO (not as hard)
			writeToFile();
		} catch (NumberFormatException e) {
			//Add soemthing for this if I feel like it
			e.printStackTrace();
		} catch (IOException e) {
			//Add soemthing for this if I feel like it
			e.printStackTrace();
		}
	}

	public boolean writeToFile(){
		//try to open file. If it exists, load the data
		try{
			FileOutputStream out = MainActivity.mContext.openFileOutput("user.dat", 0);	
			
			//write user data
			out.write(("Name:" + mName + "\n").getBytes());
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
				out.write(("Wager:" + mCourses.get(i).getCourseId()).getBytes());
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
	
	public boolean login(String Name, String password){
		/*automaticly log the user in and generate authToken*/
		
		//TODO: display some sort of 'logging in' popup here (currently just blocking)
		
		//get the info from the server
		URL url;
		try {
			url = new URL("http://lectureloot.com/api/v1/users/login?emailAddress=" + mEmail + "&password=" + mPassword);
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
	
	public boolean login(){
		/*prompt for Name/password from the UI element and try to login */		
		
		//TODO: Throw to UI element to get Username/Password
		
		return login("username", "password");	//call login method
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
	
//	public void addWager(Wager newWager){
//		
//	}
	
	public void addCourse(Course newCourse){
		
	}
	
	public void addMeeting(Meeting newMeeting){
		
	}
	
	public void addToPoints(int newPoints){
		mPoints += newPoints;
	}
	
	/* GETTERS */

	public String getName() {
		return mName;
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

	public void setName(String name) {
		mName = name;
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

//class for synchronous locking (basically semaphores)
class UserLock{
	private boolean locked;
	
	UserLock(){
		locked = false;
	}
	
	//lock the object and wait (block) until unlocked
	protected void lock(){
		locked = true;
		while(locked) {
	        try {
	            wait();
	        } catch (InterruptedException e) {}
	    }
	}
	
	protected void unlock(){
		locked = false;
		notify();
	}
}