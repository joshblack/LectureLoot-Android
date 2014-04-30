package com.lectureloot.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewDebug.FlagToString;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ReportIssueActivity extends Activity {
	private EditText issueEditText;
	private Button submitIssueButton, cancelIssueButton;
	private Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_issue);
		mContext = this;
		issueEditText = (EditText)findViewById(R.id.issueEditText);
		submitIssueButton = (Button)findViewById(R.id.submitIssueButton);
		cancelIssueButton = (Button)findViewById(R.id.cancelIssueButton);
		
		submitIssueButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(mContext, "Your issue has been submitted to the LectureLoot Team", Toast.LENGTH_LONG).show();
				Intent settingsActivityIntent = new Intent(mContext, SettingsActivity.class);
				settingsActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(settingsActivityIntent);
			}
		});
		
		cancelIssueButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent settingsActivityIntent = new Intent(mContext, SettingsActivity.class);
				settingsActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(settingsActivityIntent);
			}
		});
		
	}

}
