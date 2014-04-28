package com.lectureloot.background;

import android.util.Log;

public class HttpPostCheckin extends HttpPost {

	public HttpPostCheckin(String authToken) {
		super(authToken);
	}
	
	@Override
	public void returnResponse(String output) {
		Log.i("Http Post:", "Returning Response");
		checkinListener.onHttpPostCheckinReady(output);

	}

}
