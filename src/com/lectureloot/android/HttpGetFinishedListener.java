package com.lectureloot.android;

import android.util.Log;


public abstract class HttpGetFinishedListener {
	protected int threadCount;
	
	public void onHttpGetReady(String output){}	//generic case
	
	public void onHttpGetCoursesReady(String output){}

	public void onHttpGetMeetingsReady(String output){}
	
	public void onHttpGetWagersReady(String output){}
	
	public void onHttpGetSessionsReady(String output){}

	public void onHttpGetNewCourseReady(String output){}
	
	public void onHttpGetCourseListReady(String output){}

	public void onHttpGetBuildingReady(String output){}
	
	public void onHttpGetMeetingListReady(String output) {}
	
	public void onHttpGetUserReady(String output) {}
	
	public void waitForThreads(){
	/* Function will wait until all threads have executed before allowing caller to unblock */
		synchronized(this){
			do{
				try{
					wait();
				} catch (InterruptedException e){}
			} while (threadCount > 0);
		}
	}

	public void notifyThreadStart(){
			threadCount++;
		}
		
	public void notifyThreadComplete(){
		/* function will decrease the thread count and notify evreybody waiting on lock */
			threadCount--;
			Log.i("Threads:","Thread Count" + threadCount);
			synchronized(this){
				notifyAll();
			}
	}
}
