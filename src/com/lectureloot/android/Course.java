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
	private String coursePrefix;
	private String courseNum;
	private String courseTitle;
	private String sectionNumber;
	private String credits;
	private String instructor;
	private ArrayList<Meeting> meetings = new ArrayList<Meeting>();

	public Course () {

	}

	/* constructor for current course setup */
	public Course(int courseID, String coursePrefix, String courseNum, String courseTitle, String sectionNumber, String credits,
			String instructor) {
		this.courseId = courseID;
		this.coursePrefix = coursePrefix;
		this.courseNum = courseNum;
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
		return coursePrefix + courseNum;
	}
	
	public void setCourseCode(String courseCode){
		coursePrefix = courseCode.substring(0,2);
		courseNum = courseCode.substring(3);
	}

	public String getCoursePrefix(){
		return coursePrefix;
	}
	
	public String getCourseNum(){
		return courseNum;
	}

	public void setCourseNum(String courseNum){
		this.courseNum = courseNum;
	}
	
	public void setCoursePrefix(String coursePrefix){
		this.coursePrefix = coursePrefix;
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
		/* method to load meetings from server for multiple courses (use internal threads/listner) */
		CourseListner listner = new CourseListner(this, user);
		
		//load the courses from the server
		String meetingUrl = "http://lectureloot.eu1.frbit.net/api/v1/courses/" + courseId + "/meetings";
		HttpGetMeetings meetingTask = new HttpGetMeetings(User.getInstance().getAuthToken());
		meetingTask.setHttpGetFinishedListener(listner);
		meetingTask.execute(new String[] {meetingUrl});
	}
}


