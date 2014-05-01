package com.lectureloot.android;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.TextView;

public class FAQActivity extends Activity {
	private TextView faqTextView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_faq);

		faqTextView = (TextView) findViewById(R.id.faq_text_view);
		String faq="";

		AssetManager assetManager = getResources().getAssets();
		InputStream inputStream = null;

		try {
			inputStream = assetManager.open("faq.txt");
			if ( inputStream != null) {
				java.util.Scanner s = new java.util.Scanner(inputStream).useDelimiter("\\A");
				while(s.hasNext())
				{
					faq+=(s.next());
					faq+=("\n");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		faqTextView.setText(faq);
	}
}
