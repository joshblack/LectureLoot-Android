package com.lectureloot.background;

import android.util.Log;

public class HttpGetCourseList extends HttpGet {

	
	public HttpGetCourseList(String authToken){
		super(authToken, null);
	}
	
	@Override
	public void returnResponse(String output) {
		Log.i("HttpGetCourseList:","Returning Output");
		listener.onHttpGetCourseListReady(output);
	}

}
