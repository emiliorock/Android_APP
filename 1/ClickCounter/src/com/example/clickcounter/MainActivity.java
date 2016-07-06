package com.example.clickcounter;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	int n = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button clickMeBtn = (Button) findViewById(R.id.button1);
		clickMeBtn.setOnClickListener(new
				View.OnClickListener() {
					public void onClick(View v) {
						myClick(v); /* my method to call new intent or activity */
					}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void myClick(View v) {
		// Write your own code
		n++;
		String str = Integer.toString(n);
		TextView txCounter = (TextView) findViewById(R.id.textView1);
		txCounter.setText(str);		
	}
}