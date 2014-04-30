package com.lectureloot.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class NotificationsSettingsActivity extends Activity {
	private Button saveNotificationsSettingsButton, cancelNotificationsSettingsButton;
	private Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_notifications);
		
		saveNotificationsSettingsButton = (Button)findViewById(R.id.saveNotificationsSettingsButton);
		cancelNotificationsSettingsButton = (Button)findViewById(R.id.cancelNotificationsSettingsButton);
		
		saveNotificationsSettingsButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					Toast.makeText(mContext, "Your Notifications Settings have been Saved!", Toast.LENGTH_SHORT).show();
					Intent settingsActivityIntent = new Intent(mContext, SettingsActivity.class);
					settingsActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(settingsActivityIntent);
			}
		});
		
		cancelNotificationsSettingsButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent settingsActivityIntent = new Intent(mContext, SettingsActivity.class);
				settingsActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(settingsActivityIntent);
			}
		});
		
		
	}

}
