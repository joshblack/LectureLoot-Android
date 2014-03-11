package com.lectureloot.android;

public interface HttpGetFinishedListener {

	public void onHttpGetCoursesReady(String output);

	public void onHttpGetMeetingsReady(String output);
}
