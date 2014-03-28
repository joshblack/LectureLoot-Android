package com.lectureloot.android;

import java.util.ArrayList;

import com.lectureloot.background.HttpPostCourses;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
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
		
		final Dialog spinnerDialog = new Dialog(this);
        spinnerDialog.getWindow().getCurrentFocus();
        spinnerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        spinnerDialog.setContentView(R.layout.dialog_temp_load);
        spinnerDialog.setCancelable(false);
        spinnerDialog.setOwnerActivity(this);

        getActionBar().setTitle("Begin Looting"); 
        
		Button loginButton;
		loginButton = (Button)this.findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				EditText email = (EditText)thisActivity.findViewById(R.id.editText1);
				EditText password = (EditText)thisActivity.findViewById(R.id.editText2);
				
				User user = User.getInstance();
				user.doLogin(email.getText().toString(),password.getText().toString());
				
				spinnerDialog.show();
				while(!user.loginFlag);	//wait for login to finish
				spinnerDialog.dismiss();
				
				if(!user.getAuthToken().equals(" "))
					finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}
