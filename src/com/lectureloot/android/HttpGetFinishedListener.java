package com.lectureloot.android;

public abstract class HttpGetFinishedListener {
	protected int threadCount;
	
	public void onHttpGetCoursesReady(String output){}

	public void onHttpGetMeetingsReady(String output){}
	
	public void onHttpGetWagersReady(String output){}
	
	public void onHttpGetSessionsReady(String output){}

	public void onHttpGetCourseListReady(String output){}

	public void onHttpGetCourseReady(String output){}
	
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
			synchronized(this){
				notifyAll();
			}
		}
}
