package com.lectureloot.android;

/**
 * @author Austin Bruch, Justin Rafanan
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lectureloot.android.adapter.ExpandableListCourseAdapter;
import com.lectureloot.background.HttpGetCourses;
import com.lectureloot.background.HttpGetMeetings;
import com.lectureloot.background.HttpPostCourses;


public class ScheduleFragment extends Fragment implements OnItemSelectedListener{

	private ExpandableListCourseAdapter listAdapter;
	private ExpandableListView expListView;
	private List<String> listDataHeader = null;
	private HashMap<String, List<Course>> listDataChild = null;
	private User user;
//	private int courseId = -69; // used to setup Course Post with courseId

	private final Handler toaster = new Handler() {
		 public void handleMessage(Message msg) {
	      	Toast.makeText(getActivity(),(String)msg.obj, Toast.LENGTH_SHORT).show();
	     }
	 };

	public ScheduleFragment() {
		this.user = User.getInstance();
	}
	@Override
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);
		//final AdapterView aView = (AdapterView)rootView;
		
		user = User.getInstance();

		TextView userDisplay = (TextView)rootView.findViewById(R.id.username);
		userDisplay.setText("Your Schedule");
		userDisplay.setTypeface(null, Typeface.BOLD_ITALIC);
		userDisplay.setTextSize(25);

		//get the listview
		expListView = (ExpandableListView) rootView.findViewById(R.id.lvExp);

		//AsyncTask testing

		prepareListData();
		listAdapter = new ExpandableListCourseAdapter(getActivity(), prepareDataHeader(), prepareDataChild());

		// setting list adapter
		expListView.setAdapter(listAdapter);

		Button addNewCourseButton;
		addNewCourseButton = (Button)rootView.findViewById(R.id.addButton);
		addNewCourseButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				final Dialog dialog = new Dialog(getActivity());
				dialog.setContentView(R.layout.dialog_add_course);
				dialog.setTitle("Add Course");

				final EditText sectionNumView;
				sectionNumView = (EditText) dialog.findViewById(R.id.SectionNumberTextView);


				Button dialogButton = (Button) dialog.findViewById(R.id.dialogAddButton);
				dialogButton.setOnClickListener(new OnClickListener() {
						private Thread workerThread;
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						//get section number from text view

						final String sectionNumStr = sectionNumView.getText().toString();
						System.out.println(sectionNumStr);

						boolean sameCourseFlag = false;


						ArrayList<Course> userCourses = user.getCourses();
//											boolean sameCourse = false;
						for(Course userCourse : userCourses){
							if(userCourse.getSectionNumber() == sectionNumStr){
//									sameCourse = true;
								Toast.makeText(getActivity(), "Already Registered for " + userCourse.getCourseTitle(), Toast.LENGTH_LONG).show();
								sameCourseFlag = true;
								break;
							}
						}
		
//						System.out.println(courseId);
//						Log.v("", "" + sameCourseFlag);
						
						if(sameCourseFlag == false){	
							
							//					send server request
							String authToken = user.getAuthToken();
							HttpPostCourses coursesPost = new HttpPostCourses(authToken);	

							//listener to handle POST course response
							HttpPostCoursesFinishedListener listener = new HttpPostCoursesFinishedListener(){								
								@Override
								public void onHttpPostCoursesReady(String output){
									try {
										Log.i("Add Course:","Output: " + output);
										JSONTokener tokener = new JSONTokener(output);
										JSONObject jsonReturn = (JSONObject) tokener.nextValue();
									} catch (Exception e) {
										Log.i("Add Course:",e.toString());
									}
								}	
							};
							coursesPost.setHttpPostCoursesFinishedListener(listener);


							//							coursesPost.execute(new String[] {coursesUrl});
							workerThread = new Thread( new Runnable(){
								public void run(){
									String result = user.addCourse(sectionNumStr,true);
									if(result != null){	//display error message if valid
										Message msg = toaster.obtainMessage();
										msg.obj = result;
										toaster.sendMessage(msg);
									}else{ //otherwise add the course
										getActivity().runOnUiThread(new Runnable(){
											//this is stupid (have to run reloadItems() on main thread)
											public void run() {
												listAdapter.reloadItems(prepareDataHeader(), prepareDataChild());
											}
										});
									}
									workerThread = null;
								}
							});
							workerThread.start();
							

							dialog.dismiss();

						}
						else 
						{
							Toast.makeText(getActivity(), "Invalid Course", Toast.LENGTH_LONG).show();
							dialog.dismiss();
						}

					}
				});

				dialog.show();

			}
		});
		return rootView;
	}

	private ArrayList<Meeting> groupMeetingsDays (ArrayList<Meeting> meetingsIn) {
		//copy the meetings to a new array to avoid corrupting the origional data
		ArrayList<Meeting> meetings = new ArrayList<Meeting>();
		for(Meeting m : meetingsIn) meetings.add(new Meeting(m));
				
		ArrayList<Meeting> groupedMeetingsDays = new ArrayList<Meeting>();
//		System.out.println("enter groupMeetingDays");
		Meeting comparedAgainstMeeting = null;
		Meeting testMeeting = null;
		Meeting compareTestMeeting = null;
		for(int i = 0; i < meetings.size(); i++) {
			comparedAgainstMeeting = meetings.get(i);
			compareTestMeeting = meetings.get(i);
//			System.out.println(compareTestMeeting.toString());
			boolean flag = false;
			for(int j = i+1; j < meetings.size(); j++) {
				testMeeting = meetings.get(j);
//				System.out.println("Test"+ testMeeting.toString());
				//does nothing past this point
				if (comparedAgainstMeeting.getPeriod().equalsIgnoreCase(testMeeting.getPeriod()) 
						&& comparedAgainstMeeting.getBuildingCode().equalsIgnoreCase(testMeeting.getBuildingCode()) 
						&& comparedAgainstMeeting.getRoomNumber().equalsIgnoreCase(testMeeting.getRoomNumber())  && i != j) {
					boolean contains = false;
					comparedAgainstMeeting.setMeetingDay(comparedAgainstMeeting.getMeetingDay().concat(testMeeting.getMeetingDay()));
					for(Meeting groupedMeeting : groupedMeetingsDays) {
						if (groupedMeeting.getMeetingDay().contains(comparedAgainstMeeting.getMeetingDay())) {
							contains = true;
						}
					}
					if (contains == false) {
						groupedMeetingsDays.add(comparedAgainstMeeting);
					}
				}
			}
			if (comparedAgainstMeeting.getMeetingDay().length() == 1) {
				for(Meeting grouped : groupedMeetingsDays) {
					if (comparedAgainstMeeting.getPeriod().equalsIgnoreCase(grouped.getPeriod()) 
							&& comparedAgainstMeeting.getBuildingCode().equalsIgnoreCase(grouped.getBuildingCode()) 
							&& comparedAgainstMeeting.getRoomNumber().equalsIgnoreCase(grouped.getRoomNumber())) {
						flag = true;
					}
				}
				if (flag == false) {
					groupedMeetingsDays.add(comparedAgainstMeeting);
				}
			}
		}
		if (groupedMeetingsDays.size() == 0) {

			groupedMeetingsDays.addAll(meetings);
		}

		for (int i = 0; i < groupedMeetingsDays.size(); i++) {
			Meeting groupedMeeting = groupedMeetingsDays.get(i);
			String meet = groupedMeeting.getMeetingDay().toUpperCase();
			String sortedMeet = " ";

			if (meet.indexOf("M") != -1) {
				sortedMeet+=("M");
			}
			if (meet.indexOf("T") != -1) {
				sortedMeet+=("T");
			}
			if (meet.indexOf("W") != -1) {
				sortedMeet+=("W");
			}
			if (meet.indexOf("R") != -1) {
				sortedMeet+=("R");
			}
			if (meet.indexOf("F") != -1) {
				sortedMeet+=("F");
			}
			if (meet.indexOf("S") != -1) {
				sortedMeet+=("S");
			}

			sortedMeet.trim();

			groupedMeeting.setMeetingDay(sortedMeet);
			groupedMeetingsDays.set(i,groupedMeeting);
		}

		return groupedMeetingsDays;
	}

	public ArrayList<String> prepareDataHeader() {
		ArrayList<String> header = new ArrayList<String>();
		listDataChild = new HashMap<String, List<Course>>();

		ArrayList<Course> courses = user.getCourses();
//		System.out.println("COURSES ARRAY LIST" + courses.toString());


		List<Course> oneCourseList = null;
		for (Course course : courses) {
			header.add(Integer.toString(course.getCourseId()));
			oneCourseList = new ArrayList<Course>();
			oneCourseList.add(course);
			listDataChild.put(Integer.toString(course.getCourseId()),oneCourseList);
//			System.out.println(course.getCourseId());
		}

		for (String courseId : header) {
			getMeetingsReady(courseId);
//			System.out.println(courseId);
		}

		return header;

	}

	public HashMap<String, List<Course>> prepareDataChild() {
		ArrayList<String> header = prepareDataHeader();
		HashMap<String, List<Course>> child = new HashMap<String, List<Course>>();

		ArrayList<Course> courses = user.getCourses();
//		System.out.println("COURSES ARRAY LIST" + courses.toString());


		List<Course> oneCourseList = null;
		for (Course course : courses) {
			header.add(Integer.toString(course.getCourseId()));
			oneCourseList = new ArrayList<Course>();
			oneCourseList.add(course);
			child.put(Integer.toString(course.getCourseId()),oneCourseList);
//			System.out.println(course.getCourseId());
		}

		for (String courseId : header) {
			getMeetingsReady(courseId);
//			System.out.println(courseId);
		}

		return child;

	}


	private void prepareListData() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<Course>>();

		ArrayList<Course> courses = user.getCourses();
//		System.out.println("COURSES ARRAY LIST" + courses.toString());

		List<Course> oneCourseList = null;
		for (Course course : courses) {
			listDataHeader.add(Integer.toString(course.getCourseId()));
			oneCourseList = new ArrayList<Course>();
			oneCourseList.add(course);
			listDataChild.put(Integer.toString(course.getCourseId()),oneCourseList);
//			System.out.println(course.getCourseId());
		}

		for (String courseId : listDataHeader) {
			getMeetingsReady(courseId);
//			System.out.println(courseId);
		}

	}


	public void getMeetingsReady(String courseId) {

		Course course = null;
		List<Course> oneCourseList = null;
		int id = Integer.parseInt(courseId);
		try {
			

			ArrayList<Meeting> meetings = new ArrayList<Meeting>();
			ArrayList<Course> courses = user.getCourses();
			for(Course c : courses){
				if(c.getCourseId() == id){
//					meetings = c.getMeetings();
					ArrayList<Meeting> tempMeetings = user.getMeetings();
					for(Meeting m : tempMeetings)
					{
						if(m.getCourseId()==id)
						{
							meetings.add(m);
						}
					}
				}
			}
			
			
			System.out.println(meetings.toString());
			System.out.println("building code: " + meetings.get(0).getBuildingCode());
			//user's meetings array SHOULD NOT be grouped (is breaking validation code)
			meetings = groupMeetingsDays(new ArrayList<Meeting>(meetings));
			System.out.println("SET MEETINGS");
			course = listDataChild.get(Integer.toString(meetings.get(0).getCourseId())).get(0);
			System.out.println("SET MEETINGS2");
			course.setMeetings(meetings);
			System.out.println("SET MEETINGS3");

			oneCourseList = new ArrayList<Course>();
			oneCourseList.add(course);
			listDataChild.remove(course.getCourseId());
			listDataChild.put(Integer.toString(course.getCourseId()),oneCourseList);

		} catch (Exception e) {

			System.out.println("Exception:" + e.getMessage());
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
	}
}
