package com.lectureloot.background;

public class HttpGetCourse extends HttpGet {

	
	public HttpGetCourse(String authToken){
		super(authToken);
	}
	
	public void returnResponse(String output) {
		listener.onHttpGetCourseReady(output);
	}

}
