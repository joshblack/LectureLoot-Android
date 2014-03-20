package com.lectureloot.background;

public class HttpPutWagers extends HttpPut {

	public HttpPutWagers(String authToken){
		super(authToken);
	}
	@Override
	public void returnResponse(String output) {
		wagerListener.onHttpPutWagersReady(output);
	}
	
}
