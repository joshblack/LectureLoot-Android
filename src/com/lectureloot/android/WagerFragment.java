package com.lectureloot.android;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Date;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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


//import com.lectureloot.android.adapter.ExpandableListCourseAdapter;
import com.lectureloot.android.adapter.ExpandableListWagerAdapter;
//import com.lectureloot.background.HttpGetCourses;
//import com.lectureloot.background.HttpGetWagers;
//import com.lectureloot.background.HttpGetSession;

//import com.lectureloot.background.HttpGetMeetings;
//import com.lectureloot.background.HttpGetSessions;
//import com.lectureloot.background.HttpGetWagers;



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

//		Done by Sanders
//		String wagersUrl = "http://lectureloot.eu1.frbit.net/api/v1/users/1/wagers";
//		String authToken = "MJByIloBXVKpebWqqTqW9zGY0EUmAcyDDaiCzyyX";
//		HttpGetWagers wagersGetter = new HttpGetWagers(authToken);
//		wagersGetter.setHttpGetWagersFinishedListener(this);
//		wagersGetter.execute(new String[] {wagersUrl});

		Button addNewWagerButton;
		addNewWagerButton = (Button)rootView.findViewById(R.id.addWagerButton);
		addNewWagerButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

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
						
//						String userId = user.getUserId();
//						String wagersUrl = "http://lectureloot.eu1.frbit.net/api/v1/users/" + userId + "/wagers";
//						String authToken = user.getAuthToken();
//						HttpPostWagers wagersPost = new HttpPostWagers(authToken);
//				        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//				        nameValuePairs.add(new BasicNameValuePair("user_id", "12345"));
//				        nameValuePairs.add(new BasicNameValuePair("session_id", "0"));
//				        nameValuePairs.add(new BasicNameValuePair("wagerUnitValue", "12345"));
//				        nameValuePairs.add(new BasicNameValuePair("WagerTotalValue", "12345"));
//				        nameValuePairs.add(new BasicNameValuePair("pointsLost", "0"));
//				        
//						//wagersPost.setHttpPostWagersFinishedListener(this);
//						wagersPost.execute(new String[] {wagersUrl});
						
						/******************************************
						 * Need Sessions to get Wager Post to work*
						 ******************************************/
						
						Toast.makeText(getActivity(), "Wager Made", Toast.LENGTH_SHORT).show();
						dialog.dismiss();
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
			
			/**************************************************************
			 * Waiting for sessions database connection to be established *
			 * Then change the sessions for the group adapter             *
			 * So, the adatpers display the dates of each Wager           *
			 **************************************************************/		
		}
	}

//	public void onHttpGetSessionsReady(String output) {
////		
////	
////		System.out.println("onHttpGetSessionsReady enter");
////
////		wagerListDataHeader = new ArrayList<String>();
////		wagerListDataChild = new HashMap<String, List<Wager>>();
////
////		JSONTokener tokener = new JSONTokener(output);
////		JSONArray array = null;
////		System.out.println("onHttpGetSessionsReady 1");
////
////		try {
////			array = (JSONArray) tokener.nextValue();
////			//			System.out.println(array.toString());
////			System.out.println("onHttpGetSessionsReady 2");
////
////		} catch (Exception e) {
////			System.out.println(e.getMessage());
////		}
////		System.out.println("onHttpGetSessionsReady 3");
////
////		ArrayList<Sessions> sessions = jsonArrayToSessions(array);
////
////		System.out.println("onHttpGetWagersReady 4");
////
////		List<Sessions> oneSessionList = null;
////		for (Sessions sesssion : sessions) {
////			wagerListDataHeader.add(Integer.toString(session.getSessionId()));
////			oneSessionList = new ArrayList<Sessions>();
////			oneSessionList.add(session);
////			wagerListDataChild.put(Integer.toString(session.getSessionId()),oneSessionList);
////
////
////			System.out.println("onHttpGetWagersReady 5");
////			System.out.println("session:" + session.toString());
////
////		}
//	}

//	@Override
//	public void onHttpGetWagersReady(String output) {
//		System.out.println("onHttpGetWagersReady enter");
//
//		wagerListDataHeader = new ArrayList<String>();
//		wagerListDataChild = new HashMap<String, List<Wager>>();
//
//		JSONTokener tokener = new JSONTokener(output);
//		JSONArray array = null;
//		System.out.println("onHttpGetWagersReady 1");
//
//		try {
//			array = (JSONArray) tokener.nextValue();
//			//			System.out.println(array.toString());
//			System.out.println("onHttpGetWagersReady 2");
//
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		}
//		System.out.println("onHttpGetWagersReady 3");
//
//		ArrayList<Wager> wagers = jsonArrayToWagers(array);
//
//		System.out.println("onHttpGetWagersReady 4");
//
//		List<Wager> oneWagerList = null;
//		for (Wager wager : wagers) {
//			wagerListDataHeader.add(Integer.toString(wager.getWagerId()));
//			oneWagerList = new ArrayList<Wager>();
//			oneWagerList.add(wager);
//			wagerListDataChild.put(Integer.toString(wager.getWagerId()),oneWagerList);
//
//
//			System.out.println("onHttpGetWagersReady 5");
//			System.out.println("wager:" + wager.toString());
//
//		}
//
////		for (String wagerId : wagerListDataHeader) {
////			String authToken = "MJByIloBXVKpebWqqTqW9zGY0EUmAcyDDaiCzyyX";
////			HttpGetSessions sessionsGetter = new HttpGetSessions(authToken);
////			String SessionsUrl = "http://lectureloot.eu1.frbit.net/api/v1/courses/" + courseId + "/meetings";
////			System.out.println(meetingsUrl);
////			meetingsGetter.setHttpGetMeetingsFinishedListener(this);
////			meetingsGetter.execute(new String[] {meetingsUrl});
////		}
//
//		wagerListAdapter = new ExpandableListWagerAdapter(getActivity(), wagerListDataHeader, wagerListDataChild);
//		// setting list adapter
//		wagerExpListView.setAdapter(wagerListAdapter);
//		System.out.println("onHttpGetWagersReady exit");
//	}
	
//	private ArrayList<Wager> jsonArrayToWagers(JSONArray jsonWagers) {
//		ArrayList<Wager>  wagers = new ArrayList<Wager>();
//
//		try {
//			JSONObject jsonWager;
//			Wager wager;
//			for(int i = 0; i < jsonWagers.length(); i++) {
//				jsonWager = jsonWagers.getJSONObject(i);
//				wager = jsonObjectToWager(jsonWager);
//				wagers.add(wager);
//			}
//		} catch (Exception e) {
//
//		}
//
//		return wagers;
//	}
//	
//	private Wager jsonObjectToWager(JSONObject jsonWager) {
//
//		Wager wager = new Wager();
//		try{
//			wager.setWagerId((Integer)jsonWager.get("id"));
//			wager.setWagerSessionCode((Integer)jsonWager.get("session_id"));
//			wager.setWagerPerMeeting((Integer)jsonWager.get("wagerUnitValue"));
//			wager.setTotalWager((Integer)jsonWager.get("wagerTotalValue"));
//			wager.setCurrentWagerLost((Integer)jsonWager.get("pointsLost"));
//
//
//		} catch (Exception e){
//
//		}
//		return wager;
//	}
	
//	private ArrayList<Sessions> jsonArrayToSessions(JSONArray jsonSessions) {
//		ArrayList<Sessions>  sessions = new ArrayList<Sessions>();
//
//		try {
//			JSONObject jsonSession;
//			Sessions session;
//			for(int i = 0; i < jsonSessions.length(); i++) {
//				jsonSessions = jsonSessions.getJSONObject(i);
//				// here
//				session = jsonObjectToSessions(jsonSession);
//				sessions.add(session);
//			}
//		} catch (Exception e) {
//
//		}
//
//		return sessions;
//	}
//	
//	private Sessions jsonObjectToSessions(JSONObject jsonSessions) {
//
//		Sessions session = new Sessions();
//		try{
//			session.setSessionId((Integer)jsonSessions.get("id"));
//			session.setStartDate((Date)jsonSessions.get(""));
//			session.setEndDate((Date)jsonSessions.get(""));
//			
//			//wager.setWagerId((Integer)jsonWager.get("id"));
//			//wager.setWagerSessionCode((Integer)jsonWager.get("session_id"));
//			//wager.setWagerPerMeeting((Integer)jsonWager.get("wagerUnitValue"));
//			//wager.setTotalWager((Integer)jsonWager.get("wagerTotalValue"));
//			//wager.setCurrentWagerLost((Integer)jsonWager.get("pointsLost"));
//
//
//		} catch (Exception e){
//
//		}
//		return session;
//	}
		
}
