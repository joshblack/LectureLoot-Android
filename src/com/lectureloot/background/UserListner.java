package com.lectureloot.background;

import java.util.ArrayList;
import java.sql.Date;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

import com.lectureloot.android.Course;
import com.lectureloot.android.HttpGetFinishedListener;
import com.lectureloot.android.Meeting;
import com.lectureloot.android.Sessions;
import com.lectureloot.android.User;
import com.lectureloot.android.Wager;

public class UserListner extends HttpGetFinishedListener{
	private User user;

	public UserListner(User user){
		this.user = user;
		threadCount = 0;
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
				
				if(Integer.parseInt(user.getUserId()) == (Integer)jsonCourse.get("user_id")){
				wager = new Wager(	
						(Integer)jsonCourse.get("id"),
						(Integer)jsonCourse.get("session_id"),
						(Integer)jsonCourse.get("wagerUnitValue"),
						(Integer)jsonCourse.get("wagerTotalValue"),
						(Integer)jsonCourse.get("pointsLost")
						);

				wagers.add(wager);
				}
			}
		} catch (Exception e) {
			//Toast
		}
		user.setWagers(wagers);
	}	

	public void onHttpGetCoursesReady(String output) {
		ArrayList<Course> courses = new ArrayList<Course>();
		ArrayList<Meeting> meetings = new ArrayList<Meeting>();
		try {
			JSONTokener tokener = new JSONTokener(output);
			JSONArray array = null;
			array = (JSONArray) tokener.nextValue();
			JSONObject jsonCourse;
			for(int i = 0; i < array.length(); i++) {
				Course course;
				jsonCourse = array.getJSONObject(i);
				course = user.getCourseList().get(jsonCourse.getInt("id") - 1);
				courses.add(course);
				for(int j=0; j<course.getMeetings().size();++j){
					meetings.add(course.getMeetings().get(j));
				}
			}
		} catch (Exception e) {
			Log.i("CourseLoad:",e.toString());
		}
		user.setCourses(courses);
		user.setMeetings(meetings);
	}

	public void onHttpGetCourseListReady(String output) {
		Log.i("CourseList:","Entered Method");
		try {
			JSONTokener tokener = new JSONTokener(output);
			JSONArray array = null;
			array = (JSONArray) tokener.nextValue();
			JSONObject jsonCourse;
			ArrayList<Course> courseList = new ArrayList<Course>();
			Course course;
			for(int i = 0; i < array.length(); i++) {
				jsonCourse = array.getJSONObject(i);

				course = new Course(	
						(Integer)jsonCourse.get("id"),
						(String)jsonCourse.getString("deptCode"),
						(String)jsonCourse.getString("courseNumber"),
						(String)jsonCourse.getString("courseTitle"),
						(String)jsonCourse.getString("sectionNumber"),
						(String)jsonCourse.getString("credits"),
						(String)jsonCourse.getString("instructor"),
						(String)jsonCourse.getString("semester"),
						(String)jsonCourse.getString("year")
						);

				//ignore meetings
				courseList.add(course);
			}
			user.setCourseList(courseList);
			Log.i("CourseList:","Complete, Size: " + courseList.size());
		} catch (Exception e) {
			//Toast
		}
	}
	
	public void onHttpGetMeetingListReady(String output) {
		try {
			JSONTokener tokener = new JSONTokener(output);
			JSONArray array = null;
			array = (JSONArray) tokener.nextValue();
			JSONObject jsonMeeting;
			ArrayList<Meeting> meetingList = new ArrayList<Meeting>();
			Meeting meeting;
			for(int i = 0; i < array.length(); i++) {
				jsonMeeting = array.getJSONObject(i);

				meeting = new Meeting(	
						(Integer)jsonMeeting.get("id"),
						Integer.parseInt(jsonMeeting.getString("course_id")),
						(String)jsonMeeting.getString("roomNumber"),
						(String)jsonMeeting.getString("meetingDay"),
						(String)jsonMeeting.getString("period")
						);

				//ignore meetings
				meetingList.add(meeting);
				meeting.addBuildingById((Integer)jsonMeeting.get("building_id"));
			}
			user.setMeetingList(meetingList);
			Log.i("MeetingList:","Completed, Size: " + meetingList.size());
		} catch (Exception e) {
			Log.i("MeetingList:",e.toString());
		}
	}

	public void onHttpGetSessionsReady(String output) {
		ArrayList<Sessions> sessions = new ArrayList<Sessions>();
		try {
			JSONTokener tokener = new JSONTokener(output);
			JSONArray array = null;
			array = (JSONArray) tokener.nextValue();
			JSONObject jsonCourse;
			Sessions session;
			for(int i = 0; i < array.length(); i++) {
				jsonCourse = array.getJSONObject(i);
				
				session = new Sessions(
						(Integer)jsonCourse.get("id"),
						Date.valueOf(jsonCourse.getString("startDate")),
						Date.valueOf(jsonCourse.getString("endDate"))
						);

				sessions.add(session);
			}
		} catch (Exception e) {
			Log.i("Sessions:",e.toString());
		}
		user.setSessions(sessions);

	}

}