package com.lectureloot.android;

import java.util.ArrayList;

public class User {
	private String mName;
	private int mPoints;
	private int mWageredPoints;
	//private ArrayList<Wager> mWagers;
	private ArrayList<Course> mCourses;
	private ArrayList<Meeting> mMeetings;
	
	public User(String name){
		mName = name;
	}
	
	public void checkIn(){
		//get the user's location with gps
		//double check that with the location of the meeting
		
		//if it's close enough, check the user into the a meeting
	}
	
	public Meeting getUpcomingMeeting(){
		//go through the meetings arraylist and get the next meeting
		//based on the current time and checked in courses
		return new Meeting();
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

}
