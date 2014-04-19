package com.lectureloot.background;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ExpandableListAdapter;
import android.widget.ListAdapter;

import com.lectureloot.android.HttpGetFinishedListener;
import com.lectureloot.android.adapter.ExpandableListCourseAdapter;

public abstract class HttpGet extends AsyncTask<String, Void, String> {

//	protected HttpGetCoursesFinishedListener courseListener;
//	protected HttpGetMeetingsFinishedListener meetingsListener;
//	protected HttpGetSessionsFinishedListener sessionsListener;
//	protected HttpGetWagersFinishedListener wagersListener;
	protected HttpGetFinishedListener listener;
	protected String authorizationToken;
	protected ExpandableListCourseAdapter adapter;

	
	public HttpGet(String authToken, ExpandableListCourseAdapter adapter) {
		this.authorizationToken = authToken;
		this.adapter = adapter;
	}

//	public void setHttpGetCoursesFinishedListener(HttpGetCoursesFinishedListener listener) {
//		this.courseListener = listener;
//	}
//	
//	public void setHttpGetMeetingsFinishedListener(HttpGetMeetingsFinishedListener listener) {
//		this.meetingsListener = listener;
//	}
//	
//	public void setHttpGetSessionsFinishedListener(HttpGetSessionsFinishedListener listener) {
//		this.sessionsListener = listener;
//	}
//	
//	public void setHttpGetWagersFinishedListener(HttpGetWagersFinishedListener listener) {
//		this.wagersListener = listener;
//	}
	public void setHttpGetFinishedListener(HttpGetFinishedListener listener) {
		this.listener = listener;
	}
	public void onPreExecute(){
		listener.notifyThreadStart();	//notify listnener that a new thread has starteds
	}
	
	@Override
	protected String doInBackground(String... urls) {
		String output = null;
		for (String url : urls) {
			output = getOutputFromUrl(url);
		}
		Log.i("URLGet","Returning output");
		return output;
	}

	private String getOutputFromUrl(String url) {
		StringBuffer output = new StringBuffer("");
		try {
			InputStream stream = getHttpConnection(url);
			if (stream == null){
				Log.w("HttpGet:", "Null Stream - " + url);
				return "";
			}
			BufferedReader buffer = new BufferedReader(new InputStreamReader(stream));
			String s = "";
			while ((s = buffer.readLine()) != null) {
				output.append(s);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return output.toString();
	}


	private InputStream getHttpConnection(String urlString) throws  IOException{
		InputStream stream = null;
		URL url = new URL(urlString);
		URLConnection connection = url.openConnection();

		try {
			HttpURLConnection httpConnection = (HttpURLConnection) connection;
			httpConnection.setRequestMethod("GET");
			httpConnection.setRequestProperty("Authorization", authorizationToken); //HEADER for access token
			httpConnection.setRequestProperty("Content-Type", "application/json");
			httpConnection.connect();

			System.out.println("HTTP Response Code:" + httpConnection.getResponseCode());
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
		Log.i("URLGet","Returning output(onPost)");
		returnResponse(output);
		listener.notifyThreadComplete();
		if(adapter != null) {
			adapter.notifyDataSetChanged();
		}	
	}
	
	public void setAdapter(ExpandableListCourseAdapter adapter){
		this.adapter = adapter;
	}

	public abstract void returnResponse(String output);
}
