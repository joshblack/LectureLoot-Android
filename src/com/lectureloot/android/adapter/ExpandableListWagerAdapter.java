package com.lectureloot.android.adapter;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


import com.lectureloot.android.Course;
import com.lectureloot.android.Meeting;
import com.lectureloot.android.R;
import com.lectureloot.android.ScheduleFragment;
import com.lectureloot.android.R.id;
import com.lectureloot.android.R.layout;
import com.lectureloot.android.User;
import com.lectureloot.android.Wager;
import com.lectureloot.android.WagerFragment;
import com.lectureloot.background.HttpDeleteCourses;
import com.lectureloot.background.HttpDeleteWagers;
import com.lectureloot.background.HttpPutWagers;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ExpandableListWagerAdapter extends BaseExpandableListAdapter {

	private Context _context;
	private List<String> _listDataHeader; // header titles
	// child data in format of header title, child title
	private HashMap<String, List<Wager>> _listDataChild;
	private User user;
	
	private int tempPerClassWager;
	private String displayTempPerClassWager;
	private TextView DisplayCurrentWager;


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

	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		user = User.getInstance();
		
		
		final int wagerId = ((Wager)getChild(groupPosition,childPosition)).getWagerId();	 // getting wagerId for currect wager
		
		final int wagerSessionId = ((Wager)getChild(groupPosition,childPosition)).getWagerSessionCode();	// get sessionId for wager
		ArrayList<Meeting> meetings = user.getMeetings();  									 // getting Meetings arraylist
		final int wagerMeetings = meetings.size();						// finding the size of arrayList to get total Meetings	
		
		((Wager)getChild(groupPosition, childPosition)).setTotalMeetings(wagerMeetings); 	 // setting total Meetings
		final int wagerUnitValue =((Wager)getChild(groupPosition, childPosition)).getWagerPerMeeting();	//getting Meetings from Wagers
		final int wagerPointsLost =((Wager)getChild(groupPosition, childPosition)).getCurrentWagerLost();	//getting points lost from Wagers

		final int newTotalWager = wagerMeetings*wagerUnitValue;								// Getting new TotalWager
		
		((Wager)getChild(groupPosition, childPosition)).setTotalWager(newTotalWager);		// setting new Total Wager
		
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.wager_list_items, null);
		}

		TextView wagerPerMeeting = (TextView) convertView.findViewById(R.id.wagerPerMeeting);
		TextView totalWager = (TextView) convertView.findViewById(R.id.totalWager);
		TextView LostWager = (TextView) convertView.findViewById(R.id.WagerLost);
		
		final String wagerPerMeetingText = String.valueOf(((Wager)getChild(groupPosition,childPosition)).getWagerPerMeeting());
		final String totalWagerText = String.valueOf(((Wager)getChild(groupPosition,childPosition)).getTotalWager());
		final String LostWagerText = String.valueOf(((Wager)getChild(groupPosition, childPosition)).getCurrentWagerLost());
		
		wagerPerMeeting.setText("  " + wagerPerMeetingText);
		totalWager.setText("  " + totalWagerText);
		LostWager.setText("  " + LostWagerText);
		
		wagerPerMeeting.setTextColor(Color.parseColor("#FFFFFF"));
		totalWager.setTextColor(Color.parseColor("#FFFFFF"));
		LostWager.setTextColor(Color.parseColor("#FFFFFF"));
		
/******************************
 *    Edit Wager Button       *
 ******************************/		
		
		Button editWagerButton;
		editWagerButton = (Button)convertView.findViewById(R.id.editWagerButton);
		editWagerButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				final Dialog dialog = new Dialog(_context);
				dialog.setContentView(R.layout.dialog_edit_wager);
				dialog.setTitle("Edit a Wager");
				tempPerClassWager = 10;	

				Button dialogDecrementButton = (Button) dialog.findViewById(R.id.decrementEditPerMeetingWager);
				dialogDecrementButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if(tempPerClassWager > 1) // make a decision on 1 or 0 as the minimum value of a Per class wager
						{
							tempPerClassWager--;
						}


						displayTempPerClassWager = String.valueOf(tempPerClassWager);


						DisplayCurrentWager =(TextView)dialog.findViewById(R.id.DisplayCurrentWagerPerClass);
						DisplayCurrentWager.setText(displayTempPerClassWager);
						// changes the original Per Class Wager value to the updated incremented value
					}
				});

				Button dialogIncrementButton = (Button) dialog.findViewById(R.id.incrementEditPerMeetingWager);
				dialogIncrementButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {


						if(tempPerClassWager< 20) // might have the valid capped differently for easy demo
						{
							tempPerClassWager++;
						}
						displayTempPerClassWager = String.valueOf(tempPerClassWager);

						DisplayCurrentWager =(TextView)dialog.findViewById(R.id.DisplayCurrentWagerPerClass);
						DisplayCurrentWager.setText(displayTempPerClassWager);
						// changes the original Per Class Wager value to the updated incremented value


					}
				});
/************************************
 *       Confirm Edit Button  	    *			     
 ************************************/
				Button dialogEditWagerButton = (Button) dialog.findViewById(R.id.dialogEditWagerButton);
				dialogEditWagerButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						
						
						((Wager)getChild(groupPosition,childPosition)).setWagerPerMeeting(tempPerClassWager);

						ArrayList<Meeting> tempMeetings = user.getMeetings();
						int size = tempMeetings.size();
						int nextTotal = size*tempPerClassWager;

						((Wager)getChild(groupPosition,childPosition)).setTotalMeetings(nextTotal);
						
						String userId = user.getUserId();
						
						System.out.println(userId);
						System.out.println(wagerId);
						System.out.println(wagerSessionId);
						System.out.println("new wager value:"+ tempPerClassWager);
						System.out.println("next total:"+ nextTotal);
						System.out.println(wagerPointsLost);
						
						
						String wagersUrl = "http://lectureloot.eu1.frbit.net/api/v1/users/"+userId+"/wagers/"+wagerId +"/edit?session_id="
								+ wagerSessionId +"&wagerUnitValue="+tempPerClassWager+"&wagerTotalValue="
								+nextTotal+"&pointsLost="+wagerPointsLost;
						
						String authToken = user.getAuthToken();
						HttpPutWagers wagerPut = new HttpPutWagers(authToken);
						wagerPut.execute(new String[] {wagersUrl});
						
						
						WagerFragment frg = new WagerFragment();
						_listDataHeader = frg.prepareDataHeader();
						_listDataChild = frg.prepareDataChild();
						notifyDataSetChanged();
						
						
						Toast.makeText(_context, "Edits have been made", Toast.LENGTH_SHORT).show();
						dialog.dismiss();
					}
				});

/********************************
 *    Confirm Delete Button     *
 ********************************/				
				
				Button dialogDeleteButton = (Button) dialog.findViewById(R.id.DeleteWagerButton);
				dialogDeleteButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						
						String userId = user.getUserId();
						String wagersUrl = "http://lectureloot.eu1.frbit.net/api/v1/users/" + userId + "/wagers/" + wagerId;
						String authToken = user.getAuthToken();
						HttpDeleteWagers wagerDelete = new HttpDeleteWagers(authToken);
						wagerDelete.execute(new String[] {wagersUrl});
						
						System.out.println("Here");
						ArrayList<Wager> wagers = user.getWagers();
						ArrayList<Wager> newWagers = new  ArrayList<Wager>();

						for(int i=0;i<wagers.size();i++)
						{
							if(wagerSessionId != wagers.get(i).getWagerSessionCode())
							{
								newWagers.add(wagers.get(i));
							}
						}
						
						user.setWagers(newWagers);
						//delete from view
						WagerFragment frg = new WagerFragment();
						_listDataHeader = frg.prepareDataHeader();
						_listDataChild = frg.prepareDataChild();
						notifyDataSetChanged();
						
						Toast.makeText(_context, "Wager has been deleted", Toast.LENGTH_SHORT).show();
						dialog.dismiss();
					}
				});

				dialog.show();       
			}
		});
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
		
		user = User.getInstance();

/*********************************************************
 * Formatting yyyy-MM-dd dates into MMM dd,yyyy dates    *
 * e.g. 2014-02-14 turns to Feb 14, 2014                 *
 *********************************************************/		
		
		Format dateFormatter;
		int wagerSessionCodeText = (int)((Wager)getChild(groupPosition, 0)).getWagerSessionCode();
		Date sessionStartDate = user.getSessions().get(wagerSessionCodeText-1).getStartDate();
		Date sessionEndDate = user.getSessions().get(wagerSessionCodeText-1).getEndDate();
		dateFormatter = new SimpleDateFormat("MMM dd, yyyy");
		String startOfWeek = dateFormatter.format(sessionStartDate);
		String endOfWeek = dateFormatter.format(sessionEndDate);
		String weekOf = startOfWeek + " - " + endOfWeek;
		String wagerSessionCodeString = String.valueOf(weekOf);
		
		
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
	
	public void reloadItems(ArrayList<String> header, HashMap<String, List<Wager>> child)
	{
		this._listDataHeader = header;
		this._listDataChild = child;
		notifyDataSetChanged();
	}

}
