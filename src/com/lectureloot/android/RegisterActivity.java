package com.lectureloot.android;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		final Activity thisActivity = this;
		
		Button loginButton = (Button)this.findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				final EditText email = (EditText)thisActivity.findViewById(R.id.editText6);
				final EditText password = (EditText)thisActivity.findViewById(R.id.editText7);
				final EditText password2 = (EditText)thisActivity.findViewById(R.id.editText8);
				final EditText first = (EditText)thisActivity.findViewById(R.id.editText4);
				final EditText last = (EditText)thisActivity.findViewById(R.id.editText5);

				if(!password.getText().toString().equals(password2.getText().toString())){
						Toast.makeText(getApplicationContext(),"Passwords Do Not Match", Toast.LENGTH_SHORT).show();
						return;
				}
						
				 final Handler toaster = new Handler() {
					 public void handleMessage(Message msg) {
				      	Toast.makeText(getApplicationContext(),(String)msg.obj, Toast.LENGTH_SHORT).show();
				     }
				 };				
				
				//spawn worker thread to do the actual login stuffs
				Thread thread = new Thread(new Runnable(){
					public void run(){
						//try to register, if successful, kill the activity
						if(User.getInstance().doRegister(email.getText().toString(),password.getText().toString(),
								first.getText().toString(),last.getText().toString())){
							//tell the login function that we're done here
							Intent returnIntent = new Intent();
							setResult(RESULT_OK, returnIntent);        
							finish();	//kill the activity
						} else {	//toast otherwise
							Message msg = toaster.obtainMessage();
							msg.obj = "Registration Failed";
							toaster.sendMessage(msg);
						}
					}
				});
				thread.start();

				if((User.DEBUG_MODE & 2) != 0)
					Log.i("Login/Register:","Attempting Registration, Email:" + email.getText().toString() + " password:" + password.getText().toString());
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

	@Override
	public void onBackPressed(){
		//return to login activity
		Intent returnIntent = new Intent();
		setResult(RESULT_CANCELED, returnIntent);        
		finish();
	}
}
