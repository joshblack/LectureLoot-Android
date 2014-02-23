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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lectureloot.android.adapter.ExpandableListCourseAdapter;
import com.lectureloot.background.HttpGetCourses;
import com.lectureloot.background.HttpGetMeetings;


public class ScheduleFragment extends Fragment implements HttpGetFinishedListener{

	private ExpandableListCourseAdapter listAdapter;
	private ExpandableListView expListView;
	private List<String> listDataHeader = null;
	private HashMap<String, List<Course>> listDataChild = null;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		System.out.println("onCreateView enter");

		View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);

		TextView userDisplay = (TextView)rootView.findViewById(R.id.username);
		//TODO: Set Display name dynamically based on Singleton User Model
		userDisplay.setText("Justin Rafanan's Schedule");
		userDisplay.setTypeface(null, Typeface.BOLD_ITALIC);
		userDisplay.setTextSize(25);

		//get the listview
		expListView = (ExpandableListView) rootView.findViewById(R.id.lvExp);
		// 

		//AsyncTask testing

		//		prepareListData();
		String coursesUrl = "http://lectureloot.eu1.frbit.net/api/v1/users/1/courses";
		HttpGetCourses getter = new HttpGetCourses();
		getter.setHttpGetFinishedListener(this);
		getter.execute(new String[] {coursesUrl});



		Button addNewCourseButton;
		addNewCourseButton = (Button)rootView.findViewById(R.id.addButton);
		addNewCourseButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//			Toast.makeText(getActivity(), "Button Clicked", Toast.LENGTH_SHORT).show();

				final Dialog dialog = new Dialog(getActivity());
				dialog.setContentView(R.layout.dialog_add_course);
				dialog.setTitle("Select Course");

				Button dialogButton = (Button) dialog.findViewById(R.id.dialogAddButton);
				dialogButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Toast.makeText(getActivity(), "Error: Course Not Added", Toast.LENGTH_LONG).show();
						dialog.dismiss();

					}
				});

				dialog.show();

				//TODO: Stubbed out data for the course selection spinners
				String[] deptCodes = {"CEN","CIS","CAP","CEN","CIS","CAP","CEN","CIS","CAP","CEN","CIS","CAP","CEN","CIS","CAP"};
				Spinner deptCodeSpinner = (Spinner)dialog.findViewById(R.id.deptCodeSpinner);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,deptCodes); 
				deptCodeSpinner.setAdapter(adapter);

				String[] courseCodes = {"1234","2345","3456","1234","2345","3456","1234","2345","3456","1234","2345","3456","1234","2345","3456"};
				Spinner courseCodeSpinner = (Spinner)dialog.findViewById(R.id.courseCodeSpinner);
				ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,courseCodes); 
				courseCodeSpinner.setAdapter(adapter2);

				String[] sectionNumbers = {"12AB","5678","85H7","12AB","5678","85H7","12AB","5678","85H7"};
				Spinner sectionNumberSpinner = (Spinner)dialog.findViewById(R.id.sectionNumberSpinner);
				ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,sectionNumbers); 
				sectionNumberSpinner.setAdapter(adapter3);
			}
		});

		return rootView;
	}

	@Override
	public void onHttpGetCoursesReady(String output) {
		System.out.println("onHttpGetCoursesReady enter");

		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<Course>>();

		JSONTokener tokener = new JSONTokener(output);
		JSONArray array = null;
		System.out.println("onHttpGetCoursesReady 1");

		try {
			array = (JSONArray) tokener.nextValue();
			//			System.out.println(array.toString());
			System.out.println("onHttpGetCoursesReady 2");

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		System.out.println("onHttpGetCoursesReady 3");

		ArrayList<Course> courses = jsonArrayToCourses(array);
		System.out.println("onHttpGetCoursesReady 4");

		List<Course> oneCourseList = null;
		for (Course course : courses) {
			listDataHeader.add(Integer.toString(course.getCourseId()));
			oneCourseList = new ArrayList<Course>();
			oneCourseList.add(course);
			listDataChild.put(Integer.toString(course.getCourseId()),oneCourseList);


			//			System.out.println("onHttpGetCoursesReady 5");
			//			System.out.println("course:" + course.toString());

		}

		for (String courseId : listDataHeader) {
			HttpGetMeetings meetingsGetter = new HttpGetMeetings();
			String meetingsUrl = "http://lectureloot.eu1.frbit.net/api/v1/courses/" + courseId + "/meetings";
			System.out.println(meetingsUrl);
			meetingsGetter.setHttpGetFinishedListener(this);
			meetingsGetter.execute(new String[] {meetingsUrl});
		}

		listAdapter = new ExpandableListCourseAdapter(getActivity(), listDataHeader, listDataChild);
		// setting list adapter
		expListView.setAdapter(listAdapter);
		System.out.println("onHttpGetCoursesReady exit");
	}


	@Override
	public void onHttpGetMeetingsReady(String output) {
		System.out.println("onHttpGetMeetingsReady enter");

		JSONTokener tokener = new JSONTokener(output);
		JSONArray array = null;
		System.out.println("onHttpGetMeetingsReady 1");
		Course course = null;
		List<Course> oneCourseList = null;
		try {
			array = (JSONArray) tokener.nextValue();
			System.out.println("array printstring" + array.toString());
			System.out.println("onHttpGetMeetingsReady 2");

			System.out.println("onHttpGetMeetingsReady 3");

			ArrayList<Meeting> meetings = jsonArrayToMeetings(array);
			meetings = groupMeetingsDays(meetings);
			System.out.println("onHttpGetMeetingsReady 4");
			course = listDataChild.get(Integer.toString(meetings.get(0).getCourseId())).get(0);
			course.setMeetings(meetings);

			oneCourseList = new ArrayList<Course>();
			oneCourseList.add(course);
			listDataChild.remove(course.getCourseId());
			listDataChild.put(Integer.toString(course.getCourseId()),oneCourseList);

		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
		}
	}

	private ArrayList<Meeting> groupMeetingsDays (ArrayList<Meeting> meetings) {
		ArrayList<Meeting> groupedMeetingsDays = new ArrayList<Meeting>();

		Meeting comparedAgainstMeeting = null;
		Meeting testMeeting = null;
		Meeting compareTestMeeting = null;
		for(int i = 0; i < meetings.size(); i++) {
			comparedAgainstMeeting = meetings.get(i);
			compareTestMeeting = meetings.get(i);
			boolean flag = false;
			for(int j = i+1; j < meetings.size(); j++) {
				testMeeting = meetings.get(j);
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

	////	private void prepareListData() {
	//		listDataHeader = new ArrayList<String>();
	//		listDataChild = new HashMap<String, List<Course>>();
	//
	//		Course cen3031 = new Course();
	//		cen3031.setCourseCode("CEN3031");
	//		cen3031.setCourseTitle("INTRO SOFTWARE ENGR");
	//		cen3031.setSectionNumber(Integer.toString(5842));
	//		cen3031.setCredits(Integer.toString(3));
	//		cen3031.setInstructor("Bermudez, Manuel E");
	//		cen3031.setMeetingDays1("MWF");
	//		cen3031.setPeriod1(Integer.toString(6));
	//		cen3031.setRoom1("LIT 109");
	//		cen3031.setMeetingDays2("W");
	//		cen3031.setPeriod2(Integer.toString(7));
	//		cen3031.setRoom2("CSE E116");
	//		cen3031.setMeetingDays3(null);
	//		cen3031.setPeriod3(null);
	//		cen3031.setRoom3(null);
	//
	//		List<Course> cen3031List = new ArrayList<Course>();
	//		cen3031List.add(cen3031);
	//
	//		// Adding child data for CAP4053
	//		Course cap4053 = new Course("CAP4053", "AI FOR COMPUTER GAMES","133E","3","Anthony,Lisa, Dankel,Douglas D,II","MWF",null,null,"5", null, null, "CSE E119", null, null);
	//		List<Course> cap4053List = new ArrayList<Course>();
	//		cap4053List.add(cap4053);
	//
	//		// Adding child data for EGN4641
	//		Course egn4641 = new Course("EGN4641", "ENG ENTREPRENEURSHIP","11AF","3","Sander, Erik J","R",null,null,"3-5", null, null, "NEB 0102", null, null);
	//		List<Course> egn4641List = new ArrayList<Course>();
	//		egn4641List.add(egn4641);
	//
	//		Course newCourse = new Course();
	//		newCourse.setCourseCode("Add a Course");
	//		List<Course> newCourseList = new ArrayList<Course>();
	//		newCourseList.add(newCourse);
	//
	//		// Adding child data 
	//		listDataHeader.add(cen3031.getCourseCode());
	//		listDataHeader.add(cap4053.getCourseCode());
	//		listDataHeader.add(egn4641.getCourseCode());
	//		//        listDataHeader.add(newCourse.getCourseCode());
	//
	//
	//		listDataChild.put(listDataHeader.get(0), cen3031List); // Header, Child data
	//		listDataChild.put(listDataHeader.get(1), cap4053List);
	//		listDataChild.put(listDataHeader.get(2), egn4641List);
	//		//        listDataChild.put(listDataHeader.get(3), newCourseList);
	//
	//	}

	private Course jsonObjectToCourse(JSONObject jsonCourse) {

		Course course = new Course();
		try{
			course.setCourseId((Integer)jsonCourse.get("id"));

			String deptCode = (String)jsonCourse.getString("deptCode");
			String courseNumber = (String)jsonCourse.getString("courseNumber");
			String courseCode = deptCode + courseNumber;
			course.setCourseCode(courseCode);

			course.setSectionNumber((String)jsonCourse.getString("sectionNumber"));
			course.setCredits((String)jsonCourse.getString("credits"));
			course.setInstructor((String)jsonCourse.getString("instructor"));
			course.setCourseTitle((String)jsonCourse.getString("courseTitle"));
			//			course.setMeetingDays1("");
			//			course.setMeetingDays2("");
			//			course.setMeetingDays3("");
			//			course.setPeriod1("");
			//			course.setPeriod2("");
			//			course.setPeriod3("");
			//			course.setRoom1("");
			//			course.setRoom2("");
			//			course.setRoom3("");


		} catch (Exception e){

		}
		return course;
	}

	private ArrayList<Course> jsonArrayToCourses(JSONArray jsonCourses) {
		ArrayList<Course>  courses = new ArrayList<Course>();

		try {
			JSONObject jsonCourse;
			Course course;
			for(int i = 0; i < jsonCourses.length(); i++) {
				jsonCourse = jsonCourses.getJSONObject(i);
				course = jsonObjectToCourse(jsonCourse);
				courses.add(course);
			}
		} catch (Exception e) {

		}

		return courses;
	}


	private Meeting jsonObjectToMeeting(JSONObject jsonMeeting) {

		Meeting meeting = new Meeting();
		try{
			meeting.setMeetingId((Integer)jsonMeeting.get("id"));
			meeting.setCourseId(Integer.parseInt((String)jsonMeeting.get("course_id")));
			meeting.setBuildingCode((String)jsonMeeting.get("buildingCode"));
			meeting.setRoomNumber(((String)jsonMeeting.get("roomNumber")));
			meeting.setMeetingDay(((String)jsonMeeting.get("meetingDay")));
			meeting.setPeriod(((String)jsonMeeting.get("period")));
		} catch (Exception e){

		}
		return meeting;
	}

	private ArrayList<Meeting> jsonArrayToMeetings(JSONArray jsonMeetings) {
		ArrayList<Meeting>  meetings = new ArrayList<Meeting>();

		try {
			JSONObject jsonMeeting;
			Meeting meeting;
			for(int i = 0; i < jsonMeetings.length(); i++) {
				jsonMeeting = jsonMeetings.getJSONObject(i);
				meeting = jsonObjectToMeeting(jsonMeeting);
				meetings.add(meeting);
			}
		} catch (Exception e) {

		}

		return meetings;
	}
}
