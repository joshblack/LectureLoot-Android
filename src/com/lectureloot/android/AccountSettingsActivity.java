package com.lectureloot.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AccountSettingsActivity extends Activity {
	
	private Button changeProfilePictureButton, saveAccountChangesButton, cancelAccountChangesButton;
	private EditText firstNameEditText, lastNameEditText, emailEditText, changePasswordEditText, confirmPasswordEditText;
	private Context mContext;
	private User mCurrentUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_settings);
		mContext = this;
		mCurrentUser = User.getInstance();
		
		changeProfilePictureButton = (Button)findViewById(R.id.changeProfilePictureButton);
		saveAccountChangesButton = (Button)findViewById(R.id.saveAccountChangesButton);
		cancelAccountChangesButton = (Button)findViewById(R.id.cancelAccountChangesButton);
		firstNameEditText = (EditText)findViewById(R.id.firstNameAccountSettingsEditText);
		lastNameEditText = (EditText)findViewById(R.id.lastNameAccountSettingsEditText);
		emailEditText = (EditText)findViewById(R.id.emailAddressAccountSettingsEditText);
		changePasswordEditText = (EditText)findViewById(R.id.changePasswordAccountSettingsEditText);
		confirmPasswordEditText = (EditText)findViewById(R.id.confirmPasswordChangeAccountSettingsEditText);
		
		saveAccountChangesButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO: actually update the user's information on the backend
				Toast.makeText(mContext, "Your Account Settings have been Saved!", Toast.LENGTH_SHORT).show();
				Intent settingsActivityIntent = new Intent(mContext, SettingsActivity.class);
				settingsActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(settingsActivityIntent);
			}
		});
		
		cancelAccountChangesButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent settingsActivityIntent = new Intent(mContext, SettingsActivity.class);
				settingsActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(settingsActivityIntent);
			}
		});
		
		changeProfilePictureButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent galleryIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("content://media/internal/images/media"));
				startActivity(galleryIntent);
			}
		});
		try {
			firstNameEditText.setText(mCurrentUser.getFirstName());
			lastNameEditText.setText(mCurrentUser.getLastName());
			emailEditText.setText(mCurrentUser.getEmail());
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
	}

}
