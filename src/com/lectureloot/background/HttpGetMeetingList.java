package com.lectureloot.background;

public class HttpGetMeetingList extends HttpGet {

	public HttpGetMeetingList(String authToken){
		super(authToken, null);
	}
	
	public void returnResponse(String output) {		
		listener.onHttpGetMeetingListReady(output);
	}

}
