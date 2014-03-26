package com.lectureloot.background;

public class HttpGetWagers extends HttpGet {

	
	public HttpGetWagers(String authToken){
		super(authToken, null);
	}
		
	public void returnResponse(String output) {
		listener.onHttpGetWagersReady(output);

	}

}
