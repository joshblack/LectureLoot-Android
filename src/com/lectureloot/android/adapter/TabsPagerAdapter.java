package com.lectureloot.android.adapter;

import com.lectureloot.android.DashboardFragment;
import com.lectureloot.android.ScheduleFragment;
import com.lectureloot.android.WagerFragment;

import android.R;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class TabsPagerAdapter extends FragmentPagerAdapter {
	//this class is an adapter - it provides views for the Fragments (activities) for the Tab thing to display
	private static final String TAG = "TabPagerAdapter";
	
	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
		Log.d(TAG, "instantiation");
	}

	@Override
	public Fragment getItem(int index) {
		Log.d(TAG, "getItem");
		switch(index){
		case 0:
			//left-most fragment
			return new ScheduleFragment();
		case 1:
			//center fragment
			return new DashboardFragment();
		case 2:
			//right-most fragment
			return new WagerFragment();
		default:
			return new Fragment();
		}
	}

	@Override
	public int getCount() {
		//we have three tabs
		return 3;
	}
	
//	@Override
//	public CharSequence getPageTitle(int position) {
//		return "Section "+(position+1);
//	}

}
