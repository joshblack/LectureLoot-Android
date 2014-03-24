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
	private int sessionId = 0;
	
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

		// get all the session information formated and ready before anything start
		final ArrayList<String> stringSessions = new ArrayList<String>();		// new ArrayLisy for formated date strings
		ArrayList<Sessions> tempSessions = user.getSessions();			// ArrayList copy of User's sessions
		String formatModel;												// String used to format the dates
		System.out.println("Date Strings: "+stringSessions);
		for(Sessions s : tempSessions)
		{
			formatModel = s.getStartDate() + " - " + s.getEndDate() ;
			stringSessions.add(formatModel);
		}
		
		System.out.println("User Sessions: "+user.getSessions());
		System.out.println(tempSessions);
		System.out.println("Date Strings: "+stringSessions);
		
//		System.out.println();
		
		
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
				});
				
/********************************************************************************************************************************
 * Dummy data is present to make it look good, but there will be need to sessions to be pull to populate spinners               *
 * To go into a post, you need the selected date's sessionId to not match any of the current wagers sessionIds                  *
 * If the above condition is met, then boolean checker will switch true and allow a post to go through                          *
 ********************************************************************************************************************************/
			
//				String[] wagerDates = {"2/3/2014 - 2/7/2014","2/10/2014 - 2/14/2014","2/17/2014 - 2/21/2014","2/24/2014 - 2/28/2014",
//						"3/3/2014 - 3/7/2014","3/10/2014 - 3/14/2014","3/17/2014 - 3/21/2014"};
				final Spinner wagerDateSpinner = (Spinner)dialog.findViewById(R.id.wagerDates);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,stringSessions); 
				wagerDateSpinner.setAdapter(adapter);
							
				Button dialogCreateButton = (Button) dialog.findViewById(R.id.dialogCreateWagerButton);
				dialogCreateButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						
						String formatedDate = wagerDateSpinner.getSelectedItem().toString();
						System.out.println("Text From spinner"+ formatedDate);
						if(stringSessions.size() != 0)
						{
							System.out.println("Hello, this if statement is true");
						}
						if(stringSessions.size() != 0)
						{
							for(int i = 0;i < stringSessions.size();i++)
								{
//									System.out.println(formatedDate);
//									System.out.println(stringSessions.get(i));
									String dummyTest = stringSessions.get(i);
									boolean plzWork = false;
									if(formatedDate.equals(stringSessions.get(i)))
									{
										plzWork = true;
									}
									
									if(plzWork == true)
									{
										sessionId = i+1;
//										System.out.println("SessionId is HERE:    "+sessionId);
//										System.out.println("true");
									}
//									System.out.println("false");
								}
						}
//						else
//						{// in case for date array - stringSessions has a size of zero, which will break code
//							
//						}
						
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
						
						boolean checker = true;
						ArrayList<Wager> wagersChecker = user.getWagers();
						for(Wager w: wagersChecker)
						{
//							System.out.println("sessionId"+ sessionId);
//							System.out.println("w's sessionId is here:   "+w.getWagerSessionCode());
							if(w.getWagerSessionCode() == sessionId)
							{
								checker = false;
//								System.out.println("False");
							}
							else
							{
//							System.out.println("True");
							}
						}
						
/*******************************************************************************************************************************
* user_id    	- user.getUserId;                                                                                              *
* session_id	- TBD (probably from comparing dates or position in array                                                      *
* wagerUnitValue  - use tempPerClassWager                                                                                      *
* wagerTotalValue - usetempPerClassWager* meetings size                                                                        *
* lostPoints	- 0 ( gets defaulted to zero)                                                                                  *
********************************************************************************************************************************/
						
						if(checker == true)
						{
							String wagersUrl ="http://lectureloot.eu1.frbit.net//api/v1/wagers?user_id="+userId+"&session_id="+sessionId+"&wagerUnitValue="
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
* Need to find a way to get the right wagerIf for wagers on a local basis														*
*********************************************************************************************************************************/
							ArrayList<Wager> wagers = user.getWagers();
							int countWager = wagers.size();
							countWager++; // problem with getting the correct wagerID, since there are more wagers than array spots
							Wager newWager = new Wager(-1, sessionId, tempPerClassWager,newTotalWager, 0);
							ArrayList<Wager> newWagers = new  ArrayList<Wager>();
						
// need sessionsId to be use to add the right wagers						
							System.out.println("newwagers:   "+ newWagers);
							System.out.println("Obvious basic print");
							for(int i=0;i<countWager;i++)
							{
//								System.out.println("WTF WHY ARE THINGS NOT GOING LIKE I WANT THEM TO");
								if(wagers.size()==0)
								{
									newWagers.add(newWager);
//									System.out.println("newwagers1: (adds newWager)  "+ newWagers);
									break;
								}
								
								else if(newWager.getWagerSessionCode() > wagers.get(0).getWagerSessionCode())
								{
//									System.out.println("newwagers2: (orignal)  "+ newWagers);
									newWagers.add(wagers.get(0));
//									System.out.println("newwagers2: (adds from wagers)  "+ newWagers);
//									System.out.println("wagers2_1: (original wagers)  "+ wagers);
									wagers.remove(0);
//									System.out.println("wagers2_2: (-1 wagers)  "+ wagers);
								}
								else
								{
//									System.out.println("newwagers3: (original) "+ newWagers);
									newWagers.add(newWager);
//									System.out.println("newwagers3: (+ newWager)  "+ newWagers);
//									System.out.println("wagers3_1: (wagers)  "+ wagers);
									break;
								}
//								System.out.println("PLZ FOR LOOP WORK.... JUST WORK T.T");
							}
							if(wagers.size()>0){
								for(int j=0;j<wagers.size();j++)
								{
//									System.out.println("It prolly should not go in here");
//									System.out.println("newwagers4: (Before complete print)  "+ newWagers);
//									System.out.println("wagers4_1: (wagerlist)  "+ wagers);
									newWagers.add(wagers.get(j));
									System.out.println("newwagers4: (should have wagerlist inside now)  "+ newWagers);
								}					
							}
							System.out.println("newwagers: (new)  "+ newWagers);
							for(Wager w : newWagers)
							{
								System.out.println(w.getWagerSessionCode());
							}
							user.setWagers(newWagers);
							
							System.out.println("List of Wagers:"+user.getWagers());
							for(Wager w : user.getWagers())
							{
								System.out.println(w.getWagerSessionCode());
							}
							Toast.makeText(getActivity(), "Wager Made", Toast.LENGTH_SHORT).show();
							dialog.dismiss();
							System.out.println("Test Test Test");
						}
						else
						{
							Toast.makeText(getActivity(), "Invald Date, please chose a different date", Toast.LENGTH_SHORT).show();
						}
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
			wagerListDataHeader.add(Integer.toString(wager.getWagerSessionCode()));
			oneWagerList = new ArrayList<Wager>();
			oneWagerList.add(wager);
			wagerListDataChild.put(Integer.toString(wager.getWagerSessionCode()),oneWagerList);
			System.out.println(wager.getWagerSessionCode());
			
		}
	}
		
}
