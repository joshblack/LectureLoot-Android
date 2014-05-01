package com.lectureloot.android;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.TextView;


public class TermsOfServiceActivity extends Activity {
	private TextView tosTextView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_terms_of_service);

		tosTextView = (TextView) findViewById(R.id.terms_of_service_text_view);
		String tos="";

		AssetManager assetManager = getResources().getAssets();
		InputStream inputStream = null;

		try {
			inputStream = assetManager.open("tos.txt");
			if ( inputStream != null) {
				//		            	tosTextView.setText("It worked");
				String line = null;
				java.util.Scanner s = new java.util.Scanner(inputStream).useDelimiter("\\A");
				while(s.hasNext())
				{
					tos+=(s.next());
					tos+=("\n");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		tosTextView.setText(tos);
	}
}
