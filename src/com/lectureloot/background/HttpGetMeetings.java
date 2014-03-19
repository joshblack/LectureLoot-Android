package com.lectureloot.background;

public class HttpGetMeetings extends HttpGet {

	public HttpGetMeetings(String authToken){
		super(authToken);
	}
	
	
	public void returnResponse(String output) {		
		listener.onHttpGetMeetingsReady(output);
		listener.notifyThreadComplete();
	}

}
