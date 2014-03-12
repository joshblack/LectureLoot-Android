package com.lectureloot.background;

public class HttpGetCourses extends HttpGet {

	
	public HttpGetCourses(String authToken){
		super(authToken);
	}
	@Override
	public void returnResponse(String output) {
		listener.onHttpGetCoursesReady(output);

		
	}

}
