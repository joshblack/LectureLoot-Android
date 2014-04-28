package com.lectureloot.android;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Calendar;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.lectureloot.background.HttpPostCheckin;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioRecord.OnRecordPositionUpdateListener;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.Settings;

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
	private final int lateAmountPermitted = 5; // Number of minutes late that a user is allowed to check in within
	private final int earlyAmountPermitted = 15; // Number of minutes early that a user is allowed to check in within

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
		System.out.println("DashboardFragment onCreateView Called");
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

		mUpcomingMeetingTextView = (TextView)v.findViewById(R.id.nextMeeting);
		mTimeLeftSecsTextView = (TextView)v.findViewById(R.id.timeLeftSecs);
		mTimeLeftMinsTextView = (TextView)v.findViewById(R.id.timeLeftMins);


		mCheckInButton = (Button)v.findViewById(R.id.check_in_button);

		//Automatically set the next class information upon load

		try {
			ArrayList<Meeting> meetings = new ArrayList<Meeting>(); // currently loading test data since user.getMeetings() and user.getCourses() are not giving appropriate results
//			meetings = user.getMeetings();
			ArrayList<Course> courses = new ArrayList<Course>(); 		
//			courses = user.getCourses();

			// Beginning of test data creation:
					Course course = new Course();
					course.setCourseId(1);
					course.setCoursePrefix("CEN");
					course.setCourseNum("3031");
					courses.add(course);
					
					Meeting meeting = new Meeting();
					meeting.setMeetingId(1);
					meeting.setBuildingCode("LIT");
					meeting.setRoomNumber("109");
					meeting.setMeetingDay("R");
					meeting.setPeriod("E2");
					meeting.setCourseId(1);
			
					meetings.add(meeting);	//add to arrayList
			
					Meeting meeting2 = new Meeting();
					meeting2.setMeetingId(2);
					meeting2.setBuildingCode("LIT");
					meeting2.setRoomNumber("109");
					meeting2.setMeetingDay("W");
					meeting2.setPeriod("6");
					meeting2.setCourseId(1);
			
					meetings.add(meeting2);	//add to arrayList
					Meeting meeting3 = new Meeting();
					meeting3.setMeetingId(1);
					meeting3.setBuildingCode("LIT");
					meeting3.setRoomNumber("109");
					meeting3.setMeetingDay("W");
					meeting3.setPeriod("7-8");
					meeting3.setCourseId(1);
			
					meetings.add(meeting3);	//add to arrayList
			
					Meeting meeting4 = new Meeting();
					meeting4.setMeetingId(2);
					meeting4.setBuildingCode("LIT");
					meeting4.setRoomNumber("109");
					meeting4.setMeetingDay("M");
					meeting4.setPeriod("7");
					meeting4.setCourseId(1);
			
					meetings.add(meeting4);	//add to arrayList
					Meeting meeting5 = new Meeting();
					meeting5.setMeetingId(1);
					meeting5.setBuildingCode("LIT");
					meeting5.setRoomNumber("109");
					meeting5.setMeetingDay("N");
					meeting5.setPeriod("E3");
					meeting5.setCourseId(1);
			
					meetings.add(meeting5);	//add to arrayList
			
					Meeting meeting6 = new Meeting();
					meeting6.setMeetingId(2);
					meeting6.setBuildingCode("LIT");
					meeting6.setRoomNumber("109");
					meeting6.setMeetingDay("F");
					meeting6.setPeriod("6");
					meeting6.setCourseId(1);
			
					meetings.add(meeting6);	//add to arrayList
					
			// End of Test Data creation
					
			Collections.sort(meetings); // we want to sort the meetings in chronological order (Monday -> Sunday) and in order per day (Period 1 -> E3) in order to iterate in order
			// This sorting is now capable since Meeting implements Comparable
//			System.out.println("Meetings: ");
//			for(Meeting mtg : meetings){
//				System.out.println(mtg.toString());
//			}

			// Determine what the current day of the week is:
			String dayOfWeek = "";
			Calendar calendar = Calendar.getInstance();
			int day = calendar.get(Calendar.DAY_OF_WEEK);

			if (day == Calendar.SUNDAY) {
				dayOfWeek = "N";
			} else if (day == Calendar.MONDAY) {
				dayOfWeek = "M";
			} else if (day == Calendar.TUESDAY) {
				dayOfWeek = "T";
			} else if (day == Calendar.WEDNESDAY) {
				dayOfWeek = "W";
			} else if (day == Calendar.THURSDAY) {
				dayOfWeek = "R";
			} else if (day == Calendar.FRIDAY) {
				dayOfWeek = "F";
			} else if (day == Calendar.SATURDAY) {
				dayOfWeek = "S";
			}
			dayOfWeek = "F"; //TODO:  remove after testing
			
			// Grab the current system time, used for comparison against the user's schedule to see which meeting is most imminent 
			Time currentTime = new Time(Time.getCurrentTimezone());
			currentTime.setToNow();
			
			currentTime.set(0, 45, 12, currentTime.monthDay, currentTime.month, currentTime.year);

//			System.out.println("Day of Week:" + dayOfWeek);
//			System.out.println("Current time" + currentTime);


			// Create a HashMap pairing for Periods and Time Objects
			HashMap<String,Time> periodToTime = new HashMap<String,Time>();

			Time periodTime = new Time();
			periodTime.set(0, 25, 7, currentTime.monthDay, currentTime.month, currentTime.year);
			periodToTime.put("1", periodTime);

			periodTime = new Time();
			periodTime.set(0, 30, 8, currentTime.monthDay, currentTime.month, currentTime.year);
			periodToTime.put("2", periodTime);

			periodTime = new Time();
			periodTime.set(0, 35, 9, currentTime.monthDay, currentTime.month, currentTime.year);
			periodToTime.put("3", periodTime);

			periodTime = new Time();
			periodTime.set(0, 40, 10, currentTime.monthDay, currentTime.month, currentTime.year);
			periodToTime.put("4", periodTime);

			periodTime = new Time();
			periodTime.set(0, 45, 11, currentTime.monthDay, currentTime.month, currentTime.year);
			periodToTime.put("5", periodTime);

			periodTime = new Time();
			periodTime.set(0, 50, 12, currentTime.monthDay, currentTime.month, currentTime.year);
			periodToTime.put("6", periodTime);

			periodTime = new Time();
			periodTime.set(0, 55, 13, currentTime.monthDay, currentTime.month, currentTime.year);
			periodToTime.put("7", periodTime);

			periodTime = new Time();
			periodTime.set(0, 0, 15, currentTime.monthDay, currentTime.month, currentTime.year);
			periodToTime.put("8", periodTime);

			periodTime = new Time();
			periodTime.set(0, 5, 16, currentTime.monthDay, currentTime.month, currentTime.year);
			periodToTime.put("9", periodTime);

			periodTime = new Time();
			periodTime.set(0, 10, 17, currentTime.monthDay, currentTime.month, currentTime.year);
			periodToTime.put("10", periodTime);

			periodTime = new Time();
			periodTime.set(0, 15, 18, currentTime.monthDay, currentTime.month, currentTime.year);
			periodToTime.put("11", periodTime);

			periodTime = new Time();
			periodTime.set(0, 20, 19, currentTime.monthDay, currentTime.month, currentTime.year);
			periodToTime.put("E1", periodTime);

			periodTime = new Time();
			periodTime.set(0, 20, 20, currentTime.monthDay, currentTime.month, currentTime.year);
			periodToTime.put("E2", periodTime);

			periodTime = new Time();
			periodTime.set(0, 20, 21, currentTime.monthDay, currentTime.month, currentTime.year);
			periodToTime.put("E3", periodTime);

			// Get the User's Location (GPS Coordinates) - stored in latlong
			getLocation();

			
			// Check if the user's device has Mock Locations allowed (later, we will want to use this to not allow any checkins to go through if mock locations are enabled)
			if(Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION).equals("0"))
				System.out.println("mock location false");
			else
				System.out.println("mock location true");

			// Calculate how far away the user can be from their meetings' GPS coordinates
			double tolerance = (Math.sqrt((Math.pow(29.648685-29.65003,2) + Math.pow(-82.347619 + 82.3494,2))));
			
//			Location nextMeeting = new Location(latlong);
//			nextMeeting.setLatitude(29.64631); //test data for Reitz
//			nextMeeting.setLongitude(-82.34788); // test data for Reitz

			boolean flag = false; // flag used for checking whether any class has been found to be in range of the current time for the user

			// Check each of the meetings in order to see if that meeting is the correct meeting for the user to check in to
			for(int i = 0; i < meetings.size(); i++) {

				// Form valid periods for those meetings that have multiple periods listed
				String validMeetingPeriod = meetings.get(i).getPeriod();
				validMeetingPeriod = validMeetingPeriod.trim();
				switch (validMeetingPeriod.length()) {
				case 4:
					if(validMeetingPeriod.charAt(0) == 'E') { // E1E3
						validMeetingPeriod = validMeetingPeriod.substring(0,2); // E1
					} else if (validMeetingPeriod.charAt(1) == '-') { // 9-11
						validMeetingPeriod = validMeetingPeriod.substring(0,1); // 9
					} else { // 11E1
						validMeetingPeriod = validMeetingPeriod.substring(0,2); // 11
					}
				case 3: // 7-8
					validMeetingPeriod = validMeetingPeriod.substring(0,1); // 7
				case 2: // 11
				case 1: // 8
				}


				if(!(meetings.get(i).getMeetingDay().equalsIgnoreCase(dayOfWeek))) {
					// inside here is for when the meeting we are currently checking is before the current day (because we have sorted the meetings already) meaning this meeting has already happened
				} else {
					// inside here is for when the meeting we are checking occurs today at some time
					if(!(currentTime.toMillis(true) - periodToTime.get((validMeetingPeriod)).toMillis(true) > lateAmountPermitted*60*1000 )) {
						// inside here is for when the meeting we are currently checking against has not yet passed the current time, meaning is it the next meeting needing to be checked into
//						System.out.println(currentTime.toMillis(true));
//						System.out.println("Time difference in Seconds: " + (currentTime.toMillis(true) - periodToTime.get((validMeetingPeriod)).toMillis(true))/1000);
//						System.out.println("Late Amount Permitted in Seconds: " + (lateAmountPermitted*60));
//						System.out.println("Early Amount Permitted in Seconds:" + (-1 * earlyAmountPermitted * 60 * 1000)/1000);
						for(Course c : courses) {
							if(meetings.get(i).getCourseId() == c.getCourseId()) {
								String periodMinute = ((periodToTime.get(validMeetingPeriod).minute) < 10 ? "0" + Integer.toString(periodToTime.get(validMeetingPeriod).minute) : Integer.toString(periodToTime.get(validMeetingPeriod).minute));
								String hour = Integer.toString(((periodToTime.get(validMeetingPeriod).hour%12) == 0) ? 12 : periodToTime.get(validMeetingPeriod).hour%12);
								mUpcomingMeetingTextView.setText(c.getCoursePrefix() + c.getCourseNum() + "\n" + meetings.get(i).getBuildingCode() + " " + meetings.get(i).getRoomNumber());
//								System.out.println("Hour mark: " + Integer.toString(periodToTime.get(validMeetingPeriod).hour%12) + ":");
								//							mTimeLeftMinsTextView.setText(Integer.toString(periodToTime.get(validMeetingPeriod).hour%12) + ":");
								mTimeLeftSecsTextView.setText(hour +  ":" + periodMinute);
								currentCheckInState = CheckInStates.UserHasUpcomingMeeting;
								flag = true;
							}
						}
						if(  currentTime.toMillis(true) - periodToTime.get((validMeetingPeriod)).toMillis(true)  > -1 * earlyAmountPermitted * 60 * 1000  ) {
							// this meeting can be checked into
							// TODO send checkin POST request to the checkin api down below at the checkin button listener
							//						mTimeLeftMinsTextView.setText(Integer.toString(periodToTime.get(validMeetingPeriod).hour%12) + ":");
							String hour = Integer.toString(((periodToTime.get(validMeetingPeriod).hour%12) == 0) ? 12 : periodToTime.get(validMeetingPeriod).hour%12);
							String periodMinute = ((periodToTime.get(validMeetingPeriod).minute) < 10 ? "0" + Integer.toString(periodToTime.get(validMeetingPeriod).minute) : Integer.toString(periodToTime.get(validMeetingPeriod).minute));
							mTimeLeftSecsTextView.setText(hour + ":" + periodMinute);
							currentCheckInState = CheckInStates.UserNeedsToCheckIn;
//							if(getDistanceBetween(latlong, nextMeeting) > tolerance) { 
//								//							Toast.makeText(getActivity(), "Invalid Location, Try Again", Toast.LENGTH_SHORT).show();
//							} else {
//								//							Toast.makeText(getActivity(), "Check-in Successful", Toast.LENGTH_SHORT).show();
//								//							mNeedsToCheckInTimer.cancel();
//								currentCheckInState = CheckInStates.UserHasCheckedIn;
//							}
							break;
						}
						else {
							// it's too early to check into this meeting now, but it is still the next meeting that will need to be checked into, when the time is right
						}
					} else {
						// inside here is for when we have passed the checkin time for the meeting we are currently looking at
					}
				}
				// if we have just checked the last meeting for the user and the flag is still false, we know that there aren't anymore meetings for user today
				if(i == meetings.size()-1 && flag == false) {
					currentCheckInState = CheckInStates.UserIsDoneForTheDay;
				}
			}

		} catch (Exception e) {
			// Added the try catch block to handle issues with the User object not being able to load it's meetings and courses appropriately 
		}

		mCheckInButton.setOnClickListener(new View.OnClickListener() {
			//handles the click event
			
//			private Thread workerThread;
			@Override
			public void onClick(View v) {
				//get the current location
				// TODO: Hit the checkin endpoint with GPS coordinates and user information to perform a checkin when this button is clicked
				getLocation();
				
				String authToken = user.getAuthToken();
				authToken = "qfuVF2pkPNT5KGeYVQngRkCPqPGFQ2xjZl0ldRYk";
				HttpPostCheckin checkinPost = new HttpPostCheckin(authToken);
				// Listener to handle the POST checkin response
				HttpPostCheckinFinishedListener listener = new HttpPostCheckinFinishedListener() {
					@Override
					public boolean onHttpPostCheckinReady(String output) {
						boolean checkinSuccessful = true;
						try {
							Log.i("Post Checkin: ", "Output" + output);
							JSONTokener tokener = new JSONTokener(output);
							JSONObject jsonReturn = (JSONObject) tokener.nextValue();
							JSONObject message = jsonReturn.getJSONObject("message");
							try {
								Object success = message.get("success");
							} catch (JSONException jsone) {
								// There is no success message in the return message
								// This means that the checkin was not successful
								checkinSuccessful = false;
								System.out.println("checkinSuccessful: " + String.valueOf(checkinSuccessful));
								currentCheckInState = CheckInStates.UserNeedsToCheckIn;
							}
							try {
								Object error = message.get("error");
							} catch (JSONException jsone) {
								// There is no error message in the return message
								// This means that the checkin was successful
								checkinSuccessful = true;
								System.out.println("checkinSuccessful: " + String.valueOf(checkinSuccessful));
								currentCheckInState = CheckInStates.UserHasCheckedIn;
							}
						} catch (Exception e) {
							Log.i("Post Checkin", e.toString());
						}
						toggleCheckInBackgroundState();
					return checkinSuccessful;
					}
						
					};
					checkinPost.setHttpPostCheckinFinishedListener(listener);
					String checkinURL = "http://lectureloot.eu1.frbit.net/api/v1/users/" + /* user.getUserId() */"11" +  "/checkin/?latitude=" + Double.toString(latlong.getLatitude()) + "&longitude=" + Double.toString(latlong.getLongitude());
					System.out.println(checkinURL);
					checkinPost.execute(new String[] {checkinURL});
					
					
//				workerThread = new Thread( new Runnable() {
//					public void run() {
//						String checkinURL = "http://lectureloot.eu1.frbit.net/api/v1/users/" + user.getUserId() +  "/checkin/?latitude=" + Double.toString(latlong.getLatitude()) + "&longitude=" + Double.toString(latlong.getLongitude());
//						System.out.println(checkinURL);
//						checkinPost.execute(new String[] {checkinURL});
						
//					}
//				})
				

//				if(Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION).equals("0"))
//					System.out.println("mock location false");
//				else
//					System.out.println("mock location true");
				boolean response = false;
				// compare latLong with next upcoming meeting
				//				"lat":29.64631,
				//				   "lng":-82.34788
				//				center: 29.65003, -82.3494
				//				furthest: 29.648685, -82.347619

				// sort user's meetings based on day and time, then check for the next meeting based on current day of week and time
				System.out.println("Checkin button test");

				//compare with the ideal location
				//TODO


				//				if(response){
				//					Toast.makeText(getActivity(), "Check-in Successful", Toast.LENGTH_SHORT).show();
				//					mNeedsToCheckInTimer.cancel();
				//					currentCheckInState = CheckInStates.UserHasCheckedIn;
				//					toggleCheckInBackgroundState();
				//				}
				//				else{
				//					Toast.makeText(getActivity(), "Invalid Location, Try Again", Toast.LENGTH_SHORT).show();
				//				}				
			}
		});

		mUserPointsTextView = (TextView)v.findViewById(R.id.user_points);
		String userTotalPoints = String.valueOf(user.getPoints());
		mUserPointsTextView.setText(userTotalPoints + "pts");


		refreshUpcomingMeetingViews();

		//		mNeedsToCheckInTimer = new CountDownTimer(mUpcomingMeeting.getTimeInMillis(), CHECKIN_INTERVAL){
		//			//changes the time per second of how long the user has to check in
		//
		//			@Override
		//			public void onFinish() {
		//				mTimeLeftSecsTextView.setText(":(");
		//				toggleCheckInBackgroundState();
		//			}
		//
		//			@Override
		//			public void onTick(long millisUntilFinished) {
		//				int seconds=(int) (millisUntilFinished/1000)%60;
		//				long minutes=((millisUntilFinished-seconds)/1000)/60;
		//				if(seconds < 10)
		//					mTimeLeftSecsTextView.setText(minutes+":0"+seconds);
		//				else
		//					mTimeLeftSecsTextView.setText(minutes+":"+seconds);
		//			}
		//			
		//		};
		//		
		//		mUpcomingMeetingTimer = new CountDownTimer(mUpcomingMeeting.getTimeInMillis(), UPCOMING_MEETING_INTERVAL){
		//			//displays the time left until the user's next class
		//			
		//			@Override
		//			public void onFinish() {
		//				//dont do anything here
		//				//because if the time is within 15 mins, it goes to the other check in state
		//			}
		//
		//			@Override
		//			public void onTick(long millisUntilFinished) {
		//				long minutes = (millisUntilFinished/1000)/60 + 1;
		//				if(minutes < 15){
		//					currentCheckInState = CheckInStates.UserNeedsToCheckIn;
		//					toggleCheckInBackgroundState(); //this will stop this timer.
		//				}
		//				else if(minutes > 119){
		//					mTimeLeftMinsTextView.setText((millisUntilFinished/1000/60/60+1)+" hours");
		//				}
		//				else if(minutes > 59){
		//					mTimeLeftMinsTextView.setText("1 hour");
		//				}
		//				else{
		//					mTimeLeftMinsTextView.setText(minutes +" mins");
		//				}
		//				
		//			}
		//			
		//		};

		//refresh all the values of the current view, like upcoming meeting, stats, and the times
		toggleCheckInBackgroundState();

		return v;

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		toggleCheckInBackgroundState();
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
		//		if(secs <= (15*1000*60)){
		//			//the user needs to check in
		//			currentCheckInState = CheckInStates.UserNeedsToCheckIn;
		//		}
		//		else if(secs >= 8*1000*60*60){
		//			//if the user doesn't have a meeting in the next 8 hours
		//			currentCheckInState = CheckInStates.UserIsDoneForTheDay;
		//		}
		//		else if(secs == 0){
		//			currentCheckInState = CheckInStates.UserHasCheckedIn;
		//		}
		//		else {
		//			//the user has an upcoming meeting but doesn't need to check in yet
		//			currentCheckInState = CheckInStates.UserHasUpcomingMeeting;
		//		}
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
			//				mUpcomingMeetingTimer.cancel();
			//				mNeedsToCheckInTimer.start();
			break;

		case UserHasUpcomingMeeting:
			mUserNeedsToCheckInLayout.setVisibility(View.GONE);
			mUserHasUpcomingMeetingLayout.setVisibility(View.VISIBLE);
			mUserCheckedInLayout.setVisibility(View.GONE);
			mUserIsDoneLayout.setVisibility(View.GONE);

			//start or end the timer
			//				mUpcomingMeetingTimer.start();
			//				mNeedsToCheckInTimer.cancel();
			break;

		case UserHasCheckedIn:
			mUserNeedsToCheckInLayout.setVisibility(View.GONE);
			mUserHasUpcomingMeetingLayout.setVisibility(View.GONE);
			mUserCheckedInLayout.setVisibility(View.VISIBLE);
			mUserIsDoneLayout.setVisibility(View.GONE);

			//make sure the timers are off
			//				mUpcomingMeetingTimer.cancel();
			//				mNeedsToCheckInTimer.cancel();
			break;

		case UserIsDoneForTheDay:
			mUserNeedsToCheckInLayout.setVisibility(View.GONE);
			mUserHasUpcomingMeetingLayout.setVisibility(View.GONE);
			mUserCheckedInLayout.setVisibility(View.GONE);
			mUserIsDoneLayout.setVisibility(View.VISIBLE);

			//make sure the timers are off
			//				mUpcomingMeetingTimer.cancel();
			//				mNeedsToCheckInTimer.cancel();
			break;
		}
	}

	private double getDistanceBetween(Location first, Location second) 
	{
		double distance = 0;

		distance = (Math.sqrt((Math.pow(first.getLatitude()-second.getLatitude(),2) + Math.pow(first.getLongitude() - second.getLongitude(),2))));

		return distance;
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
