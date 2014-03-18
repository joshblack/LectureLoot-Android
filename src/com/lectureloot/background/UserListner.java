package com.lectureloot.background;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.lectureloot.android.Course;
import com.lectureloot.android.HttpGetFinishedListener;
import com.lectureloot.android.Meeting;
import com.lectureloot.android.User;
import com.lectureloot.android.Wager;

public class UserListner implements HttpGetFinishedListener{
	private User user;
	private ArrayList<Course> courses;
	private ArrayList<Meeting> meetings;
	
	public UserListner(){
		user = User.getInstance();
		courses = new ArrayList<Course>();
		meetings = new ArrayList<Meeting>();
	}
	
	public void onHttpGetWagersReady(String output) {
		ArrayList<Wager> wagers = new ArrayList<Wager>();
		try {
			JSONTokener tokener = new JSONTokener(output);
			JSONArray array = null;
			array = (JSONArray) tokener.nextValue();
			JSONObject jsonCourse;
			Wager wager;
			for(int i = 0; i < array.length(); i++) {
				jsonCourse = array.getJSONObject(i);
				
				wager = new Wager(	
				(Integer)jsonCourse.get("id"),
				(Integer)jsonCourse.get("user_id"),
				(Integer)jsonCourse.get("session_id"),
				(Integer)jsonCourse.get("wagerUnitValue"),
				(Integer)jsonCourse.get("wagerTotalValue"),
				(Integer)jsonCourse.get("pointsLost")
				);
				
				wagers.add(wager);
			}
		} catch (Exception e) {
			//Toast
		}
		user.setWagers(wagers);
	}	

	public void onHttpGetMeetingsReady(String output) {
		//do nothing (meetins are loaded in Course class)
	}

	public void onHttpGetCoursesReady(String output) {
		try {
			JSONTokener tokener = new JSONTokener(output);
			JSONArray array = null;
			array = (JSONArray) tokener.nextValue();
			JSONObject jsonCourse;
			Course course;
			for(int i = 0; i < array.length(); i++) {
				jsonCourse = array.getJSONObject(i);
				
				course = new Course(	
				(Integer)jsonCourse.get("id"),
				((String)jsonCourse.getString("deptCode") + (String)jsonCourse.getString("courseNumber")),
				(String)jsonCourse.getString("courseTitle"),
				(String)jsonCourse.getString("sectionNumber"),
				(String)jsonCourse.getString("credits"),
				(String)jsonCourse.getString("instructor")
				);
				
				course.loadMeetings(this); //let the Course class load the meetings
				
				courses.add(course);
			}
		} catch (Exception e) {
			//Toast
		}
		user.setCourses(courses);
		user.setMeetings(meetings);
	}	

	public void addMeeting(Meeting meeting){
		/* Method will grab meeting from external source and add it to the meetings array list. * 
		 *  This way, the courses can find thier own meetings and update the master list later  */
		meetings.add(meeting);
	}
}
