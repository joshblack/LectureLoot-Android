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

import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.ExpandableListView;
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
	private int courseId = -69; // used to setup Course Post with courseId


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		System.out.println("onCreateView enter");

		View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);
		//final AdapterView aView = (AdapterView)rootView;


		user = User.getInstance();


		TextView userDisplay = (TextView)rootView.findViewById(R.id.username);
		//TODO: Set Display name dynamically based on Singleton User Model
		//String name = user.getName();
		//System.out.println("HELLO" + user.getName());
		userDisplay.setText("Justin Rafanan's Schedule");
		userDisplay.setTypeface(null, Typeface.BOLD_ITALIC);
		userDisplay.setTextSize(25);

		//get the listview
		expListView = (ExpandableListView) rootView.findViewById(R.id.lvExp);
		// 

		//AsyncTask testing

		prepareListData();
		listAdapter = new ExpandableListCourseAdapter(getActivity(), listDataHeader, listDataChild);

		// setting list adapter
		expListView.setAdapter(listAdapter);

		/*
		-- Do this some other way - JOSH --
		String coursesUrl = "http://lectureloot.eu1.frbit.net/api/v1/users/1/courses";
		String authToken = "MJByIloBXVKpebWqqTqW9zGY0EUmAcyDDaiCzyyX";
		HttpGetCourses getter = new HttpGetCourses(authToken);
		getter.setHttpGetCoursesFinishedListener(this);
		getter.execute(new String[] {coursesUrl});
		 */


		Button addNewCourseButton;
		addNewCourseButton = (Button)rootView.findViewById(R.id.addButton);
		addNewCourseButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//			Toast.makeText(getActivity(), "Button Clicked", Toast.LENGTH_SHORT).show();

				final ArrayList<Course> allCoursesArray = user.getCourseList();
				//				System.out.println(allCoursesArray.toString());
				ArrayList<String> deptCodes = new ArrayList<String>();
				ArrayList<String> courseCodes = new ArrayList<String>();
				ArrayList<String> sectionNumbers = new ArrayList<String>();

				for(Course course : allCoursesArray){
					deptCodes.add(course.getCoursePrefix());
				}
				for(Course course : allCoursesArray){
					courseCodes.add(course.getCourseNum());
				}				
				for(Course course : allCoursesArray){
					sectionNumbers.add(course.getSectionNumber());
				}


				final Dialog dialog = new Dialog(getActivity());
				dialog.setContentView(R.layout.dialog_add_course);
				dialog.setTitle("Select Course");


				//AutoCompleteTextViews for Course Selection
				final AutoCompleteTextView deptCodeView;
				deptCodeView = (AutoCompleteTextView) dialog.findViewById(R.id.autoCompleteTextView1);
				ArrayAdapter<String> adapter0 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,deptCodes);
				deptCodeView.setAdapter(adapter0);

				//				deptCodeView.setOnItemClickListener(new OnItemClickListener() {
				//
				//					@Override
				//					public void onItemClick(AdapterView<?> arg0, View arg1,
				//							int arg2, long arg3) {
				//						System.out.println("HELLO HELP ME");
				//						System.out.println("Whats going on?");
				//						
				//						
				//					}
				//				});

				final AutoCompleteTextView courseCodeView;
				courseCodeView = (AutoCompleteTextView) dialog.findViewById(R.id.autoCompleteTextView2);
				ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,courseCodes);
				courseCodeView.setAdapter(adapter1);

				final AutoCompleteTextView sectionNumberView;
				sectionNumberView = (AutoCompleteTextView) dialog.findViewById(R.id.autoCompleteTextView3);
				ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,sectionNumbers);
				sectionNumberView.setAdapter(adapter2);

				//Spinners for Course Selection
				//				//String[] deptCodes = {"CEN","CIS","CAP","CEN","CIS","CAP","CEN","CIS","CAP","CEN","CIS","CAP","CEN","CIS","CAP"};
				//				Spinner deptCodeSpinner = (Spinner)dialog.findViewById(R.id.deptCodeSpinner);
				//				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,deptCodes); 
				//				deptCodeSpinner.setAdapter(adapter);
				//				//deptCodeSpinner.setOnItemSelectedListener(this);
				//	          
				//
				//				//String[] courseCodes = {"1234","2345","3456","1234","2345","3456","1234","2345","3456","1234","2345","3456","1234","2345","3456"};
				//				Spinner courseCodeSpinner = (Spinner)dialog.findViewById(R.id.courseCodeSpinner);
				//				ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,courseCodes); 
				//				courseCodeSpinner.setAdapter(adapter2);
				//
				//				//String[] sectionNumbers = {"12AB","5678","85H7","12AB","5678","85H7","12AB","5678","85H7"};
				//				Spinner sectionNumberSpinner = (Spinner)dialog.findViewById(R.id.sectionNumberSpinner);
				//				ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,sectionNumbers); 
				//				sectionNumberSpinner.setAdapter(adapter3);



				Button dialogButton = (Button) dialog.findViewById(R.id.dialogAddButton);
				dialogButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						String deptCodeStr = deptCodeView.getText().toString();
						//System.out.println(deptCodeStr + ".");
						String courseCodeStr = courseCodeView.getText().toString();
						//System.out.println(courseCodeStr + ".");
						String sectionNumberStr = sectionNumberView.getText().toString();
						//System.out.println(sectionNumberStr + ".");


						for(Course course : allCoursesArray){

//							if(course.getCoursePrefix().equals(deptCodeStr)){
//								System.out.println("equals");
//							}
//							else
//								System.out.println("fail");
//							if(course.getCourseNum().equals(courseCodeStr)){
//								System.out.println("equals");
//							}
//							else
//								System.out.println("fail");
//							if(course.getSectionNumber().equals(sectionNumberStr)){
//								System.out.println("equals");
//							}
//							else
//								System.out.println("fail");
//							System.out.println("Course Prefix"+course.getCoursePrefix() +".");
//							System.out.println("Course Num"+course.getCourseNum()+ ".");
//							System.out.println("Course Section"+course.getSectionNumber()+ ".");
							if(course.getCoursePrefix().equals(deptCodeStr) && course.getCourseNum().equals(courseCodeStr)
									&& course.getSectionNumber().equals(sectionNumberStr)){
								courseId = course.getCourseId();
								break;
							}
						}
						if(courseId != -69){
							//send server request
							String userId = user.getUserId();
							System.out.println(userId);
							String coursesUrl = "http://lectureloot.eu1.frbit.net//api/v1/users/" + userId + "/courses?course_id=" + courseId;
							System.out.println(coursesUrl);
							String authToken = user.getAuthToken();
							HttpPostCourses coursesPost = new HttpPostCourses(authToken);			         
							coursesPost.execute(new String[] {coursesUrl});
							
							//update locally
							int newCourseId = courseId;
							String newCoursePrefix = "defaultPrefix";
							String newCourseNum = "defaultCourseNum";
							String newCourseTitle = "defaultCourseTitle";
							String newSectionNum = "defaultSectionNum";
							String newCredits = "defaultCredits";
							String newInstructor = "defaultInstructor";
							ArrayList<Meeting> newMeetings = new ArrayList<Meeting>();
							
							for(Course course : allCoursesArray){
								if(newCourseId == course.getCourseId()){
									newCoursePrefix = course.getCoursePrefix();
									newCourseNum = course.getCourseNum();
									newCourseTitle = course.getCourseTitle();
									newSectionNum = course.getSectionNumber();
									newCredits = course.getCredits();
									newInstructor = course.getInstructor();
									newMeetings = course.getMeetings();
								}
							}

							ArrayList<Course> userCourses = user.getCourses();
							Course newCourse = new Course(newCourseId, newCoursePrefix, newCourseNum, newCourseTitle, newSectionNum, newCredits, newInstructor);
							
//										Meeting meeting = new Meeting();
//										meeting.setMeetingId(1);
//										meeting.setBuildingCode("LIT");
//										meeting.setRoomNumber("RoomNum");
//										meeting.setMeetingDay("M");
//										meeting.setPeriod("6");
//										meeting.setCourseId(1);
//										newMeetings.add(meeting);
							
							newCourse.setMeetings(newMeetings);
							userCourses.add(newCourse);
							
							user.setCourses(userCourses);
							
							//user.addCourseFromList(newCourse);
							
							Toast.makeText(getActivity(), "Course Added", Toast.LENGTH_LONG).show();
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

				//				String deptCodeStr = deptCodeView.getEditableText().toString();
				//				System.out.println("deptCodeString: " + deptCodeStr);





			}
		});

		return rootView;
	}

	private ArrayList<Meeting> groupMeetingsDays (ArrayList<Meeting> meetings) {
		ArrayList<Meeting> groupedMeetingsDays = new ArrayList<Meeting>();
		System.out.println("enter groupMeetingDays");
		Meeting comparedAgainstMeeting = null;
		Meeting testMeeting = null;
		Meeting compareTestMeeting = null;
		for(int i = 0; i < meetings.size(); i++) {
			comparedAgainstMeeting = meetings.get(i);
			compareTestMeeting = meetings.get(i);
			System.out.println(compareTestMeeting.toString());
			boolean flag = false;
			for(int j = i+1; j < meetings.size(); j++) {
				testMeeting = meetings.get(j);
				System.out.println("Test"+ testMeeting.toString());
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

	private void prepareListData() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<Course>>();

		ArrayList<Course> courses = user.getCourses();
		System.out.println("COURSES ARRAY LIST" + courses.toString());


		List<Course> oneCourseList = null;
		for (Course course : courses) {
			listDataHeader.add(Integer.toString(course.getCourseId()));
			oneCourseList = new ArrayList<Course>();
			oneCourseList.add(course);
			listDataChild.put(Integer.toString(course.getCourseId()),oneCourseList);
			System.out.println(course.getCourseId());
		}

		for (String courseId : listDataHeader) {
			getMeetingsReady(courseId);
			System.out.println(courseId);
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
					meetings = c.getMeetings();
				}
			}

			//System.out.println("building code: " +meetings.get(0).getBuildingCode());
			//			ArrayList<Meeting> meetings = new ArrayList<Meeting>();
			//			Meeting meeting = new Meeting();
			//			meeting.setMeetingId(1);
			//			meeting.setBuildingCode("LIT");
			//			meeting.setRoomNumber("RoomNum");
			//			meeting.setMeetingDay("M");
			//			meeting.setPeriod("6");
			//			meeting.setCourseId(1);
			//			
			//			meetings.add(meeting);	//add to arrayList
			//			
			//			Meeting meeting2 = new Meeting();
			//			meeting2.setMeetingId(2);
			//			meeting2.setBuildingCode("LIT");
			//			meeting2.setRoomNumber("RoomNum");
			//			meeting2.setMeetingDay("W");
			//			meeting2.setPeriod("6");
			//			meeting2.setCourseId(1);
			//			
			//			meetings.add(meeting2);	//add to arrayList

			System.out.println(meetings.toString());
			System.out.println("building code: " + meetings.get(0).getBuildingCode());
			meetings = groupMeetingsDays(meetings);
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
