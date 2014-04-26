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
import android.util.Log;
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
	private List<String> wagerListDataHeader = null;
	private HashMap<String, List<Wager>> wagerListDataChild = null;

	private int tempPerClassWager;
	private String displayTempPerClassWager;
	private TextView DisplayCurrentWager;
	private User user;
	private int sessionId = 0;

	public WagerFragment() {
		this.user = User.getInstance();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_wager, container, false);

		user = User.getInstance();

		TextView userDisplay = (TextView)rootView.findViewById(R.id.userWager);
		userDisplay.setText("Your Wagers");
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

		// get the listview
		wagerExpListView = (ExpandableListView) rootView.findViewById(R.id.wager_lvExp);
		prepareWagerListData();
		wagerListAdapter = new ExpandableListWagerAdapter(getActivity(), prepareDataHeader(), prepareDataChild());
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
								boolean plzWork = false;
								if(formatedDate.equals(stringSessions.get(i)))
								{
									plzWork = true;
								}

								if(plzWork == true)
								{
									sessionId = i+1;
								}
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

						boolean checker = true;
						ArrayList<Wager> wagersChecker = user.getWagers();
						for(Wager w: wagersChecker)
						{

							if(w.getWagerSessionCode() == sessionId)
							{
								checker = false;
							}
							else
							{
								// still need to revise if LectureLoot needs this else
							}
						}

						/*******************************************************************************************************************************
						 * user_id    	- user.getUserId;                                                                                              *
						 * session_id	- TBD (probably from comparing dates or position in array                                                      *
						 * wagerUnitValue  - use tempPerClassWager                                                                                     *
						 * wagerTotalValue - usetempPerClassWager* meetings size                                                                       *
						 * lostPoints	- 0 ( gets defaulted to zero)                                                                                  *
						 ********************************************************************************************************************************/

						if(checker == true)
						{
							String wagersUrl ="http://lectureloot.eu1.frbit.net//api/v1/users/" + user.getUserId() + "/wagers?session_id="+sessionId+"&wagerUnitValue="
									+tempPerClassWager+"&wagerTotalValue="+newTotalWager+"&pointsLost=0";


							ArrayList<Wager> wagers = user.getWagers();
							int countWager = wagers.size();
							countWager++; 
							Wager newWager = new Wager(-1, sessionId, tempPerClassWager,newTotalWager, 0);
							ArrayList<Wager> newWagers = new  ArrayList<Wager>();
							
							
							String authToken = user.getAuthToken();
							HttpPostWagers wagersPost = new HttpPostWagers(authToken);

							HttpPostWagersFinishedListener listener = new HttpPostWagersFinishedListener(){
								Wager w;
								
								@Override
								public void onHttpPostWagersReady(String output){
									try {
										Log.i("Add Wager:","Output: " + output);
										JSONTokener tokener = new JSONTokener(output);
										JSONObject jsonReturn = (JSONObject) tokener.nextValue();
										w.setWagerId(jsonReturn.getInt("id"));	
										Log.i("Add Wager:", "Completed Successfully" + w.getWagerId());
									} catch (Exception e) {
										Log.i("Add Wager:",e.toString());
									}
								}
								
								public void setWager(Wager w){
									this.w = w;
								}
							};
							listener.setWager(newWager);
							wagersPost.setHttpPostWagersFinishedListener(listener);
							wagersPost.execute(new String[] {wagersUrl});
							
	
							// need sessionsId to be use to add the right wagers						
							System.out.println("newwagers:   "+ newWagers);
							System.out.println("Obvious basic print");
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

							Toast.makeText(getActivity(), "Wager Made", Toast.LENGTH_SHORT).show();
							dialog.dismiss();
							System.out.println("Test Test Test");


							wagerListAdapter.reloadItems(prepareDataHeader(), prepareDataChild());

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

	public ArrayList<String> prepareDataHeader() {
		ArrayList<String> header = new ArrayList<String>();
		wagerListDataChild = new HashMap<String, List<Wager>>();

		ArrayList<Wager> wagers = user.getWagers();

		List<Wager> oneWagerList = null;
		for (Wager wager : wagers) {
			header.add(Integer.toString(wager.getWagerSessionCode()));
			oneWagerList = new ArrayList<Wager>();
			oneWagerList.add(wager);
			wagerListDataChild.put(Integer.toString(wager.getWagerSessionCode()),oneWagerList);
		}

		return header;

	}

	public HashMap<String, List<Wager>> prepareDataChild() {
		ArrayList<String> header = prepareDataHeader();
		HashMap<String, List<Wager>> child = new HashMap<String, List<Wager>>();

		ArrayList<Wager> wagers = user.getWagers();

		List<Wager> oneWagerList = null;
		for (Wager wager : wagers) {
			header.add(Integer.toString(wager.getWagerSessionCode()));
			oneWagerList = new ArrayList<Wager>();
			oneWagerList.add(wager);
			child.put(Integer.toString(wager.getWagerSessionCode()),oneWagerList);
		}	
		return child;

	}


	private void prepareWagerListData() {
		wagerListDataHeader = new ArrayList<String>();
		wagerListDataChild = new HashMap<String, List<Wager>>();

		ArrayList<Wager> tempWagers = user.getWagers();
		System.out.println("Here!!!!!!");
		for(int i = 0;i <tempWagers.size();i++)
		{
			for(int z= i; z < tempWagers.size();z++)
			{
				if(tempWagers.get(i).getWagerSessionCode() >= tempWagers.get(z).getWagerSessionCode())
				{
					Wager tWager = tempWagers.get(i);
					tempWagers.set(i,tempWagers.get(z));
					tempWagers.set(z,tWager);
				}
			}
		
		}
		
		ArrayList<Wager> wagers = tempWagers;

		List<Wager> oneWagerList = null;
		for (Wager wager : wagers) {
			wagerListDataHeader.add(Integer.toString(wager.getWagerSessionCode()));
			oneWagerList = new ArrayList<Wager>();
			oneWagerList.add(wager);
			wagerListDataChild.put(Integer.toString(wager.getWagerSessionCode()),oneWagerList);

		}
	}

}
