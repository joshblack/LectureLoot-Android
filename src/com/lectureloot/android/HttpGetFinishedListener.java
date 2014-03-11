package com.lectureloot.android;

public interface HttpGetFinishedListener {

	public void onHttpGetCoursesReady(String output);

	public void onHttpGetMeetingsReady(String output);
	
	public void onHttpGetWagersReady(String output);
}
