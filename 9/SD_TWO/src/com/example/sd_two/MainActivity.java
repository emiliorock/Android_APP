package com.example.sd_two;

import android.app.Activity;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.v7.app.*;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity
{

	GPSReader GP = new GPSReader(this);
	private WifiManager wifiManager;
	private Button check, write, show;
	private static final String FILENAME = "9336.txt";
	@SuppressWarnings("unchecked")
	private List<String> total = new ArrayList();

	TextView report;
	ListView list;
	TextView test;
	List<ScanResult> signals;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
		Button check = (Button) findViewById(R.id.button);
		Button write = (Button) findViewById(R.id.button2);
		Button show = (Button) findViewById(R.id.button3);
		final TextView report = (TextView) findViewById(R.id.textView);
		list = (ListView) findViewById(R.id.listView);
		test = (TextView) findViewById(R.id.textView1);
		signals = wifiManager.getScanResults();
		
		write.setOnClickListener(new MySetOnClickListener());
		show.setOnClickListener(new MySetOnClickListener());

		check.setOnClickListener(new OnClickListener()
		{

			public void onClick(View v)
			{
				if (isExternalStorageWritable())
				{
					report.setTextSize(20);
					report.setText("The external storage is available for read and write."
							+ "\n"
							+ "The capacity of the storage is "
							+ getSDTotalSize());
				} else if (isExternalStorageReadable())
				{
					report.setTextSize(20);
					report.setText("The external storage is available to at least read."
							+ "\n"
							+ "The capacity of the storage is "
							+ getSDTotalSize());
				}
			}
		});
	}

	private class MySetOnClickListener implements OnClickListener
	{

		@Override
		public void onClick(View v)
		{
			File file = new File(Environment.getExternalStorageDirectory(), FILENAME);
			switch (v.getId())
			{
			case R.id.button2:
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
				{
					try
					{
						GP.getLocation();
						SimpleDateFormat sDateFormat = new SimpleDateFormat(
								"yyyy-MM-dd    hh:mm:ss");
						String date = sDateFormat.format(new java.util.Date());
						List<String> total = new ArrayList();
						
						
						for (int i = 0; i < wifiManager.getScanResults().size(); i++)
						{
							total.add("Date/Time: " + date + "\n" + GP.s.get(0)
									+ "\n" + GP.s.get(1) + "\n"
									+ wifiManager.getScanResults().get(i));
						}

						FileOutputStream fos = new FileOutputStream(file);
						String a = Pattern
								.compile("\\b([\\w\\W])\\b")
								.matcher(total.toString().
										substring(1,total.toString().length() - 1)).replaceAll("$1");
						String ap = a.replaceAll(",", "");
						fos.write(ap.toString().getBytes());
						fos.close();
						Toast.makeText(MainActivity.this, "Write Success",Toast.LENGTH_LONG).show();
						
					} 
					catch (Exception e)
					{
						Toast.makeText(MainActivity.this, "Write Fail",	Toast.LENGTH_SHORT).show();
					}
				} 
				else
				{
					// SDcard is not exist or can not read and write
					Toast.makeText(
							MainActivity.this,
							"SDcard does not exist or cannot be read and write operations",
							Toast.LENGTH_SHORT).show();
				}
				break;

			case R.id.button3:// use SDcard do something
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
				{
					try
					{
						file = new File(
								Environment.getExternalStorageDirectory(),
								"9336.txt");
						BufferedReader br = new BufferedReader(new FileReader(
								file));
						String readline = "";
						StringBuffer sb = new StringBuffer();
						List<String> output = new ArrayList();
						int line = 1;

						while ((readline = br.readLine()) != null)
						{
							sb.append(readline + "\n");
							if (line % 6 == 0)
							{
								output.add(sb.toString());
								sb = new StringBuffer();
							}
							line++;
						}
						br.close();
						Toast.makeText(MainActivity.this, "Read Success",
								Toast.LENGTH_LONG).show();

						write(output);
					} 
					catch (Exception e)
					{
						e.printStackTrace();
						Toast.makeText(MainActivity.this, "Read Fail",	Toast.LENGTH_SHORT).show();
					}
				} else
				{
					// SDcard is not exist or can not read and write
					Toast.makeText(
							MainActivity.this,
							"SDcard does not exist or cannot be read and write operations",
							Toast.LENGTH_SHORT).show();
				}
				break;
			}

		}
	}

	public void write(List<String> output)
	{

		list.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, output));

	}

	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable()
	{
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state))
		{
			return true;
		}
		return false;
	}

	/* Checks if external storage is available to at least read */
	public boolean isExternalStorageReadable()
	{
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)
				|| Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))
		{
			return true;
		}
		return false;
	}

	public String getSDTotalSize()
	{
		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return Formatter.formatFileSize(MainActivity.this, blockSize * totalBlocks);
	}
}
