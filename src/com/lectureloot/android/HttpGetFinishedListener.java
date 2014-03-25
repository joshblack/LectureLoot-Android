package com.lectureloot.android;

import android.util.Log;

public abstract class HttpGetFinishedListener {
	protected int threadCount;
	
	public void onHttpGetCoursesReady(String output){}

	public void onHttpGetMeetingsReady(String output){}
	
	public void onHttpGetWagersReady(String output){}
	
	public void onHttpGetSessionsReady(String output){}

	public void onHttpGetCourseListReady(String output){}

	public void onHttpGetCourseReady(String output){}
	
	public void onHttpGetBuildingReady(String output){}
	
	public void waitForThreads(){
	/* Function will wait until all threads have executed before allowing caller to unblock */
		synchronized(this){
			do{
				Log.i("Thread:","Waiting...");
				try{
					wait();
				} catch (InterruptedException e){}
			} while (threadCount > 0);
		}
	}

	public void notifyThreadStart(){
			Log.i("Thread:","Started, Count: " + threadCount);
			threadCount++;
		}
		
	public void notifyThreadComplete(){
		/* function will decrease the thread count and notify evreybody waiting on lock */
			threadCount--;
			synchronized(this){
				Log.i("Thread:","Thread Finished, Count:" + threadCount);
				notifyAll();
			}
		}
}
