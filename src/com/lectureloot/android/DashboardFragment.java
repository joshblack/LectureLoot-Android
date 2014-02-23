package com.lectureloot.android;
//comment
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DashboardFragment extends Fragment {
	private User mCurrentUser;
	private Meeting mUpcomingMeeting;
	private LinearLayout mNeedsToCheckIn;
	private LinearLayout mUserCheckedIn;
	private Button mCheckIn;
	private Button mCheckedIn;
	private TextView userPoints;
	private TextView timeLeft;
	private TextView upcomingMeeting;
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
		mNeedsToCheckIn = (LinearLayout)v.findViewById(R.id.next_class_display);
		mUserCheckedIn = (LinearLayout)v.findViewById(R.id.checked_in_display);
		mUserCheckedIn.setVisibility(View.GONE);


		mCheckIn = (Button)v.findViewById(R.id.check_in_button);
		mCheckIn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(currentCheckInState == CheckInStates.UserNeedsToCheckIn){
					toggleCheckInBackgroundState();	
					currentCheckInState = CheckInStates.UserIsDoneForTheDay;
				}
				if(currentCheckInState == CheckInStates.UserIsDoneForTheDay){
					toggleCheckInBackgroundState();
					//					currentCheckInState = CheckInStates.UserNeedsToCheckIn;
				}

				//handles the click event

				//get the current location 
				//pass the location to the user model, to compare with the ideal location

				//if the user is in the right place, will display "Yay checked in!"
				//else display "invalid location, try again"

				//tell the view to change the check-in display to the neutral display

			}
		});

		mCheckedIn = (Button)v.findViewById(R.id.checked_in_button);

		userPoints = (TextView)v.findViewById(R.id.user_points);
		//userPoints.setText(mCurrentUser.getPoints()+"pts");

		timeLeft = (TextView)v.findViewById(R.id.timeLeft);
		upcomingMeeting = (TextView)v.findViewById(R.id.nextMeeting);

		//refresh all the values of the current view, like upcoming meeting, stats, and the times
		refreshUpcomingMeetingViews();
		toggleCheckInBackgroundState();

		return v;

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
			mNeedsToCheckIn.setVisibility(View.VISIBLE);
			//				mCheckIn.setVisibility(View.VISIBLE);
			//				mCheckedIn.setVisibility(View.GONE);

			break;
		case UserHasUpcomingMeeting:
			//change state to white for upcoming meeting
			//disable the checkIn button entirely
			mCheckIn.setVisibility(View.GONE);

			break;
		case UserIsDoneForTheDay:
			mNeedsToCheckIn.setVisibility(View.GONE);
			mUserCheckedIn.setVisibility(View.VISIBLE);
			//				mCheckIn.setVisibility(View.GONE);
			//				mCheckedIn.setVisibility(View.VISIBLE);
			//				timeLeft.setVisibility(View.GONE);
			//				upcomingMeeting.setVisibility(View.GONE);

			//change state to congrats and happy day
			//disable the check in button

			break;
		case UserIsDoneForTheWager:
			//maybe we dont need this
			//disable the check in button

			break;
		}
	}


}
