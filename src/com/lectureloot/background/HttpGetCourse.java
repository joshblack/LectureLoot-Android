package com.lectureloot.background;

import com.lectureloot.android.adapter.ExpandableListCourseAdapter;

public class HttpGetCourse extends HttpGet {

	
	public HttpGetCourse(String authToken, ExpandableListCourseAdapter adapter){
		super(authToken, adapter);
	}
	
	public void returnResponse(String output) {
		listener.onHttpGetCourseReady(output);
	}

}
