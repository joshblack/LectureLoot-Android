package com.lectureloot.background;

public class HttpGetCourses extends HttpGet {

	
	public HttpGetCourses(){
	}
	@Override
	public void returnResponse(String output) {
		listener.onHttpGetCoursesReady(output);

		
	}

}
