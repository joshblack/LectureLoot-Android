package com.lectureloot.background;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

import com.lectureloot.android.Meeting;
import com.lectureloot.android.HttpGetFinishedListener;

public class MeetingListner extends HttpGetFinishedListener{
	Meeting meeting;
	
	public MeetingListner(Meeting meeting){
		this.meeting = meeting;
	}
	
	public void onHttpGetMeetingsReady(String output) {
		try {
			JSONTokener tokener = new JSONTokener(output);
			JSONArray array = null;
			array = (JSONArray) tokener.nextValue();
			JSONObject jsonBuilding;
			for(int i = 0; i < array.length(); i++) {
				jsonBuilding = array.getJSONObject(i);
				
				meeting.setLatitude(Double.parseDouble((String)jsonBuilding.getString("gpsLatitude")));
				meeting.setLongitude(Double.parseDouble((String)jsonBuilding.getString("gpsLongitude")));
				meeting.setBuildingCode((String)jsonBuilding.getString("buildingCode"));
			}
		} catch (Exception e) {
			Log.i("Building:",e.toString());
		}
	}
}
