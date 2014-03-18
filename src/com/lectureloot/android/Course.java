package com.lectureloot.android;

import java.util.ArrayList;

import com.lectureloot.background.CourseListner;
import com.lectureloot.background.HttpGetMeetings;
import com.lectureloot.background.UserListner;

/**
 * Course Model
 * @author Austin
 * 
 * modified by Josh S.
 *
 */

public class Course {

	private int courseId;
	private String courseCode;
	private String courseTitle;
	private String sectionNumber;
	private String credits;
	private String instructor;
	private ArrayList<Meeting> meetings = new ArrayList<Meeting>();

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
	}

	/* constructor for current course setup */
	public Course(int courseID, String courseCode, String courseTitle, String sectionNumber, String credits,
			String instructor) {
		this.courseId = courseID;
		this.courseCode = courseCode;
		this.courseTitle = courseTitle;
		this.sectionNumber = sectionNumber;
		this.credits = credits;
		this.instructor = instructor;
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
	public ArrayList<Meeting> getMeetings() {
		return meetings;
	}

	public void setMeetings(ArrayList<Meeting> meetings) {
		this.meetings = meetings;
	}

	
	public void loadMeetings(UserListner user){
		/* method to load meetings from server (use internal threads/listner) */
		CourseListner listner = new CourseListner(this, user);
		
		//load the courses from the server
		String meetingUrl = "http://lectureloot.eu1.frbit.net/api/v1/courses/" + courseId + "/meetings";
		HttpGetMeetings meetingTask = new HttpGetMeetings(User.getInstance().getAuthToken());
		meetingTask.setHttpGetFinishedListener(listner);
		meetingTask.execute(new String[] {meetingUrl});
	}

}


