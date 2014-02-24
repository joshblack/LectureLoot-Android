package com.lectureloot.android;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.text.format.Time;

public class User {
	private static User mInstance = null;
	private String mName;
	private int mPoints;
	private int mWageredPoints;
	//private ArrayList<Wager> mWagers;
	private ArrayList<Course> mCourses;
	private ArrayList<Meeting> mMeetings;
	
	/* CONSTRUCTOR FOR USER */
	
	private User(){
		
	}
	
	public static User getInstance(){
		if(mInstance == null){
            mInstance = new User();
        }
        return mInstance;
	}
	
	/* CONSTRUCTOR FOR User WITH SUPPLIED NAME */
	
	private User(String name){
		mName = name;
		mPoints = 0;
		mWageredPoints = 0;
		//mWagers = null;
		mCourses = null;
		mMeetings = null;
		
		//TODO now need to reflect this in the database, unless it does it automatically?
		//Or just pull from the Database and it'll set this automatically?
	}
	
	public static User getInstance(String name){
		//this is for development purposes
		if(mInstance == null){
            mInstance = new User(name);
        }
        return mInstance;
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

//	public ArrayList<Wager> getWagers() {
//		return mWagers;
//	}

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
	
//	public void setWagers(ArrayList<Wager> wagers) {
//		mWagers = wagers;
//	}	

	public void setCourses(ArrayList<Course> courses) {
		mCourses = courses;
	}

	public void setMeetings(ArrayList<Meeting> meetings) {
		mMeetings = meetings;
	}

}
