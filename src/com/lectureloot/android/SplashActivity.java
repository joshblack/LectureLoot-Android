package com.lectureloot.android;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;


public class SplashActivity extends Activity {	
	private Thread workThread = new Thread(new Runnable(){
		public void run(){
			/* Check Use Cases:
			 * If the user isn't logged in yet, don't load, but don't exit either
			 * If the user was already loaded, don't do it again
			 * Otherwise, load the user data	*/
			User user = User.getInstance();
			if(user.loggedIn()){
				if(!user.loaded()) user.loadUserData(false);
				finish();
			}
		}
	});
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash1);
		
		if((User.DEBUG_MODE & 1) != 0) Log.i("Load Debug:","Created Splash Screen");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}
	
	protected void onResume(){
		super.onResume();
		
		if((User.DEBUG_MODE & 1) != 0) Log.i("Load Debug:","Splash: Getting User Data");
		
		workThread.start();
	}
	
	@Override
	public void onBackPressed(){
		//do nothing (Back button is disabled)
	}
}
