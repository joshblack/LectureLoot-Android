package com.lectureloot.android;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;


public class SplashActivity extends Activity {	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}
	
	protected void onStart(){
		System.out.println("getting user isntance");
		
		//wait for user to finish it's stuff
		while(User.getInstance().isBusy());
		finish();		
	}

}
