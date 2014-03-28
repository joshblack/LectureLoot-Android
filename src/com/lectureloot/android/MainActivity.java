package com.lectureloot.android;

import java.net.URL;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lectureloot.android.adapter.TabsPagerAdapter;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {
	private static final String TAG = "Main";
	private ViewPager mViewPager;
	private TabsPagerAdapter mAdapter;
	private User mCurrentUser;
	private int[] nTabNames = {R.string.schedule_title, R.string.dashboard_title, R.string.wager_title};
	public static Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;	//get application context
		
		mCurrentUser = User.getInstance();
		Intent intent = new Intent(this, SplashActivity.class);
		startActivity(intent);
		
			
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
			Toast.makeText(this, "Settings Button Clicked", Toast.LENGTH_SHORT).show();
			return true;
		case R.id.action_stats:
			//bring up statistics
			Toast.makeText(this, "Statistics Button Clicked", Toast.LENGTH_SHORT).show();
			return true;
		case R.id.test_notification:
			//notify
			Intent intent = new Intent();
			PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
			Notification noti = new Notification.Builder(this)
			.setTicker("Get Your Ass to Class!")
			.setContentTitle("You have class in 15 minutes")
			.setContentText("Clock's ticking...")
			.setSmallIcon(R.drawable.ic_launcher)
			.setContentIntent(pIntent).getNotification();
			noti.flags=Notification.FLAG_AUTO_CANCEL;
			noti.sound = Uri.parse("android.resource://"
					+ this.getPackageName() + "/" + R.raw.coin);
			NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			notificationManager.notify(0, noti);

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
	protected void onStop(){
		//write data on app close
		super.onStop();
		if(mCurrentUser != null)
			mCurrentUser.writeToFile();
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
