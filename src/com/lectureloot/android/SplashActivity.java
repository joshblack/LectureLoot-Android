package com.lectureloot.android;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;


public class SplashActivity extends Activity {
	public static SplashActivity splashScreen;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		splashScreen = this;
		
		setContentView(R.layout.activity_splash1);
		
		System.out.println("getting user isntance");
		do{
			try{
				synchronized(MainActivity.mContext){
					MainActivity.mContext.wait();
				}
			} catch (InterruptedException e){}
		}while(User.getInstance().isBusy());

		finish();		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}

}
