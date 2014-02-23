package com.lectureloot.android;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DashboardFragment extends Fragment implements LocationListener{
	private User mCurrentUser;
	private Meeting mUpcomingMeeting;
	private LocationManager mLocationManager;
	private String mProvider;
	
	private Button mCheckInButton;
	private TextView mUserPointsTextView;
	private TextView mTimeLeftTextView;
	private TextView mUpcomingMeetingTextView;
	private enum CheckInStates{
		UserNeedsToCheckIn,
		UserHasUpcomingMeeting,
		UserIsDoneForTheDay,
		UserIsDoneForTheWager;
	}
	private CheckInStates currentCheckInState = CheckInStates.UserNeedsToCheckIn;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//this is called when the view is displayed when the app launches
		View v = inflater.inflate(R.layout.fragment_dashboard, container, false);
		
		mCheckInButton = (Button)v.findViewById(R.id.check_in_button);
		mCheckInButton.setOnClickListener(new View.OnClickListener() {
			//handles the click event
			@Override
			public void onClick(View v) {
				//get the current location
				double[] latLong = getLocation();
				
				//compare with the ideal location
				//TODO
				boolean response = true;
				
				//if the user is in the right place, will display "Yay checked in!"
				//else display "invalid location, try again"
				if(response){
					Toast.makeText(getActivity(), "SCheck-in Successful", Toast.LENGTH_SHORT);
				}
				else{
					Toast.makeText(getActivity(), "Check-in Unsuccessful, Try Again", Toast.LENGTH_SHORT);
				}
				
				//tell the view to change the check-in display to the neutral display
				toggleCheckInBackgroundState();
			}
		});
		
		mUserPointsTextView = (TextView)v.findViewById(R.id.user_points);
		//userPoints.setText(mCurrentUser.getPoints()+"pts");
		
		mTimeLeftTextView = (TextView)v.findViewById(R.id.timeLeft);
		mUpcomingMeetingTextView = (TextView)v.findViewById(R.id.nextMeeting);
		
		//get location manager
		mLocationManager = (LocationManager)this.getActivity().getSystemService(Context.LOCATION_SERVICE);
		
		//refresh all the values of the current view, like upcoming meeting, stats, and the times
		refreshUpcomingMeetingViews();
		toggleCheckInBackgroundState();
		
		return v;
	
	}
	
	private double[] getLocation(){
		//define the criteria for selecting the location provider
		Criteria criteria = new Criteria();
		mProvider = mLocationManager.getBestProvider(criteria, false);
		Location location = mLocationManager.getLastKnownLocation(mProvider);

		//initialize the location fields
		if(location != null){
			//the phone has found a location
			System.out.println("Provider "+mProvider+", has been selected");
			//onLocationChanged(location);
		}
		
		double[] latlong = new double[2];
		
		latlong[0] = location.getLatitude();
		latlong[1] = location.getLongitude();
		
		mLocationManager.requestLocationUpdates(mProvider, 400, 1, this);
		
		return latlong;
	}
	
	private void refreshUpcomingMeetingViews(){
		//mUpcomingMeeting = mCurrentUser.getUpcomingMeeting();
		
		
		//set the state of currentCheckInState to be whatever is associated with this. 
		//So if there's no upcoming meeting for the day, then 
	}
	
	private void toggleCheckInBackgroundState(){
		switch(currentCheckInState){
			case UserNeedsToCheckIn:
				//change the state to red
				//mCheckIn.setVisibility(View.VISIBLE);
				
				break;
			case UserHasUpcomingMeeting:
				//change state to white for upcoming meeting
				//disable the checkIn button entirely
				//mCheckIn.setVisibility(View.GONE);
				
				break;
			case UserIsDoneForTheDay:
				//change state to congrats and happy day
				//disable the check in button
				
				break;
			case UserIsDoneForTheWager:
				//maybe we dont need this
				//disable the check in button
				
				break;
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		//we dont want this called multiple times. 
		//but I can change this back should we need like take the average or something
	}

	@Override
	public void onProviderDisabled(String provider) {
		System.out.println("Disabled provider " + provider);
	}

	@Override
	public void onProviderEnabled(String provider) {
		System.out.println("Enabled new provider ");
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		
	}
	
	
}
