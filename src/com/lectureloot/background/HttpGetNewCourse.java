package com.lectureloot.background;

import com.lectureloot.android.adapter.ExpandableListCourseAdapter;

public class HttpGetNewCourse extends HttpGet {

	
	public HttpGetNewCourse(String authToken, ExpandableListCourseAdapter adapter){
		super(authToken, adapter);
	}

	public void returnResponse(String output) {
		listener.onHttpGetNewCourseReady(output);
	}

}
