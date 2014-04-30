package com.lectureloot.android;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
		final ArrayList<String> settingsOptions = new ArrayList<String>();
		final String[] settings = new String[]{
				"Account Settings",
				"Modify Payment Information",
				//				"Account Removal (?)", moved to Account Settings
				"Notifications",
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

		settingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				String item = ((TextView)view).getText().toString();
				//				Toast.makeText(mContext, "Settings selection made at position:" + position, Toast.LENGTH_SHORT).show();
				//				Toast.makeText(mContext, "Settings selection made at id:" + id, Toast.LENGTH_SHORT).show();
				//				Toast.makeText(mContext, "Selection: " + item, Toast.LENGTH_SHORT).show();

				Intent settingsSelectionIntent;
				
				if (position == settingsOptions.indexOf(settings[0])) { // Account Settings
					settingsSelectionIntent = new Intent(mContext, AccountSettingsActivity.class);
					startActivity(settingsSelectionIntent);
				} else if (position == settingsOptions.indexOf(settings[1])) { // Modify Payment Information
					settingsSelectionIntent = new Intent(mContext, PaymentInformationActivity.class);
					startActivity(settingsSelectionIntent);
				} else if (position == settingsOptions.indexOf(settings[2])) { // Notifications
					settingsSelectionIntent = new Intent(mContext, NotificationsSettingsActivity.class);
					startActivity(settingsSelectionIntent);
				} else if (position == settingsOptions.indexOf(settings[3])) { // Tutorial
					settingsSelectionIntent = new Intent(mContext, TutorialActivity.class);
					startActivity(settingsSelectionIntent);
				} else if (position == settingsOptions.indexOf(settings[4])) { // FAQ/Help
					settingsSelectionIntent = new Intent(mContext, FAQActivity.class);
					startActivity(settingsSelectionIntent);
				} else if (position == settingsOptions.indexOf(settings[5])) { // Report an Issue
					settingsSelectionIntent = new Intent(mContext, ReportIssueActivity.class);
					startActivity(settingsSelectionIntent);
				} else if (position == settingsOptions.indexOf(settings[6])) { // Terms of Service
					settingsSelectionIntent = new Intent(mContext, TermsOfServiceActivity.class);
					startActivity(settingsSelectionIntent);
				}

			}
		});


	}

}
