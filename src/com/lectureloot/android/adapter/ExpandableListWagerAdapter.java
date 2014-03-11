package com.lectureloot.android.adapter;

import java.util.HashMap;
import java.util.List;


import com.lectureloot.android.R;
import com.lectureloot.android.R.id;
import com.lectureloot.android.R.layout;
import com.lectureloot.android.Wager;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ExpandableListWagerAdapter extends BaseExpandableListAdapter {

	private Context _context;
	private List<String> _listDataHeader; // header titles
	// child data in format of header title, child title
	private HashMap<String, List<Wager>> _listDataChild;

	public ExpandableListWagerAdapter(Context context, List<String> wagerListDataHeader, HashMap<String, List<Wager>> wagerListChildData) {
		this._context = context;
		this._listDataHeader = wagerListDataHeader;
		this._listDataChild = wagerListChildData;
	}

	public Object getChild(int groupPosition, int childPosititon) {
		return this._listDataChild.get(this._listDataHeader.get(groupPosition))
				.get(childPosititon);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {


		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.wager_list_items, null);
		}

		TextView wagerSessionCode = (TextView) convertView.findViewById(R.id.wagerSessionCode);
		TextView wagerPerMeeting = (TextView) convertView.findViewById(R.id.wagerPerMeeting);
		TextView totalMeetings = (TextView) convertView.findViewById(R.id.totalMeetings);
		TextView totalWager = (TextView) convertView.findViewById(R.id.totalWager);

		final int wagerSessionCodeText = (int) ((Wager)getChild(groupPosition,childPosition)).getWagerSessionCode();
		final int wagerPerMeetingText = (int) ((Wager)getChild(groupPosition,childPosition)).getWagerPerMeeting();
		final int totalMeetingsText = (int) ((Wager)getChild(groupPosition,childPosition)).getTotalMeetings();
		final int totalWagerText = (int) ((Wager)getChild(groupPosition,childPosition)).getTotalWager();

		wagerSessionCode.setText(wagerSessionCodeText); // wagerSessionCode --- wagerSessionCodeText
		wagerPerMeeting.setText("Wager Per Meeting: " + wagerPerMeetingText);
		totalMeetings.setText("Total Meetings: "+ totalMeetingsText);
		totalWager.setText("Total Wager: "+ totalWagerText);

		return convertView;
	}


	public int getChildrenCount(int groupPosition) {
		return this._listDataChild.get(this._listDataHeader.get(groupPosition))
				.size();
	}

	public Object getGroup(int groupPosition) {
		return this._listDataHeader.get(groupPosition);
	}


	public int getGroupCount() {
		return this._listDataHeader.size();
	}


	public long getGroupId(int groupPosition) {
		return groupPosition;
	}


	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		int wagerSessionCodeText = (int)((Wager)getChild(groupPosition, 0)).getWagerSessionCode();
		String wagerSessionCodeString = String.valueOf(wagerSessionCodeText);
		
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.wager_list_group, null);
		}

		TextView wagerSessionCode = (TextView) convertView.findViewById(R.id.wagerSessionCode);

		wagerSessionCode.setTypeface(null, Typeface.BOLD);
		wagerSessionCode.setText(wagerSessionCodeString);
			
		return convertView;
	}


	public boolean hasStableIds() {
		return false;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
