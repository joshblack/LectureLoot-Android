package com.lectureloot.android.adapter;

import java.util.ArrayList;
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
		
		System.out.println("wagerId:   "+ wagerId);
		
		final int wagerSessionId = ((Wager)getChild(groupPosition,childPosition)).getWagerSessionCode();	// get sessionId for wager
		
		System.out.println("wagerSessionId:   "+ wagerSessionId);
		
		ArrayList<Meeting> meetings = user.getMeetings();  									 // getting Meetings arraylist
		final int wagerMeetings = meetings.size();						// finding the size of arrayList to get total Meetings
		
		System.out.println("wagerMeetings:   "+ wagerMeetings);	
		
		((Wager)getChild(groupPosition, childPosition)).setTotalMeetings(wagerMeetings); 	 // setting total Meetings
		final int wagerUnitValue =((Wager)getChild(groupPosition, childPosition)).getWagerPerMeeting();	//getting Meetings from Wagers
		
		System.out.println("wagerUnitValue:   "+ wagerUnitValue);
		
		final int newTotalWager = wagerMeetings*wagerUnitValue;								// Getting new TotalWager
		
		System.out.println("newTotalWager:   "+ newTotalWager);								// used to verify TotalWagers is correct
		
		((Wager)getChild(groupPosition, childPosition)).setTotalWager(newTotalWager);		// setting new Total Wager
		
		
		
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.wager_list_items, null);
		}

		//TextView wagerSessionCode = (TextView) convertView.findViewById(R.id.wagerSessionCode);
		TextView wagerPerMeeting = (TextView) convertView.findViewById(R.id.wagerPerMeeting);
		TextView totalWager = (TextView) convertView.findViewById(R.id.totalWager);
		TextView LostWager = (TextView) convertView.findViewById(R.id.WagerLost);
		
		
		//final String wagerSessionCodeText = String.valueOf(((Wager)getChild(groupPosition,childPosition)).getWagerSessionCode());
		final String wagerPerMeetingText = String.valueOf(((Wager)getChild(groupPosition,childPosition)).getWagerPerMeeting());
		final String totalWagerText = String.valueOf(((Wager)getChild(groupPosition,childPosition)).getTotalWager());
		final String LostWagerText = String.valueOf(((Wager)getChild(groupPosition, childPosition)).getCurrentWagerLost());
		
		//wagerSessionCode.setText("  " + wagerSessionCodeText); // wagerSessionCode --- wagerSessionCodeText
		wagerPerMeeting.setText("  " + wagerPerMeetingText);
		totalWager.setText("  " + totalWagerText);
		LostWager.setText("  " + LostWagerText);
		
		wagerPerMeeting.setTextColor(Color.parseColor("#FFFFFF"));
		totalWager.setTextColor(Color.parseColor("#FFFFFF"));
		LostWager.setTextColor(Color.parseColor("#FFFFFF"));
		
		
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
						System.out.println("new wagerUnitValue: "+ tempPerClassWager);
						ArrayList<Meeting> tempMeetings = user.getMeetings();
						int size = tempMeetings.size();
						int nextTotal = size*tempPerClassWager;
						System.out.println("new wagerTotalvalue: "+ nextTotal);
						((Wager)getChild(groupPosition,childPosition)).setTotalMeetings(nextTotal);
						
						
//						String userId = user.getUserId();
//						String wagersUrl = "http://lectureloot.eu1.frbit.net/api/v1/users/" + userId + "/wagers/" + wagerId;
//						String authToken = user.getAuthToken();
//						HttpDeleteWagers getter = new HttpDeleteWagers(authToken);
//						//	getter.setHttpDeleteCoursesFinishedListener(this);
//						getter.execute(new String[] {wagersUrl});
						
						
						
						Toast.makeText(_context, "Edits have been made", Toast.LENGTH_SHORT).show();
						dialog.dismiss();
					}
				});

				Button dialogDeleteButton = (Button) dialog.findViewById(R.id.DeleteWagerButton);
				dialogDeleteButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						
						String userId = user.getUserId();
						String wagersUrl = "http://lectureloot.eu1.frbit.net/api/v1/users/" + userId + "/wagers/" + wagerId;
						String authToken = user.getAuthToken();
						HttpDeleteWagers getter = new HttpDeleteWagers(authToken);
						//	getter.setHttpDeleteCoursesFinishedListener(this);
						getter.execute(new String[] {wagersUrl});
						
						System.out.println("Here");
						ArrayList<Wager> wagers = user.getWagers();
						ArrayList<Wager> newWagers = new  ArrayList<Wager>();
						System.out.println("Wager arrayList"+wagers);
						for(int i=0;i<wagers.size();i++)
						{
							if(wagerSessionId != wagers.get(i).getWagerSessionCode())
							{
								newWagers.add(wagers.get(i));
							}
						}
						System.out.println("Wager arrayList"+wagers);
						System.out.println("newWager arrayList"+newWagers);
						
						
						user.setWagers(newWagers);
						//delete from view
						WagerFragment frg = new WagerFragment();
						_listDataHeader = frg.prepareDataHeader();
						_listDataChild = frg.prepareDataChild();
						notifyDataSetChanged();
						
						System.out.println("I survived");
						
//						ArrayList<Wager> wagers = new ArrayList<Wager>();
//						wagers = user.getWagers();
//						
//						System.out.println(wagers.toString());
//						
//						for(Wager wager : wagers){
//							if(wagerId == wager.getWagerId())
//								wagers.remove(wager);
//						}
//
//						System.out.println(wagers.toString());
//						user.setWagers(wagers);
						
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
	
	public void reloadItems(ArrayList<String> header, HashMap<String, List<Wager>> child)
	{
		this._listDataHeader = header;
		this._listDataChild = child;
		notifyDataSetChanged();
	}

}
