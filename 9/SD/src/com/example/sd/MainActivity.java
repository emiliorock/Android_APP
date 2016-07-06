package com.example.sd;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity
{
	Button write;
	Button read;
	EditText input;
	TextView output;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		

		Button write = (Button) findViewById(R.id.button);
		Button read = (Button) findViewById(R.id.button2);
		final EditText input = (EditText) findViewById(R.id.editText);
		final TextView output = (TextView) findViewById(R.id.textView);

		write.setOnClickListener(new View.OnClickListener()
		{

			public void onClick(View v)
			{

				SharedPreferences mySharedPreferences = getSharedPreferences(
						"test", Activity.MODE_PRIVATE);
				SharedPreferences.Editor editor = mySharedPreferences.edit();

				String data = input.getText().toString();
				System.out.println("data: " + data);
				editor.putString("data: ", data);

				editor.commit();
			}
		});

		read.setOnClickListener(new View.OnClickListener()
		{

			public void onClick(View v)
			{
				SharedPreferences sharedPreferences = getSharedPreferences(
						"test", Activity.MODE_PRIVATE);
				String data = sharedPreferences.getString("data: ", "");
				output.setText(data);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings)
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
