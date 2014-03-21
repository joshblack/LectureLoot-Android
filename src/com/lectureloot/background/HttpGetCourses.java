package com.lectureloot.background;

public class HttpGetCourses extends HttpGet {

	
	public HttpGetCourses(String authToken){
		super(authToken);
	}

	public void returnResponse(String output) {
		listener.onHttpGetCoursesReady(output);
	}

}
