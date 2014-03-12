package com.lectureloot.background;

public class HttpGetSessions extends HttpGet {

	
	public HttpGetSessions(String authToken){
		super(authToken);
	}
	@Override
	public void returnResponse(String output) {
		sessionsListener.onHttpGetSessionsReady(output);

		
	}

}