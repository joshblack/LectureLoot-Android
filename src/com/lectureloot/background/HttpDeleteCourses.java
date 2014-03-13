package com.lectureloot.background;

public class HttpDeleteCourses extends HttpDelete {

	
	public HttpDeleteCourses(String authToken){
		super(authToken);
	}
	@Override
	public void returnResponse(String output) {
		courseListener.onHttpDeleteCoursesReady(output);

		
	}

}
