package com.lectureloot.android;

public class Meeting {
	
	private int meetingId;
	private int courseId;
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
	
	public Meeting(int meetingId, int courseId, String buildingCode, String roomNumber, String meetingDay, String period){
		this.meetingId = meetingId;
		this.courseId = courseId;
		this.buildingCode = buildingCode;
		this.roomNumber = roomNumber;
		this.meetingDay = meetingDay;
		this.period = period;
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
	
	//this can be deleted later, for testing purposes
	public void setTime(long timeInMillis){
		this.time = timeInMillis;
	}
	
	public long getTimeInMillis(){
		return this.time;
	}

}
