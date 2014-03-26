package com.lectureloot.android;

import java.util.ArrayList;

import android.util.Log;

import com.lectureloot.background.HttpGetBuilding;
import com.lectureloot.background.MeetingListner;

public class Meeting {
	
	private int meetingId;
	private int courseId;
	private double latitude;
	private double longitude;
	private String buildingCode;
	private String roomNumber;
	private String meetingDay;
	private String period;
	private long time;	//is this even used?
	
	public Meeting(){
		
	}
	
	//this can be deleted later
	public Meeting(long timeInMillis){
		this.time = timeInMillis;
	}
	
	public Meeting(int meetingId, int courseId, String roomNumber, String meetingDay, String period){
		this.meetingId = meetingId;
		this.courseId = courseId;
		this.roomNumber = roomNumber;
		this.meetingDay = meetingDay;
		this.period = period;
	}
	
	public void addBuildingById(int buildingId){
		/* method to load meetings from server for multiple courses (use internal threads/listner) */
		MeetingListner listner = new MeetingListner(this);
		
		//load the courses from the server
		String buildingUrl = "http://lectureloot.eu1.frbit.net/api/v1/buildings/" + buildingId;
		HttpGetBuilding buildingTask = new HttpGetBuilding(User.getInstance().getAuthToken());
		buildingTask.setHttpGetFinishedListener(listner);
		buildingTask.execute(new String[] {buildingUrl});
		
		//do this asynchronously (should catch up by end of other stuff)
	}
	
	public int getMeetingId() {
		return meetingId;
	}
	public void setMeetingId(int meetingId) {
		this.meetingId = meetingId;
	}
	public int getCourseId() {
		return courseId;
	}
	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}
	public String getBuildingCode() {
		return buildingCode;
	}
	public void setBuildingCode(String buildingCode) {
		this.buildingCode = buildingCode;
	}
	public String getRoomNumber() {
		return roomNumber;
	}
	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}
	public String getMeetingDay() {
		return meetingDay;
	}
	public void setMeetingDay(String meetingDay) {
		this.meetingDay = meetingDay;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public void setLatitude(double lat){
		latitude = lat;
	}
	public void setLongitude(double log){
		longitude = log;
	}
	public double getLatitude(){
		return latitude;
	}
	public double getLongitude(){
		return longitude;
	}
	
	
	//this can be deleted later, for testing purposes
	public void setTime(long timeInMillis){
		this.time = timeInMillis;
	}
	
	public long getTimeInMillis(){
		return this.time;
	}
	
	public static void resolveMeetings(ArrayList<Meeting> meetings, ArrayList<Course> courses){
		for(int i=0;i<meetings.size();++i){
			//add the meetings to the courses
			Meeting m = meetings.get(i);
			try{
				courses.get(m.courseId - 1).getMeetings().add(m);
			} catch (IndexOutOfBoundsException e){
				Log.w("ResolveMeetings:",e.toString());
			}	//ignore extra meetings
		}
	}

}
