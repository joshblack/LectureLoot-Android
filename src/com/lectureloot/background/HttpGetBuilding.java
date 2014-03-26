package com.lectureloot.background;

public class HttpGetBuilding extends HttpGet {

	
	public HttpGetBuilding(String authToken){
		super(authToken, null);
	}
	
	public void returnResponse(String output) {
		listener.onHttpGetBuildingReady(output);
	}

}
