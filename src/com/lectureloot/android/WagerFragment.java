package com.lectureloot.android;



import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Date;


import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lectureloot.android.adapter.ExpandableListWagerAdapter;
import com.lectureloot.background.HttpPostWagers;


public class WagerFragment extends Fragment {

	private ExpandableListWagerAdapter wagerListAdapter;
	private ExpandableListView wagerExpListView;
	private List<String> wagerListDataHeader;
	private HashMap<String, List<Wager>> wagerListDataChild;

	private int tempPerClassWager;
	private String displayTempPerClassWager;
	private TextView DisplayCurrentWager;
	private User user;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_wager, container, false);

		user = User.getInstance();
		
		TextView userDisplay = (TextView)rootView.findViewById(R.id.userWager);
		//String name = user.getName();
		userDisplay.setText( /*name +*/ "LectureLoot's Wager");
		userDisplay.setTypeface(null, Typeface.BOLD_ITALIC);
		userDisplay.setTextSize(25);

		// get the listview
		wagerExpListView = (ExpandableListView) rootView.findViewById(R.id.wager_lvExp);
		prepareWagerListData();
		wagerListAdapter = new ExpandableListWagerAdapter(getActivity(), wagerListDataHeader, wagerListDataChild);
		// setting list adapter
		wagerExpListView.setAdapter(wagerListAdapter);

		Button addNewWagerButton;
		addNewWagerButton = (Button)rootView.findViewById(R.id.addWagerButton);
		addNewWagerButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				final Dialog dialog = new Dialog(getActivity());
				dialog.setContentView(R.layout.dialog_add_wager);
				dialog.setTitle("Make A Wager");
				tempPerClassWager = 10;
				System.out.println("toast");
				
				Button dialogDecrementButton = (Button) dialog.findViewById(R.id.decrementPerMeetingWager);
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
					// temp solution, don't remember if needed
				});

				Button dialogIncrementButton = (Button) dialog.findViewById(R.id.incrementPerMeetingWager);
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
					// temp solution don't remember if needed
				});

				Button dialogCreateButton = (Button) dialog.findViewById(R.id.dialogCreateWagerButton);
				dialogCreateButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						
//						JSONObject wagerJsonObject = new JSONObject();
						String userId = user.getUserId();
						ArrayList<Meeting> meetings = user.getMeetings();
						int wagerMeetings = meetings.size();
						int newTotalWager = tempPerClassWager*wagerMeetings;
						System.out.println("UserId: "+ userId);
						System.out.println("Wager Per Class:  "+tempPerClassWager);
						System.out.println("Wager Meetings: "+ wagerMeetings);
						System.out.println("Total Wager Value: "+ newTotalWager);
						// I want to see if the value for the variables are correct
						//if they are, I will insert them into the url for post
						
						
/*******************************************************************************************************************************
* user_id    	- user.getUserId;                                                                                              *
* session_id	- TBD (probably from comparing dates or position in array                                                      *
* wagerUnitValue  - use tempPerClassWager                                                                                      *
* wagerTotalValue - usetempPerClassWager* meetings size                                                                        *
* lostPoints	- 0 ( gets defaulted to zero)                                                                                  *
********************************************************************************************************************************/
						
						String wagersUrl ="http://lectureloot.eu1.frbit.net//api/v1/wagers?user_id="+userId+"&session_id=10&wagerUnitValue="
						+tempPerClassWager+"&wagerTotalValue="+newTotalWager+"&pointsLost=0";
						
//						String wagersUrl = "http://lectureloot.eu1.frbit.net/api/v1/wagers?user_id="+4+
//								"&session_id="+9+"&wagerUnitValue="+5+"&wagerTotalValue="+25+"&pointsLost="+0;
						String authToken = user.getAuthToken();
						HttpPostWagers wagersPost = new HttpPostWagers(authToken);
						
						//wagersPost.setHttpPostWagersFinishedListener(this);
						wagersPost.execute(new String[] {wagersUrl});
						
/********************************************************************************************************************************
* Need Sessions to get Wager Post to work                                                                                       *
* Current code is a hard coded version                                                                                          *
*********************************************************************************************************************************/
						ArrayList<Wager> wagers = user.getWagers();
						int countWager = wagers.size();
						countWager++;
						Wager newWager = new Wager(countWager, 10, tempPerClassWager,newTotalWager, 0);
						ArrayList<Wager> newWagers = new  ArrayList<Wager>();
						
						for(int i=0;i<countWager;i++)
						{
							if(wagers.size()==0)
							{
								newWagers.add(newWager);
								break;
							}
							else if(newWager.getWagerSessionCode() > wagers.get(0).getWagerSessionCode())
							{
								newWagers.add(wagers.get(0));
								wagers.remove(0);
							}
							else
							{
								newWagers.add(newWager);
								break;
							}
						}
						if(wagers.size()>0){
							for(int j=0;j<wagers.size();j++)
							{
								newWagers.add(wagers.get(j));
							}					
						}
						user.setWagers(newWagers);
						System.out.println("List of Wagers:"+user.getWagers());
						Toast.makeText(getActivity(), "Wager Made", Toast.LENGTH_SHORT).show();
						dialog.dismiss();
						System.out.println("Test Test Test");
					}
				});
				dialog.show();
			}
		});
		return rootView;
	}

	private void prepareWagerListData() {
		wagerListDataHeader = new ArrayList<String>();
		wagerListDataChild = new HashMap<String, List<Wager>>();

		ArrayList<Wager> wagers = user.getWagers();
		System.out.println("Wagers Array List " + wagers.toString());
		
		List<Wager> oneWagerList = null;
		for (Wager wager : wagers) {
			wagerListDataHeader.add(Integer.toString(wager.getWagerId()));
			oneWagerList = new ArrayList<Wager>();
			oneWagerList.add(wager);
			wagerListDataChild.put(Integer.toString(wager.getWagerId()),oneWagerList);
			System.out.println(wager.getWagerId());
			
/*********************************************************************************************************************************
* Waiting for sessions database connection to be established                                                                     *
* Then change the sessions for the group adapter                                                                                 *
* So, the adapters display the dates of each Wager                                                                               *
**********************************************************************************************************************************/		
		}
	}
		
}
