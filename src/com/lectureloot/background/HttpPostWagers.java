package com.lectureloot.background;

public class HttpPostWagers extends HttpPost {

	public HttpPostWagers(String authToken){
		super(authToken);
	}
	@Override
	public void returnResponse(String output) {
		wagerListener.onHttpPostWagersReady(output);
	}
	
}
