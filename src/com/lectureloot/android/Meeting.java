package com.lectureloot.android;

import java.util.ArrayList;

import android.util.Log;

import com.lectureloot.background.HttpGetBuilding;
import com.lectureloot.background.MeetingListner;

public class Meeting implements Comparable<Meeting>{
	
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
	
	public int compareTo(Meeting comparedAgainst) {
		String meetingDays = "MTWRFSN";
		String periods = "1234567891011E1E2E3";
		String thisMeetingPeriodValid = this.period.trim();
		// Need to grab the first period of the period field if there is a range of periods being represented (E1E3)
		switch(thisMeetingPeriodValid.length()) {
		case 4:
			if (thisMeetingPeriodValid.charAt(0) == 'E') {
				thisMeetingPeriodValid = thisMeetingPeriodValid.substring(0,2);
			} else if (thisMeetingPeriodValid.charAt(1) == '-') {
				thisMeetingPeriodValid = thisMeetingPeriodValid.substring(0,1);
			} else {
				thisMeetingPeriodValid = thisMeetingPeriodValid.substring(0,2);
			}
			
		case 3:
			thisMeetingPeriodValid = thisMeetingPeriodValid.substring(0,1);
		case 2:
		case 1:
		}
		
		String comparedMeetingPeriodValid = comparedAgainst.getPeriod().trim();
		// Need to grab the first period of the period field if there is a range of periods being represented (E1E3)
		switch(comparedMeetingPeriodValid.length()) {
		case 4:
			if (comparedMeetingPeriodValid.charAt(0) == 'E') {
				comparedMeetingPeriodValid = comparedMeetingPeriodValid.substring(0,2);
			} else if (comparedMeetingPeriodValid.charAt(1) == '-') {
				comparedMeetingPeriodValid = comparedMeetingPeriodValid.substring(0,1);
			} else {
				comparedMeetingPeriodValid = comparedMeetingPeriodValid.substring(0,2);
			}
			
		case 3:
			comparedMeetingPeriodValid = comparedMeetingPeriodValid.substring(0,1);
		case 2:
		case 1:
		}
		
		
		if(meetingDays.indexOf(this.meetingDay.trim().toUpperCase()) > meetingDays.indexOf(comparedAgainst.meetingDay.trim().toUpperCase())) {
			return 1;
		} else if (meetingDays.indexOf(this.meetingDay) == meetingDays.indexOf(comparedAgainst.meetingDay)){
			if(periods.indexOf(thisMeetingPeriodValid.trim().toUpperCase()) > periods.indexOf(comparedMeetingPeriodValid.trim().toUpperCase())) {
				return 1;
			} else if (periods.indexOf(thisMeetingPeriodValid.trim().toUpperCase()) < periods.indexOf(comparedMeetingPeriodValid.trim().toUpperCase())){
				return -1;
			} else {
				return 0;
			}
		} else {
			return -1;
		}
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
	
	public Meeting(Meeting m){
		meetingId = m.meetingId;
		courseId = m.courseId;
		latitude = m.latitude;
		longitude = m.longitude;
		buildingCode = m.buildingCode;
		roomNumber = m.roomNumber;
		meetingDay = m.meetingDay;
		period = m.period;
		time = m.time;	//is this even used?
		
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
	
	public boolean equals(Object o){
		Meeting m;
		try{
			m = (Meeting) o;
			
			if	 (!(meetingId == m.meetingId			&& courseId == m.courseId			&&
					buildingCode.equals(m.buildingCode) && roomNumber.equals(m.roomNumber)	&&
					meetingDay.equals(m.meetingDay)		&& period.equals(m.period))) return false;

			//range check the double values (lat/long) (5% allowable error)
			if((Math.abs(latitude - m.latitude) > Math.abs(latitude) * .05	||
					Math.abs(longitude - m.longitude) > Math.abs(longitude) * .05)){
				Log.i("Meetings:","Lat/Long comparison failure");
				return false;
			}

		} catch (Exception e){ return false; }
		return true;
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
	
	public String toString() {
		String result = "";
		
		result += "courseId: " + this.getCourseId() + ", ";
		result += "meetingId: " + this.getMeetingId() + ", ";
		result += "meetingDay: \"" + this.getMeetingDay() + "\", ";
		result += "period: \"" + this.getPeriod() + "\", ";
		result += "building: \"" + this.getBuildingCode() + "\", ";
		result += "roomNumber: \"" + this.getRoomNumber() + "\"";
		
		return result;
	}

}
