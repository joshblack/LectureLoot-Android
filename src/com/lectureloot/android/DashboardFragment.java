package com.lectureloot.android;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DashboardFragment extends Fragment implements LocationListener{
	private User mCurrentUser;
	private Meeting mUpcomingMeeting;
	private LocationManager mLocationManager;
	private Location latlong;
	private String mProvider;
	private User user;
	
	private Button mCheckInButton;
	private TextView mUserPointsTextView;
	private TextView mTimeLeftSecsTextView;
	private TextView mTimeLeftMinsTextView;
	private TextView mUpcomingMeetingTextView;
	private LinearLayout mUserNeedsToCheckInLayout;
	private LinearLayout mUserCheckedInLayout;
	private LinearLayout mUserHasUpcomingMeetingLayout;
	private LinearLayout mUserIsDoneLayout;
	
	private CountDownTimer mNeedsToCheckInTimer;
	private CountDownTimer mUpcomingMeetingTimer;
	private final long CHECKIN_INTERVAL = 1000;
	private final long UPCOMING_MEETING_INTERVAL = 60000; 

	private enum CheckInStates{
		UserNeedsToCheckIn,
		UserHasUpcomingMeeting,
		UserHasCheckedIn,
		UserIsDoneForTheDay;
	}
	private CheckInStates currentCheckInState = CheckInStates.UserHasUpcomingMeeting;
	
	private Meeting[] testMeetings = {
		new Meeting(16*1000*60),//30 mins from now
		new Meeting(15*1000*60),
		new Meeting(50*1000*60),
		new Meeting(120*1000*60)
	};
	int currentMeeting = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//this is called when the view is displayed when the app launches
		View v = inflater.inflate(R.layout.fragment_dashboard, container, false);
		
		user = User.getInstance();
		//get location manager
		mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
				
		mUserNeedsToCheckInLayout = (LinearLayout)v.findViewById(R.id.needs_to_check_in_layout);
		
		mUserCheckedInLayout = (LinearLayout)v.findViewById(R.id.has_checked_in_layout);
		mUserCheckedInLayout.setVisibility(View.GONE);
		
		mUserHasUpcomingMeetingLayout = (LinearLayout) v.findViewById(R.id.has_upcoming_meeting_layout);
		mUserHasUpcomingMeetingLayout.setVisibility(View.GONE);
		
		mUserIsDoneLayout = (LinearLayout)v.findViewById(R.id.is_done_for_day_layout);
		mUserIsDoneLayout.setVisibility(View.GONE);

		mCheckInButton = (Button)v.findViewById(R.id.check_in_button);
		mCheckInButton.setOnClickListener(new View.OnClickListener() {
			//handles the click event
			@Override
			public void onClick(View v) {
				//get the current location
				getLocation();
				
				//compare with the ideal location
				//TODO
				boolean response = true;
				
				if(response){
					Toast.makeText(getActivity(), "Check-in Successful", Toast.LENGTH_SHORT).show();
					mNeedsToCheckInTimer.cancel();
					currentCheckInState = CheckInStates.UserHasCheckedIn;
					toggleCheckInBackgroundState();
				}
				else{
					Toast.makeText(getActivity(), "Invalid Location, Try Again", Toast.LENGTH_SHORT).show();
				}				
			}
		});
		
		mUserPointsTextView = (TextView)v.findViewById(R.id.user_points);
		String userTotalPoints = String.valueOf(user.getPoints());
		mUserPointsTextView.setText(userTotalPoints + "pts");
		
		mTimeLeftSecsTextView = (TextView)v.findViewById(R.id.timeLeftSecs);
		mTimeLeftMinsTextView = (TextView)v.findViewById(R.id.timeLeftMins);
		mUpcomingMeetingTextView = (TextView)v.findViewById(R.id.nextMeeting);
			
		refreshUpcomingMeetingViews();
		
		mNeedsToCheckInTimer = new CountDownTimer(mUpcomingMeeting.getTimeInMillis(), CHECKIN_INTERVAL){
			//changes the time per second of how long the user has to check in

			@Override
			public void onFinish() {
				mTimeLeftSecsTextView.setText(":(");
				toggleCheckInBackgroundState();
			}

			@Override
			public void onTick(long millisUntilFinished) {
				int seconds=(int) (millisUntilFinished/1000)%60;
				long minutes=((millisUntilFinished-seconds)/1000)/60;
				if(seconds < 10)
					mTimeLeftSecsTextView.setText(minutes+":0"+seconds);
				else
					mTimeLeftSecsTextView.setText(minutes+":"+seconds);
			}
			
		};
		
		mUpcomingMeetingTimer = new CountDownTimer(mUpcomingMeeting.getTimeInMillis(), UPCOMING_MEETING_INTERVAL){
			//displays the time left until the user's next class
			
			@Override
			public void onFinish() {
				//dont do anything here
				//because if the time is within 15 mins, it goes to the other check in state
			}

			@Override
			public void onTick(long millisUntilFinished) {
				long minutes = (millisUntilFinished/1000)/60 + 1;
				if(minutes < 15){
					currentCheckInState = CheckInStates.UserNeedsToCheckIn;
					toggleCheckInBackgroundState(); //this will stop this timer.
				}
				else if(minutes > 119){
					mTimeLeftMinsTextView.setText((millisUntilFinished/1000/60/60+1)+" hours");
				}
				else if(minutes > 59){
					mTimeLeftMinsTextView.setText("1 hour");
				}
				else{
					mTimeLeftMinsTextView.setText(minutes +" mins");
				}
				
			}
			
		};

		//refresh all the values of the current view, like upcoming meeting, stats, and the times
		toggleCheckInBackgroundState();

		return v;

	}

	
	private void getLocation(){
		//define the criteria for selecting the location provider
		Criteria criteria = new Criteria();
		mProvider = mLocationManager.getBestProvider(criteria, false);
		Location location = mLocationManager.getLastKnownLocation(mProvider);
		
		//initialize the location fields
		if(location != null){
			//the phone has found a location
			Log.d("getLocation", "Provider "+mProvider+", has been selected");
			onLocationChanged(location);
			Log.d("getLocation", "Lat: "+latlong.getLatitude()+"; Long: "+latlong.getLongitude());

		}
		else{
			//TODO display to the user that it is null
			Log.d("getLocation", "location is null");
		}
	}
	
	private void refreshUpcomingMeetingViews(){
		mUpcomingMeeting = testMeetings[currentMeeting];
		
		long secs = mUpcomingMeeting.getTimeInMillis();
		//check what the currentCheckInState should be based on the time 
		if(secs <= (15*1000*60)){
			//the user needs to check in
			currentCheckInState = CheckInStates.UserNeedsToCheckIn;
		}
		else if(secs >= 8*1000*60*60){
			//if the user doesn't have a meeting in the next 8 hours
			currentCheckInState = CheckInStates.UserIsDoneForTheDay;
		}
		else if(secs == 0){
			currentCheckInState = CheckInStates.UserHasCheckedIn;
		}
		else {
			//the user has an upcoming meeting but doesn't need to check in yet
			currentCheckInState = CheckInStates.UserHasUpcomingMeeting;
		}
		currentMeeting++;
	}


	private void toggleCheckInBackgroundState(){
		
		switch(currentCheckInState){
			case UserNeedsToCheckIn:
				mUserNeedsToCheckInLayout.setVisibility(View.VISIBLE);
				mUserHasUpcomingMeetingLayout.setVisibility(View.GONE);
				mUserCheckedInLayout.setVisibility(View.GONE);
				mUserIsDoneLayout.setVisibility(View.GONE);
				
				//start or end the timer
				mUpcomingMeetingTimer.cancel();
				mNeedsToCheckInTimer.start();
				break;
				
			case UserHasUpcomingMeeting:
				mUserNeedsToCheckInLayout.setVisibility(View.GONE);
				mUserHasUpcomingMeetingLayout.setVisibility(View.VISIBLE);
				mUserCheckedInLayout.setVisibility(View.GONE);
				mUserIsDoneLayout.setVisibility(View.GONE);
				
				//start or end the timer
				mUpcomingMeetingTimer.start();
				mNeedsToCheckInTimer.cancel();
				break;
				
			case UserHasCheckedIn:
				mUserNeedsToCheckInLayout.setVisibility(View.GONE);
				mUserHasUpcomingMeetingLayout.setVisibility(View.GONE);
				mUserCheckedInLayout.setVisibility(View.VISIBLE);
				mUserIsDoneLayout.setVisibility(View.GONE);
				
				//make sure the timers are off
				mUpcomingMeetingTimer.cancel();
				mNeedsToCheckInTimer.cancel();
				break;
				
			case UserIsDoneForTheDay:
				mUserNeedsToCheckInLayout.setVisibility(View.GONE);
				mUserHasUpcomingMeetingLayout.setVisibility(View.GONE);
				mUserCheckedInLayout.setVisibility(View.GONE);
				mUserIsDoneLayout.setVisibility(View.VISIBLE);
				
				//make sure the timers are off
				mUpcomingMeetingTimer.cancel();
				mNeedsToCheckInTimer.cancel();
				break;
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		//we dont want this called multiple times. 
		//but I can change this back should we need like take the average or something
		latlong = location;
	}

	@Override
	public void onProviderDisabled(String provider) {
		Toast.makeText(getActivity(), "Disabled Location " + provider,
		        Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderEnabled(String provider) {
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		
	}
	
	@Override
	public void onPause() {
		super.onPause();
		mLocationManager.removeUpdates(this);
	}

}
