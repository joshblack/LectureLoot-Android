package com.lectureloot.android;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;


public class SplashActivity extends Activity {	
	private Thread workThread = new Thread(new Runnable(){
		public void run(){
			User.getInstance().loadUserData(false);
			finish();
		}
	});
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash1);
		Log.i("Splash:","Created Splash Screen");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}
	
	protected void onResume(){
		super.onResume();
		Log.i("Splash:","Getting User Data");
		
			workThread.start();
	}
	
	@Override
	public void onBackPressed(){
		//do nothing (Back button is disabled)
	}
}
