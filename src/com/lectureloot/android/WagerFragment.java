package com.lectureloot.android;



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
//import com.lectureloot.background.HttpGetWagers;
//import com.lectureloot.background.HttpGetSession;


public class WagerFragment extends Fragment implements HttpGetWagersFinishedListener, HttpGetSessionsFinishedListener{

	private ExpandableListWagerAdapter wagerListAdapter;
	private ExpandableListView wagerExpListView;
	private List<String> wagerListDataHeader;
	private HashMap<String, List<Wager>> wagerListDataChild;

	private int tempPerClassWager;
	private String displayTempPerClassWager;
	private TextView DisplayCurrentWager;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_wager, container, false);

		TextView userDisplay = (TextView)rootView.findViewById(R.id.userWager);
		//TODO: Set Display name dynamically based on Singleton User Model
		userDisplay.setText("LectureLoot's Wager");
		userDisplay.setTypeface(null, Typeface.BOLD_ITALIC);
		userDisplay.setTextSize(25);

		//        // get the listview
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
			//System.out.println("print");
			//Toast.makeText(getActivity(), "Button Clicked", Toast.LENGTH_SHORT).show();
			//System.out.println("toast");

				final Dialog dialog = new Dialog(getActivity());
				System.out.println("toast1");
				dialog.setContentView(R.layout.dialog_add_wager);
				System.out.println("toast2");
				dialog.setTitle("Make A Wager");
				System.out.println("toast3");
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
						// TODO Auto-generated method stub
						Toast.makeText(getActivity(), "Error: Could Not Create Wager", Toast.LENGTH_SHORT).show();
						dialog.dismiss();

					}
				});

				dialog.show();
			}
		});


		return rootView;
	}

	/*	public void onHttpGetWagerReady(String output) {
		System.out.println("onHttpGetCoursesReady enter");

		wagerListDataHeader = new ArrayList<String>();
		wagerListDataChild = new HashMap<String, List<Wager>>();

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

		ArrayList<Wager> Wagers = jsonArrayToWagers(array);
		System.out.println("onHttpGetCoursesReady 4");

		List<Wager> oneWagerList = null;
		for (Wager Wager : Wagers) {
			wagerListDataHeader.add(Integer.toString(Wager.getWagerSessionCode()));
			oneWagerList = new ArrayList<Wager>();
			oneWagerList.add(Wager);
			wagerListDataChild.put(Integer.toString(Wager.getWagerSessionCode()),oneWagerList);


			//			System.out.println("onHttpGetCoursesReady 5");
			//			System.out.println("course:" + course.toString());

		}

		for (String wagerCode : wagerListDataHeader) {
			HttpGetSession SessionsGetter = new HttpGetSession();
			String meetingsUrl = "http://lectureloot.eu1.frbit.net/api/v1/wagers/" + wagerCode + "/session_id";
			System.out.println(meetingsUrl);
			SessionsGetter.setHttpGetFinishedListener(this);
			SessionsGetter.execute(new String[] {meetingsUrl});
		}

		wagerListAdapter = new ExpandableListWagerAdapter(getActivity(), wagerListDataHeader, wagerListDataChild);
		// setting list adapter
		wagerExpListView.setAdapter((ListAdapter) wagerListAdapter);
		System.out.println("onHttpGetCoursesReady exit");
	}

	 */

	/*	public void onHttpGetSessionsReady(String output) {
		System.out.println("onHttpGetMeetingsReady enter");

		JSONTokener tokener = new JSONTokener(output);
		JSONArray array = null;
		System.out.println("onHttpGetMeetingsReady 1");
		Sessions sessions = null;
		List<Sessions> oneSessionList = null;
		try {
			array = (JSONArray) tokener.nextValue();
			System.out.println("array printstring" + array.toString());
			System.out.println("onHttpGetMeetingsReady 2");

			System.out.println("onHttpGetMeetingsReady 3");

			ArrayList<Sessions> wagerSessions = jsonArrayToSessions(array);
			System.out.println("onHttpGetMeetingsReady 4");
			sessions = wagerListDataChild.get(Integer.toString(Sessions.get(0).getSessionId())).get(0);
			sessions.setSession(wagerSessions);

			oneSessionList = new ArrayList<Sessions>();
			oneSessionList.add(sessions);
			wagerListDataChild.remove(sessions.getSessionId());
			wagerListDataChild.put(Integer.toString(sessions.getSessionId()),oneSessionList);

		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
		}
	}
	 */	

	//TODO figure out how meetings are stored, maybe have to create meeting model

	//	private Wager jsonObjectToWager(JSONObject jsonWager/*,JSONArray jsonMeetings*/) {
	/*
		Wager wager = new Wager();
		try{
			wager.setWagerSessionCode((Integer)jsonWager.get("id"));

//			String user_id = (String)jsonWager.getString("user_id");
//			int SessionCode = (int)jsonWager.getInt("session_id");
			int wagerUnitValue = (int)jsonWager.getInt("wagerUnitValue");
			int wagerTotalValue = (int)jsonWager.getInt("wagerTotalValue");
			int totalMeetings = wagerTotalValue/wagerUnitValue;


//			String courseCode = deptCode + courseNumber;
			wager.setWagerPerMeeting(wagerUnitValue);
			wager.setTotalWager(wagerTotalValue);
			wager.setTotalMeetings(totalMeetings);

		} catch (Exception e){

		}
		return wager;
	}
	 */

	private void prepareWagerListData() {
		wagerListDataHeader = new ArrayList<String>();
		wagerListDataChild = new HashMap<String, List<Wager>>();

		Wager Session1 = new Wager();
		Session1.setWagerSessionCode(1);
		Session1.setWagerPerMeeting(5);
		Session1.setTotalMeetings(10);
		Session1.setTotalWager(50);
		Session1.setCurrentWagerLost(11);

		List<Wager> Session1List = new ArrayList<Wager>();
		Session1List.add(Session1);

		
		// no longer need newWager
		//Wager newWager = new Wager();
		//newWager.setWagerSessionCode(100); // might need to create an string type for a title for Wager Class
		//List<Wager> newWagerList = new ArrayList<Wager>();
		//newWagerList.add(newWager);

		// Adding child data 
		wagerListDataHeader.add(Session1.getWagerSessionCodeString());

		wagerListDataChild.put(wagerListDataHeader.get(0), Session1List); // Header, Child data

	}
	/*    
	private ArrayList<Wager> jsonArrayToWagers(JSONArray jsonWagers) {
		ArrayList<Wager>  wagers = new ArrayList<Wager>();

		try {
			JSONObject jsonWager;
			Wager wager;
			for(int i = 0; i < jsonWagers.length(); i++) {
				jsonWager = jsonWagers.getJSONObject(i);
				wager = jsonObjectToWager(jsonWager);
				wagers.add(wager);
			}
		} catch (Exception e) {

		}

		return wagers;
	}
	 */
	/*
	private Sessions jsonObjectToSession(JSONObject jsonSession) {

		Sessions session = new Sessions();
		try{
			session.setSessionId((Integer)jsonSession.get("id"));
			session.setStartDate((Date)jsonSession.get("startDate"));
			session.setEndDate((Date)jsonSession.get("endDate"));

		} catch (Exception e){

		}
		return session;
	}

	private ArrayList<Sessions> jsonArrayToSessions(JSONArray jsonSessions) {
		ArrayList<Sessions>  session= new ArrayList<Sessions>();

		try {
			JSONObject jsonWagerSession;
			Sessions wagerSession;
			for(int i = 0; i < jsonSessions.length(); i++) {
				jsonWagerSession = jsonSessions.getJSONObject(i);
				wagerSession = jsonObjectToSession(jsonWagerSession);
				session.add(wagerSession);
			}
		} catch (Exception e) {

		}

		return session;
	}
	 */

	@Override
	public void onHttpGetSessionsReady(String output) {

	}

	@Override
	public void onHttpGetWagersReady(String output) {

	}
}
