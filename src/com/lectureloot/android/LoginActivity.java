package com.lectureloot.android;

import java.util.ArrayList;

import com.lectureloot.background.HttpPostCourses;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

		 final Handler toaster = new Handler() {
			 public void handleMessage(Message msg) {
		      	Toast.makeText(getApplicationContext(),(String)msg.obj, Toast.LENGTH_SHORT).show();
		     }
		 };
		
		Button loginButton = (Button)this.findViewById(R.id.loginButton);
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
						} else {	//send toast otherwise
							Message msg = toaster.obtainMessage();
							msg.obj = "Login Failed";
							toaster.sendMessage(msg);
						}
					}
				});
				thread.start();
			}
		});
		
		Button registerButton = (Button)this.findViewById(R.id.registerButton);
		registerButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				
				//Throw to register screen and end this activity
				Intent intent = new Intent(thisActivity, RegisterActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivityForResult(intent,1);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public void onBackPressed(){
		//do nothing (Back button is disabled)
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//kill the activity if registration was successfull
		if (requestCode == 1) 
		     if(resultCode == RESULT_OK)      
		         finish();          
	}
}
