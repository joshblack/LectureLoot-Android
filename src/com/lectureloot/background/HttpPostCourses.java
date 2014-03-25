package com.lectureloot.background;

public class HttpPostCourses extends HttpPost{
	
	public HttpPostCourses(String authToken){
		super(authToken);
	}
	@Override
	public void returnResponse(String output) {
		courseListener.onHttpPostCoursesReady(output);		
	}
	
}
