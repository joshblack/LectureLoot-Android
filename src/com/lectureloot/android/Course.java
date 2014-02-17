package com.lectureloot.android;
/**
 * Course Model
 * @author Austin
 *
 */

public class Course {
	
	private int courseId;
	private String courseCode;
	private String courseTitle;
	private String sectionNumber;
	private String credits;
	private String instructor;
	private String meetingDays1;
	private String meetingDays2;
	private String meetingDays3;
	private String period1;
	private String period2;
	private String period3;
	private String room1;
	private String room2;
	private String room3;
	
	public Course () {
		
	}
	
	public Course(String courseCode, String courseTitle, String sectionNumber, String credits,
			String instructor, String meetingDays1, String meetingDays2,
			String meetingDays3, String period1, String period2,
			String period3, String room1, String room2, String room3) {
		this.courseCode = courseCode;
		this.courseTitle = courseTitle;
		this.sectionNumber = sectionNumber;
		this.credits = credits;
		this.instructor = instructor;
		this.meetingDays1 = meetingDays1;
		this.meetingDays2 = meetingDays2;
		this.meetingDays3 = meetingDays3;
		this.period1 = period1;
		this.period2 = period2;
		this.period3 = period3;
		this.room1 = room1;
		this.room2 = room2;
		this.room3 = room3;
	}
	
	public Course(int courseId, String courseCode, String courseTitle, String sectionNumber, String credits,
			String instructor, String meetingDays1, String meetingDays2,
			String meetingDays3, String period1, String period2,
			String period3, String room1, String room2, String room3) {
		this.courseId = courseId;
		this.courseCode = courseCode;
		this.courseTitle = courseTitle;
		this.sectionNumber = sectionNumber;
		this.credits = credits;
		this.instructor = instructor;
		this.meetingDays1 = meetingDays1;
		this.meetingDays2 = meetingDays2;
		this.meetingDays3 = meetingDays3;
		this.period1 = period1;
		this.period2 = period2;
		this.period3 = period3;
		this.room1 = room1;
		this.room2 = room2;
		this.room3 = room3;
	}


	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public String getCourseCode() {
		return courseCode;
	}


	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public String getCourseTitle() {
		return courseTitle;
	}
	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}
	public String getSectionNumber() {
		return sectionNumber;
	}
	public void setSectionNumber(String sectionNumber) {
		this.sectionNumber = sectionNumber;
	}
	public String getCredits() {
		return credits;
	}
	public void setCredits(String credits) {
		this.credits = credits;
	}
	public String getInstructor() {
		return instructor;
	}
	public void setInstructor(String instructor) {
		this.instructor = instructor;
	}
	public String getMeetingDays1() {
		return meetingDays1;
	}
	public void setMeetingDays1(String meetingDays1) {
		this.meetingDays1 = meetingDays1;
	}
	public String getMeetingDays2() {
		return meetingDays2;
	}
	public void setMeetingDays2(String meetingDays2) {
		this.meetingDays2 = meetingDays2;
	}
	public String getMeetingDays3() {
		return meetingDays3;
	}
	public void setMeetingDays3(String meetingDays3) {
		this.meetingDays3 = meetingDays3;
	}
	public String getPeriod1() {
		return period1;
	}
	public void setPeriod1(String period1) {
		this.period1 = period1;
	}
	public String getPeriod2() {
		return period2;
	}
	public void setPeriod2(String period2) {
		this.period2 = period2;
	}
	public String getPeriod3() {
		return period3;
	}
	public void setPeriod3(String period3) {
		this.period3 = period3;
	}
	public String getRoom1() {
		return room1;
	}
	public void setRoom1(String room1) {
		this.room1 = room1;
	}
	public String getRoom2() {
		return room2;
	}
	public void setRoom2(String room2) {
		this.room2 = room2;
	}
	public String getRoom3() {
		return room3;
	}
	public void setRoom3(String room3) {
		this.room3 = room3;
	}
}
