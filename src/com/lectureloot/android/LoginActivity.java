package com.lectureloot.android;

import java.util.ArrayList;

import com.lectureloot.background.HttpPostCourses;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login1);
		
		final Activity thisActivity = this;
		
		Button loginButton;
		loginButton = (Button)this.findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				EditText email = (EditText)thisActivity.findViewById(R.id.editText1);
				EditText password = (EditText)thisActivity.findViewById(R.id.editText2);
				
				final User user = User.getInstance();
				user.doLogin(email.getText().toString(),password.getText().toString());
				
				Log.i("LoginActivity:","Attempting Login, Email:" + email.getText().toString() + " password:" + password.getText().toString());
				while(user.loginFlag);	//wait for login to finish
				
				if(!user.getAuthToken().equals(" ")){
					Thread thread = new Thread( new Runnable(){
						public void run(){
							user.loadUserData();
						}
					});
					thread.start();
					finish();
				}
					
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}
