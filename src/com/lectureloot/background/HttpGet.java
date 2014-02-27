package com.lectureloot.background;

import android.os.AsyncTask;
import com.lectureloot.android.HttpGetFinishedListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public abstract class HttpGet extends AsyncTask<String, Void, String> {

	protected HttpGetFinishedListener listener;
	protected String authorizationToken;
	
	public HttpGet(String authToken) {
		this.authorizationToken = authToken;
	}

	public void setHttpGetFinishedListener(HttpGetFinishedListener listener) {
		this.listener = listener;
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
		// TODO: Test data used for stubs until we can get JSON data from lectureloot REST API
		//		String testString = "[ { \"id\": 1, \"deptCode\": \"CEN\", \"courseNumber\": \"3031\", \"sectionNumber\":  \"11F8\", \"credits\": \"3\", \"instructor\": \"Bermudez, Manuel E\", \"courseTitle\":\"INTRO SOFTWARE ENGR\"} , "; 
		//		testString += "{ \"id\": 2, \"deptCode\": \"CAP\", \"courseNumber\": \"4053\", \"sectionNumber\":  \"133E\", \"credits\": \"3\", \"instructor\": \"Anthony,Lisa, Dankel,Douglas D,II\", \"courseTitle\":\"AI FOR COMPUTER GAMES\" } ]";




		System.out.println("response:" + output);
		//			System.out.println("onPostExecute logger");
		//			listener.onHttpGetCoursesReady(output);
		returnResponse(output);
	}

	public abstract void returnResponse(String output);
}
