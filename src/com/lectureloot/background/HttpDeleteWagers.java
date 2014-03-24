package com.lectureloot.background;

public class HttpDeleteWagers extends HttpDelete {
	
	public HttpDeleteWagers(String authToken){
		super(authToken);
	}
	@Override
	public void returnResponse(String output) {
		wagerListener.onHttpDeleteWagersReady(output);

		
	}

}
