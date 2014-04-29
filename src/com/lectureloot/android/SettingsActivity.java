package com.lectureloot.android;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class SettingsActivity extends Activity {

	private User mCurrentUser;
	private static Context mContext;
	private ListView settingsListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		mContext = this;
		ArrayList<String> settingsOptions = new ArrayList<String>();
		String[] settings = new String[]{
				"Account Settings",
				"Modify Payment Information",
				//				"Account Removal (?)", moved to Account Settings
				"Tutorial",
				"FAQ/Help",
				"Report an Issue",
				"Terms of Service",
		};
		for(int i = 0; i < settings.length; i++) {
			settingsOptions.add(settings[i]);
		}
		settingsListView = (ListView) findViewById(R.id.settingListView);
		ArrayAdapter<String> settingsListViewAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, settingsOptions);
		settingsListView.setAdapter(settingsListViewAdapter);


	}

}
