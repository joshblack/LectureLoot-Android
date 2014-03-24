package com.lectureloot.background;

public class HttpGetCourseList extends HttpGet {

	
	public HttpGetCourseList(String authToken){
		super(authToken);
	}
	
	public void returnResponse(String output) {
		listener.onHttpGetCourseListReady(output);
	}

}
