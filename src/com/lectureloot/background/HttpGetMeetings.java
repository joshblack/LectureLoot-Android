package com.lectureloot.background;

public class HttpGetMeetings extends HttpGet {

	public HttpGetMeetings(String authToken){
		super(authToken);
	}
	
	@Override
	public void returnResponse(String output) {
		String testString = "[ { \"id\": 1,\"course_id\":\"1\",\"buildingCode\":\"LIT\",\"roomNumber\":\"109\",\"meetingDay\":\"f\",\"period\":\"6\"} ,  "; 
		testString += "{ \"id\": 1,\"course_id\":\"1\",\"buildingCode\":\"LIT\",\"roomNumber\":\"109\",\"meetingDay\":\"m\",\"period\":\"6\"} ]";
		
		meetingsListener.onHttpGetMeetingsReady(output);

	}

}
