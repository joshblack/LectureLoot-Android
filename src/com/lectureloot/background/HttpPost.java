package com.lectureloot.background;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import com.lectureloot.android.HttpPostCoursesFinishedListener;
import com.lectureloot.android.HttpPostWagersFinishedListener;
import com.lectureloot.android.HttpPostCheckinFinishedListener;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View.OnClickListener;

public abstract class HttpPost extends AsyncTask<String, Void, String> {
	
	protected HttpPostCoursesFinishedListener courseListener;
	protected HttpPostWagersFinishedListener wagerListener; 
	protected HttpPostCheckinFinishedListener checkinListener;
	protected String authorizationToken;
	
	public HttpPost(String authToken) {
		this.authorizationToken = authToken;
		courseListener = null;
		wagerListener = null;
	}

	public void setHttpPostCoursesFinishedListener(HttpPostCoursesFinishedListener listener) {
		this.courseListener = (HttpPostCoursesFinishedListener) listener;
	}
	
	public void setHttpPostWagersFinishedListener(HttpPostWagersFinishedListener listener) {
		this.wagerListener = listener;
	}
	
	public void setHttpPostCheckinFinishedListener(HttpPostCheckinFinishedListener listener) {
		this.checkinListener = listener;
	}

	@Override
	public void onPreExecute(){
		if(courseListener != null)courseListener.notifyThreadStart();
	}
	
	@Override
	protected String doInBackground(String... urls) {
		//	android.os.Debug.waitForDebugger();
		String output = null;
		for (String url : urls) {
			output = getOutputFromUrl(url);
		}
		return output;
	}
	
	private String getOutputFromUrl(String url) {
		StringBuffer output = new StringBuffer("");
		try {
			InputStream stream = getHttpConnection(url);
			BufferedReader buffer = new BufferedReader(new InputStreamReader(stream));
			String s = "";
			while ((s = buffer.readLine()) != null) {
				output.append(s);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (Exception e){
			Log.w("HttpPost:",e.toString());
			Log.w("HttpPost:",url);
		}
		return output.toString();
	}
	
	private InputStream getHttpConnection(String urlString) throws  IOException{
		InputStream stream = null;
		URL url = new URL(urlString);
		URLConnection connection = url.openConnection();

		try {
			HttpURLConnection httpConnection = (HttpURLConnection) connection;
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Authorization", authorizationToken); //HEADER for access token
			httpConnection.setRequestProperty("Content-Type", "application/json");
			httpConnection.connect();

			//System.out.println("HTTP Response Code:" + httpConnection.getResponseCode());
			if(httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				stream = httpConnection.getInputStream();
			} 
		} catch (Exception ex) {
			ex.printStackTrace();
			//			System.out.println(ex.getMessage());
		}
		return stream;
	}

	@Override
	protected void onPostExecute(String output) {
		//System.out.println("response:" + output);
		returnResponse(output);
		if(courseListener != null)courseListener.notifyThreadComplete();
	}

	public abstract void returnResponse(String output);
	
}
