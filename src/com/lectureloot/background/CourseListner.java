package com.lectureloot.background;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

import com.lectureloot.android.Course;
import com.lectureloot.android.HttpGetFinishedListener;
import com.lectureloot.android.Meeting;

public class CourseListner extends HttpGetFinishedListener{
	Course course;
	UserListner user;
	ArrayList<Meeting> meetings;
	
	public CourseListner(Course course, UserListner user){
		this.course = course;
		this.user = user;
		this.meetings = new ArrayList<Meeting>(); 
	}
	
	public void onHttpGetMeetingsReady(String output) {
		ArrayList<Meeting> meetings = new ArrayList<Meeting>();
		try {
			JSONTokener tokener = new JSONTokener(output);
			JSONArray array = null;
			array = (JSONArray) tokener.nextValue();
			JSONObject jsonCourse;
			Meeting meeting;
			for(int i = 0; i < array.length(); i++) {
				jsonCourse = array.getJSONObject(i);
				
				meeting = new Meeting(	
				(Integer)jsonCourse.get("id"),
				Integer.parseInt((String)jsonCourse.get("course_id")),
				(String)jsonCourse.getString("roomNumber"),
				(String)jsonCourse.getString("meetingDay"),
				(String)jsonCourse.getString("period")
				);
				
				meeting.addBuildingById((Integer)jsonCourse.get("building_id"));
				meetings.add(meeting);
				user.addMeeting(meeting);
			}
			course.setMeetings(meetings);
		} catch (Exception e) {
			Log.i("Meetings:",e.toString());
		}
	}
}
