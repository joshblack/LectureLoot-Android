package com.lectureloot.background;

public class HttpGetUser extends HttpGet {

	
	public HttpGetUser(String authToken){
		super(authToken, null);
	}
	public void returnResponse(String output) {
		listener.onHttpGetUserReady(output);
	}

}
