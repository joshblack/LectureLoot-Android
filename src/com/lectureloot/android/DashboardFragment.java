package com.lectureloot.android;
//comment
import com.lectureloot.android.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.TextView;

public class DashboardFragment extends Fragment {
	private User mCurrentUser;
	private Meeting mUpcomingMeeting;
	
	private Button mCheckIn;
	private TextView userPoints;
	private TextView timeLeft;
	private TextView upcomingCourse;
	private enum CheckInStates{
		UserNeedsToCheckIn,
		UserHasUpcomingMeeting,
		UserIsDoneForTheDay,
		UserIsDoneForTheWager;
	}
	private CheckInStates currentCheckInState = CheckInStates.UserHasUpcomingMeeting;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//this is called when the view is displayed when the app launches
		
		//refresh all the values of the current view, like upcoming meeting, stats, and the times
		refreshUpcomingMeeting();
		updateCheckInView();
		
		View v = inflater.inflate(R.layout.fragment_dashboard, container, false);
		
		mCheckIn = (Button)v.findViewById(R.id.check_in_button);
		mCheckIn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//handles the click event
				
				//get the current location 
				//pass the location to the user model, to compare with the ideal location
				
				//if the user is in the right place, will display "Yay checked in!"
				//else display "invalid location, try again"
				
				//tell the view to change the check-in display to the neutral display
				
			}
		});
		
		userPoints = (TextView)v.findViewById(R.id.user_points);
		userPoints.setText(mCurrentUser.getPoints()+"pts");
		
		timeLeft = (TextView)v.findViewById(R.id.timeLeft);
		timeLeft.setText(getTimeUntilNextClass());
		
		upcomingCourse = (TextView)v.findViewById(R.id.nextMeeting);
		//need to get the prefix+code and the location
		//upcomingCourse.setText(mUpcomingMeeting.getName());
		
		
		return v;
	
	}
	
	private void refreshUpcomingMeeting(){
		mUpcomingMeeting = mCurrentUser.getUpcomingMeeting();
		//set the state of currentCheckInState to be whatever is associated with this. 
		//So if there's no upcoming meeting for the day, then 
	}
	
	private void updateCheckInView(){
		if(CheckInState.UserNeedsToCheckIn){
			
		}
		else if(CheckInState.UserHasUpcomingMeeting){
			
		}
		else{
			//user is done for the day or the week
		}
	}
	
	private String getTimeUntilNextClass(){
		//first make sure that the upcomingMeeting is correct. may need to put this elsewhere
		refreshUpcomingMeeting();
		
		//get information from the upcoming Meeting
		//Time meetTime =  mUpcomingMeeting.getTimeAt();
		
		//if the state of the meeting is 15 mins or less away, need to refresh the screen every second
		//refresh the time every second if within 15 mins
		
		
		//else subtract the time of the meeting - current time then add mins
		
		return "15:00";
	}
	
	private void toggleCheckInBackgroundState(boolean justCheckedIn){
		if(justCheckedIn){
			//set the state to neutral
		}
		else{
			//set the state back to be checked in.
		}
	}
	
	
}
