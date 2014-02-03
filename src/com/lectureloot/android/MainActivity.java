package com.lectureloot.android;

import com.lectureloot.android.adapter.TabsPagerAdapter;
import com.lectureloot.android.R;

import android.os.Bundle;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.app.ActionBar;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {
	private static final String TAG = "Main";
	private ViewPager mViewPager;
	private TabsPagerAdapter mAdapter;
	private int[] nTabNames = {R.string.schedule_title, R.string.dashboard_title, R.string.wager_title};
	//private String[] nTabNames = {"schedule_title", "dashboard_title", "wager_title"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "on Create");
		setContentView(R.layout.activity_main);
		
		//Initialization of the tabs
		Log.d(TAG, "initializing tabs");
		mViewPager = (ViewPager)findViewById(R.id.pager);
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
		Log.d(TAG, "adding the tabs");
		for(int tab_name : nTabNames){
			Log.d(TAG, "adding tab "+tab_name);
			actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
		}
		Log.d(TAG, "done");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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

}
