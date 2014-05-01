package com.lectureloot.android;	

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.Toast;

import com.lectureloot.android.adapter.TabsPagerAdapter;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {
	private static final String TAG = "Main";
	private ViewPager mViewPager;
	private TabsPagerAdapter mAdapter;
	private User mCurrentUser;
	private int[] nTabNames = {R.string.schedule_title, R.string.dashboard_title, R.string.wager_title};
	public static Context mContext;
	private Thread workThread;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;	//get application context
		
		mCurrentUser = User.getInstance();
		//mCurrentUser.clearData(true, true, true, true);	//DEBUG: Wipe all the Data
		
		//if the user doesn't exist yet, and no file is found, load the data
		if(!mCurrentUser.loaded()){
				if(!mCurrentUser.loadFromFile()){
					//get the data
					Intent splashIntent = new Intent(this, SplashActivity.class);
					splashIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(splashIntent);
					
					if(!mCurrentUser.loggedIn()){
						//Login (second because activities display in a stack
						Intent loginIntent = new Intent(this, LoginActivity.class);
						loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(loginIntent);
					}
				} /*else {	//validate the itegrity of the file, not even caring anymore
					final FragmentActivity thisActivity = this; 
					workThread  = new Thread(new Runnable(){
						public void run(){
							if(!mCurrentUser.validateData()){
								Looper.prepare();
								Toast.makeText(thisActivity, "Data Error: Restarting App", Toast.LENGTH_LONG).show();
								finish();					//kill the app
							}
						}
					});
					workThread.start();
				}*/
		}
		
		if((User.DEBUG_MODE & 1) != 0) Log.i("Load Debug:","Proceeding with Program");
			
		//----------Load Main-------------
		setContentView(R.layout.activity_main);
		
		//Initialization of the tabs
		mViewPager = (ViewPager)findViewById(R.id.pager);
		
		//keeps background tabs alive
		//mViewPager.setOffscreenPageLimit(3);
		final ActionBar actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

		mViewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		//inits the selected indication to reflect which tab is selected
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		//add Tabs
		for(int tab_name : nTabNames){
			actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
		}

		//set the middle tab to be the default
		mViewPager.setCurrentItem(1, false);
		

	}

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		//handle item selection
		switch(item.getItemId()){
		case R.id.action_settings:
			//bring up settings
//			Toast.makeText(this, "Settings Button Clicked", Toast.LENGTH_SHORT).show();
			Intent settingsIntent = new Intent(mContext,SettingsActivity.class);
			startActivity(settingsIntent);
			return true;
		case R.id.action_report_cancelled_meeting:
			//get user courses for spinner
			ArrayList<Course> userCourses = mCurrentUser.getCourses();
			ArrayList<String> stringOfCourses = new ArrayList<String>();
			for(Course c : userCourses){
					stringOfCourses.add(c.getCourseTitle());
			}
			//set dialog
			final Dialog dialog = new Dialog(this);
			dialog.setContentView(R.layout.dialog_class_cancelled);
			dialog.setTitle("Class Cancellation");
			//set spinner
			final Spinner cancelableClassesSpinner = (Spinner)dialog.findViewById(R.id.classesToBeCancelled);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,stringOfCourses); 
			cancelableClassesSpinner.setAdapter(adapter);
			//set confirmation button
			Button confirmCancellationButton;
			confirmCancellationButton = (Button)dialog.findViewById(R.id.dialogConfirmCancellation);
			confirmCancellationButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Toast.makeText(mContext, "Class Cancelled Reported", Toast.LENGTH_SHORT).show();
					dialog.dismiss();
				}
			});
			
			dialog.show();
			return true;
		case R.id.action_logout:
			mCurrentUser.clearData(true,true,true,true);
//			Toast.makeText(this, "Logout Button Clicked", Toast.LENGTH_SHORT).show();
			final Dialog logoutDialog = new Dialog(this);
			logoutDialog.setContentView(R.layout.dialog_logout);
			logoutDialog.setTitle("Are you sure?");

			logoutDialog.show();
			
			Button confirmLogoutYesButton = (Button)logoutDialog.findViewById(R.id.dialogLogoutConfirmButton);
			confirmLogoutYesButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					logoutDialog.dismiss();
					
					Intent loginIntent = new Intent(mContext, LoginActivity.class);
					startActivity(loginIntent);
				}
			});
			
			Button confirmLogoutNoButton = (Button)logoutDialog.findViewById(R.id.dialogLogoutDenyButton);
			confirmLogoutNoButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					logoutDialog.dismiss();
				}
				
			});
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		//when the user taps on the tab button itself
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}
	
	@Override
	protected void onDestroy(){
		//write data on app close

		super.onDestroy();

		if(User.getInstance().loaded())
			User.getInstance().writeToFile();
		Log.i("Main Activity:","Stopped");
	}
	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}
}
