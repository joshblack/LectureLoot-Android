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

import android.os.AsyncTask;
import android.view.View.OnClickListener;

public abstract class HttpPost extends AsyncTask<String, Void, String> {
	
	protected HttpPostCoursesFinishedListener courseListener;
	protected HttpPostWagersFinishedListener wagerListener; 
	protected String authorizationToken;
	
	public HttpPost(String authToken) {
		this.authorizationToken = authToken;
	}

	public void setHttpPostCoursesFinishedListener(OnClickListener onClickListener) {
		this.courseListener = (HttpPostCoursesFinishedListener) onClickListener;
	}
	
	public void setHttpPostWagersFinishedListener(HttpPostWagersFinishedListener listener) {
		this.wagerListener = listener;
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

		System.out.println("response:" + output);
		//returnResponse(output);
	}

	public abstract void returnResponse(String output);
	
}
