package com.lectureloot.android;

import java.util.ArrayList;

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
	private String semester;
	private String year;	
	private ArrayList<Meeting> meetings = new ArrayList<Meeting>();

	public Course () {

	}

	/* constructor for current course setup */
	public Course(int courseID, String coursePrefix, String courseNum, String courseTitle, String sectionNumber, String credits,
			String instructor, String semester, String year) {
		this.courseId = courseID;
		this.coursePrefix = coursePrefix;
		this.courseNum = courseNum;
		this.courseTitle = courseTitle;
		this.sectionNumber = sectionNumber;
		this.credits = credits;
		this.instructor = instructor;
		this.semester = semester;
		this.year = year;		
	}
	
	public Course(int courseID, String coursePrefix, String courseNum, String courseTitle, String sectionNumber, String credits,
			String instructor) {
		this.courseId = courseID;
		this.coursePrefix = coursePrefix;
		this.courseNum = courseNum;
		this.courseTitle = courseTitle;
		this.sectionNumber = sectionNumber;
		this.credits = credits;
		this.instructor = instructor;
		this.semester = "";
		this.year = "";		
	}
	
	public boolean equals(Object o){
		Course c;
		try{
			c = (Course) o;
		} catch(ClassCastException e){ return false;}
		return (courseId == c.courseId					&& coursePrefix.equals(c.coursePrefix)	&&
				courseNum.equals(c.courseNum)			&& courseTitle.equals(c.courseTitle) 	&&
				sectionNumber.equals(c.sectionNumber)	&& credits.equals(c.credits) 			&&
				instructor.equals(c.instructor)			&& semester.equals(c.semester) 			&&
				year.equals(c.year) 					&& meetings.equals(c.meetings));
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

	public String getSemester(){
		return semester;
	}
	
	public String getYear(){
		return year;
	}
	
	public void setSemester(String semester){
		this.semester = semester; 
	}
	
	public void setYear(String year){
		this.year = year;
	}
}


