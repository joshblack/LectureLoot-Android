package com.lectureloot.android;

import java.util.ArrayList;

import com.lectureloot.background.HttpPostCourses;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login1);

		final Activity thisActivity = this;

		Button addNewCourseButton;
		addNewCourseButton = (Button)this.findViewById(R.id.addButton);
		addNewCourseButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				EditText email = (EditText)thisActivity.findViewById(R.id.editText1);
				EditText password = (EditText)thisActivity.findViewById(R.id.editText2);

				User user = User.getInstance();
				user.setEmail(email.getText().toString());
				user.setPassword(password.getText().toString());

				thisActivity.finish();
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
