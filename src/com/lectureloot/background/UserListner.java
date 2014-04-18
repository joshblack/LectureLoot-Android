package com.lectureloot.background;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
	private ArrayList<Thread> threads;

	public UserListner(User user){
		this.user = user;
		threadCount = 0;
		threads = new ArrayList<Thread>();
	}
	
	public void onHttpGetUserReady(String output){
		JSONTokener tokener = new JSONTokener(output);
		JSONObject jsonCourse = null;
		try {
			jsonCourse = (JSONObject) tokener.nextValue();
			user.setFirstName(jsonCourse.getString("firstName"));
			user.setLastName(jsonCourse.getString("lastName"));
			user.setPoints((Integer)jsonCourse.get("pointBalance"));
		} catch (Exception e) {
			Log.w("GetUser:",e.toString() + " - " + output);
		}
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
		try {
			JSONTokener tokener = new JSONTokener(output);
			JSONArray array = null;
			array = (JSONArray) tokener.nextValue();
			JSONObject jsonCourse;
			for(int i = 0; i < array.length(); i++) {
				jsonCourse = array.getJSONObject(i);
				Course course = new Course(jsonCourse.getInt("id"),
						jsonCourse.getString("deptCode"),
						jsonCourse.getString("courseNumber"),
						jsonCourse.getString("courseTitle"),
						jsonCourse.getString("sectionNumber"),
						jsonCourse.getString("credits"),
						jsonCourse.getString("instructor"),
						jsonCourse.getString("semester"),
						jsonCourse.getString("year"));
				user.getCourses().add(course);				
				final int id = course.getCourseId(); 
				
				Thread thread = new Thread(new Runnable(){
					public void run(){
						notifyThreadStart();
						//synchronously get the meetings (avoid starting too many threads)
						URL url;
						try {
							url = new URL("http://lectureloot.eu1.frbit.net/api/v1/courses/" + id + "/meetings");
							HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
							urlConnection.setRequestMethod("GET");
							urlConnection.setRequestProperty("Authorization", user.getAuthToken()); //HEADER for access token
							BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
							onHttpGetMeetingsReady(in.readLine());
						} catch (Exception e) {
							Log.i("Meeting Load:",e.toString());						}
						threads.remove(this);
						notifyThreadComplete();
					}
				});
				threads.add(thread);
				thread.start();
				
				/*UserListner listner = new UserListner(user);
				String meetingUrl = "http://lectureloot.eu1.frbit.net/api/v1/courses/" + course.getCourseId() + "/meetings";
				HttpGetMeetings meetingTask = new HttpGetMeetings(user.getAuthToken());
				meetingTask.setHttpGetFinishedListener(listner);
				meetingTask.execute(new String[] {meetingUrl});
				listner.waitForThreads();*/
			}
		} catch (Exception e) {
			Log.i("CourseLoad:",e.toString() + " - " + output);
		}
	}
	
	public void onHttpGetMeetingsReady(String output) {
		try {
			JSONTokener tokener = new JSONTokener(output);
			JSONArray array = null;
			array = (JSONArray) tokener.nextValue();
			JSONObject jsonMeeting;
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
				user.getMeetings().add(meeting);
				meeting.addBuildingById((Integer)jsonMeeting.get("building_id"));
			}
		} catch (Exception e) {
			Log.i("Meetings:",e.toString());
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