package com.lectureloot.android;

import java.util.ArrayList;

import com.lectureloot.background.HttpPostCourses;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
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
		Log.i("Login:","Created Login Activity");

		Button loginButton;
		loginButton = (Button)this.findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				final EditText email = (EditText)thisActivity.findViewById(R.id.editText1);
				final EditText password = (EditText)thisActivity.findViewById(R.id.editText2);

				//spawn worker thread to do the actual login stuffs
				Thread thread = new Thread(new Runnable(){
					public void run(){
						//try to login, if successful, kill the activity
						if(User.getInstance().doLogin(email.getText().toString(),password.getText().toString())){
							finish();
						} else {	//toast otherwise
							Toast.makeText(thisActivity,"Login Failed", Toast.LENGTH_SHORT).show();
						}
					}
				});
				thread.start();

				Log.i("LoginActivity:","Attempting Login, Email:" + email.getText().toString() + " password:" + password.getText().toString());
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
