package com.lectureloot.background;

import android.util.Log;

public class HttpPostWagers extends HttpPost {

	public HttpPostWagers(String authToken){
		super(authToken);
	}
	@Override
	public void returnResponse(String output) {
		Log.i("Http Post:", "Returning Response");
		wagerListener.onHttpPostWagersReady(output);
	}
	
}
