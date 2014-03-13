package com.lectureloot.android.adapter;
/**
 * ExpandableListAdapter for Schedule Viewing
 * @author Austin Bruch, Justin Rafanan
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONTokener;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lectureloot.android.Course;
import com.lectureloot.android.R;
import com.lectureloot.background.HttpDeleteCourses;
import com.lectureloot.background.HttpGetCourses;
import com.lectureloot.background.HttpGetMeetings;

public class ExpandableListCourseAdapter extends BaseExpandableListAdapter {

	private Context _context;
	private List<String> _listDataHeader; // header titles
	// child data in format of header title, child title
	private HashMap<String, List<Course>> _listDataChild;

	public ExpandableListCourseAdapter(Context context, List<String> listDataHeader,HashMap<String, List<Course>> listChildData) {
		this._context = context;
		this._listDataHeader = listDataHeader;
		this._listDataChild = listChildData;
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this._listDataChild.get(this._listDataHeader.get(groupPosition))
				.get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

		final int courseId = ((Course)getChild(groupPosition,childPosition)).getCourseId();


		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.course_list_item, null);
		}

		TextView courseTitle = (TextView) convertView.findViewById(R.id.courseTitle);
		TextView instructor = (TextView) convertView.findViewById(R.id.instructor);
		TextView meetingDays1 = (TextView) convertView.findViewById(R.id.meetingDays1);
		TextView meetingDays2 = (TextView) convertView.findViewById(R.id.meetingDays2);
		TextView meetingDays3 = (TextView) convertView.findViewById(R.id.meetingDays3);
		// No longer needed as Period information is displayed within the Meeting Day TextViews
		//    		TextView period1 = (TextView) convertView.findViewById(R.id.period1);
		//    		TextView period2 = (TextView) convertView.findViewById(R.id.period2);
		//    		TextView period3 = (TextView) convertView.findViewById(R.id.period3);
		TextView room1 = (TextView) convertView.findViewById(R.id.room1);
		TextView room2 = (TextView) convertView.findViewById(R.id.room2);
		TextView room3 = (TextView) convertView.findViewById(R.id.room3);


		final String courseText = (String) ((Course)getChild(groupPosition,childPosition)).getCourseTitle();
		final String instructorText = (String) ((Course)getChild(groupPosition,childPosition)).getInstructor();
		final String meeting1Text = (String) ((Course)getChild(groupPosition,childPosition)).getMeetings().get(0).getMeetingDay().toUpperCase();
		final String meeting2Text = (((Course)getChild(groupPosition,childPosition)).getMeetings().size() > 1 ) ? (String) ((Course)getChild(groupPosition,childPosition)).getMeetings().get(1).getMeetingDay().toUpperCase() : null;
		final String meeting3Text = (((Course)getChild(groupPosition,childPosition)).getMeetings().size() > 2 ) ?(String) ((Course)getChild(groupPosition,childPosition)).getMeetings().get(2).getMeetingDay().toUpperCase() : null;
		final String period1Text = (String) ((Course)getChild(groupPosition,childPosition)).getMeetings().get(0).getPeriod();
		final String period2Text = (((Course)getChild(groupPosition,childPosition)).getMeetings().size() > 1 ) ? (String) ((Course)getChild(groupPosition,childPosition)).getMeetings().get(1).getPeriod() : null;
		final String period3Text = (((Course)getChild(groupPosition,childPosition)).getMeetings().size() > 2 ) ? (String) ((Course)getChild(groupPosition,childPosition)).getMeetings().get(2).getPeriod() : null;
		final String room1Text = (String) ((Course)getChild(groupPosition,childPosition)).getMeetings().get(0).getRoomNumber();
		final String room2Text = (((Course)getChild(groupPosition,childPosition)).getMeetings().size() > 1 ) ? (String) ((Course)getChild(groupPosition,childPosition)).getMeetings().get(1).getRoomNumber() : null;
		final String room3Text = (((Course)getChild(groupPosition,childPosition)).getMeetings().size() > 2 ) ? (String) ((Course)getChild(groupPosition,childPosition)).getMeetings().get(2).getRoomNumber() : null;
		final String buildingCode1 = (String) ((Course)getChild(groupPosition,childPosition)).getMeetings().get(0).getBuildingCode().toUpperCase();
		final String buildingCode2 = (((Course)getChild(groupPosition,childPosition)).getMeetings().size() > 1 ) ? (String) ((Course)getChild(groupPosition,childPosition)).getMeetings().get(1).getBuildingCode().toUpperCase() : null;
		final String buildingCode3 = (((Course)getChild(groupPosition,childPosition)).getMeetings().size() > 2 ) ? (String) ((Course)getChild(groupPosition,childPosition)).getMeetings().get(2).getBuildingCode().toUpperCase() : null;
		//		final String meeting1Text = (String) ((Course)getChild(groupPosition,childPosition)).getMeetingDays1();
		//		final String meeting2Text = (String) ((Course)getChild(groupPosition,childPosition)).getMeetingDays2();
		//		final String meeting3Text = (String) ((Course)getChild(groupPosition,childPosition)).getMeetingDays3();
		//		final String period1Text = (String) ((Course)getChild(groupPosition,childPosition)).getPeriod1();
		//		final String period2Text = (String) ((Course)getChild(groupPosition,childPosition)).getPeriod2();
		//		final String period3Text = (String) ((Course)getChild(groupPosition,childPosition)).getPeriod3();
		//		final String room1Text = (String) ((Course)getChild(groupPosition,childPosition)).getRoom1();
		//		final String room2Text = (String) ((Course)getChild(groupPosition,childPosition)).getRoom2();
		//		final String room3Text = (String) ((Course)getChild(groupPosition,childPosition)).getRoom3();
		final String courseCode = (String) ((Course)getChild(groupPosition,childPosition)).getCourseCode().toUpperCase();

		courseTitle.setText(courseText);
		instructor.setText("Instructor: " + instructorText);
		meetingDays1.setText(meeting1Text + " " + period1Text);
		if(meeting2Text != null && period2Text != null) {
			meetingDays2.setText(meeting2Text + " " + period2Text);
		} else {
			meetingDays2.setText(meeting2Text);
		}
		if(meeting3Text != null && period3Text != null) {
			meetingDays3.setText(meeting3Text + " " + period3Text);
		} else {
			meetingDays3.setText(meeting3Text);
		}
		// No longer needed as period is displayed within Meetings TextViews
		//    		period1.setText(period1Text);
		//    		period2.setText(period2Text);
		//    		period3.setText(period3Text);

		room1.setText(buildingCode1 + " " + room1Text);
		if (buildingCode2 != null && room2Text != null) {
			room2.setText(buildingCode2 + " " + room2Text);
		} else {
			room2.setText("");
		}
		if (buildingCode3 != null && room3Text != null) {
			room3.setText(buildingCode3 + " " + room3Text);
		} else {
			room3.setText("");
		}


		//http://campusmap.ufl.edu/?sched=EEL4712C,MWF,2,MAEA,303,R,9-11,NEB,281;CEN3031,MWF,6,LIT,109,W,7,CSE,E116;MAS4203,MWF,4,LIT,217;EEL3135,TR,7-8,NEB,202;
		Button mapButton;
		mapButton = (Button)convertView.findViewById(R.id.mapButton);
		mapButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String scheduleMapUri = "http://campusmap.ufl.edu/?sched=";


				scheduleMapUri+=courseCode.trim()+",";
				scheduleMapUri+=meeting1Text.trim()+",";
				scheduleMapUri+=period1Text.trim()+",";
				scheduleMapUri+=buildingCode1.trim()+",";
				scheduleMapUri+=room1Text.trim()+",";
				boolean meet2 = (meeting2Text != null) ? true : false;
				scheduleMapUri+=(meet2) ? meeting2Text.trim()+"," : "";
				scheduleMapUri+=(meet2) ? period2Text.trim()+"," : "";
				scheduleMapUri+=(meet2) ? buildingCode2.trim()+"," : "";
				scheduleMapUri+=(meet2) ? room2Text.trim()+"," : "";
				boolean meet3 = (meeting3Text != null) ? true : false;
				scheduleMapUri+=(meet3) ? meeting3Text.trim()+"," : "";
				scheduleMapUri+=(meet3) ? period3Text.trim()+"," : "";
				scheduleMapUri+=(meet3) ? buildingCode3.trim()+"," : "";
				scheduleMapUri+=(meet3) ? room3Text.trim()+"," : "";
				scheduleMapUri = scheduleMapUri.substring(0, scheduleMapUri.length()-1);
				scheduleMapUri+=";";
				Uri uri = Uri.parse(scheduleMapUri);
				Intent campusMap = new Intent(android.content.Intent.ACTION_VIEW, uri);
				//campusMap.setData(Uri.parse("campusmap.ufl.edu"));
				v.getContext().startActivity(campusMap);

			}
		});

		Button dropCourseButton;
		dropCourseButton = (Button)convertView.findViewById(R.id.dropButton);
		dropCourseButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("Drop Course Button Clicked");

				final Dialog dialog = new Dialog(_context);
				dialog.setContentView(R.layout.dialog_drop_course);
				dialog.setTitle("Drop Course?");

				Button confirmButton = (Button) dialog.findViewById(R.id.dialogDropConfirmButton);
				confirmButton.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						String coursesUrl = "http://lectureloot.eu1.frbit.net/api/v1/users/1/courses/"+courseId;
						String authToken = "MJByIloBXVKpebWqqTqW9zGY0EUmAcyDDaiCzyyX";
						HttpDeleteCourses getter = new HttpDeleteCourses(authToken);
						//	getter.setHttpDeleteCoursesFinishedListener(this);
						getter.execute(new String[] {coursesUrl});

						Toast.makeText(_context, "Course Dropped", Toast.LENGTH_LONG).show();
						//						mNeedsToCheckIn.setVisibility(View.GONE);

						dialog.dismiss();

					}
				});

				Button denyButton = (Button) dialog.findViewById(R.id.dialogDropDenyButton);
				denyButton.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Toast.makeText(_context, "Course Not Dropped", Toast.LENGTH_LONG).show();
						dialog.dismiss();

					}
				});

				dialog.show();
			}
		});


		return convertView;
	}

	//	private void disableChild

	@Override
	public int getChildrenCount(int groupPosition) {
		return this._listDataChild.get(this._listDataHeader.get(groupPosition))
				.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this._listDataHeader.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this._listDataHeader.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		String courseText = (String) ((Course)getChild(groupPosition, 0)).getCourseCode();//getGroup(groupPosition);
		String sectionText = (String) ((Course)getChild(groupPosition, 0)).getSectionNumber();
		String creditsText = (String) ((Course)getChild(groupPosition, 0)).getCredits();

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.course_list_group, null);
		}

		TextView courseCode = (TextView) convertView.findViewById(R.id.courseCode);

		TextView sectionNumber = (TextView) convertView.findViewById(R.id.sectionNumber);
		TextView credits = (TextView) convertView.findViewById(R.id.credits);

		courseCode.setTypeface(null, Typeface.BOLD);
		courseCode.setText(courseText);

		if (sectionText != null) {
			sectionNumber.setText("Section " + sectionText);
		} else {
			sectionNumber.setText(sectionText);
		}

		if (creditsText != null) {
			credits.setText("Credits: " + creditsText);
		} else {
			credits.setText(creditsText);
		}


		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}


}