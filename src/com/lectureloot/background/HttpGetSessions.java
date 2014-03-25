package com.lectureloot.background;

public class HttpGetSessions extends HttpGet {

	
	public HttpGetSessions(String authToken){
		super(authToken, null);
	}
	public void returnResponse(String output) {
		listener.onHttpGetSessionsReady(output);
	}

}
