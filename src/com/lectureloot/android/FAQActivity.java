package com.lectureloot.android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class FAQActivity extends Activity {
	private TextView faqTextView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_faq);

		faqTextView = (TextView) findViewById(R.id.faq_text_view);
		faqTextView.setText("Frequently Asked Questions: \n1. Question 1 \n2. Question 2 \n3. Question 3 \n4. Question 4"); 
	}
}
