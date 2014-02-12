package com.lectureloot.android;
/**
 * ExpandableListAdapter for Schedule Viewing
 * @author Austin Bruch, Justin Rafanan
 */

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Context _context;
	private List<String> _listDataHeader; // header titles
	// child data in format of header title, child title
	private HashMap<String, List<Course>> _listDataChild;

	public ExpandableListAdapter(Context context, List<String> listDataHeader,HashMap<String, List<Course>> listChildData) {
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

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_item, null);
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
		final String meeting1Text = (String) ((Course)getChild(groupPosition,childPosition)).getMeetingDays1();
		final String meeting2Text = (String) ((Course)getChild(groupPosition,childPosition)).getMeetingDays2();
		final String meeting3Text = (String) ((Course)getChild(groupPosition,childPosition)).getMeetingDays3();
		final String period1Text = (String) ((Course)getChild(groupPosition,childPosition)).getPeriod1();
		final String period2Text = (String) ((Course)getChild(groupPosition,childPosition)).getPeriod2();
		final String period3Text = (String) ((Course)getChild(groupPosition,childPosition)).getPeriod3();
		final String room1Text = (String) ((Course)getChild(groupPosition,childPosition)).getRoom1();
		final String room2Text = (String) ((Course)getChild(groupPosition,childPosition)).getRoom2();
		final String room3Text = (String) ((Course)getChild(groupPosition,childPosition)).getRoom3();

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
		room1.setText(room1Text);
		room2.setText(room2Text);
		room3.setText(room3Text);
		return convertView;
	}

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
		String courseText = (String) getGroup(groupPosition);
		String sectionText = (String) ((Course)getChild(groupPosition, 0)).getSectionNumber();
		String creditsText = (String) ((Course)getChild(groupPosition, 0)).getCredits();

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_group, null);
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