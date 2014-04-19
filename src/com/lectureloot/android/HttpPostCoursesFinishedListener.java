package com.lectureloot.android;

import android.util.Log;

public abstract class HttpPostCoursesFinishedListener {
	protected int threadCount;
	
	public void onHttpPostCoursesReady(String output){}	//do nothing
	
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
