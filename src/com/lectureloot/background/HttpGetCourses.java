package com.lectureloot.background;

import com.lectureloot.android.adapter.ExpandableListCourseAdapter;

public class HttpGetCourses extends HttpGet {

	
	public HttpGetCourses(String authToken, ExpandableListCourseAdapter adapter){
		super(authToken, adapter);
	}

	public void returnResponse(String output) {

		listener.onHttpGetCoursesReady(output);

	}

}
