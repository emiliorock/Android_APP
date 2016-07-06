package com.example.mybatterylevel;

import java.util.*;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	TextView tx1;
	TextView tx2;
	TextView tx3;
	TextView tx4;
	Button btn1;
	Button btn2;
	Button btn3;
	BroadcastReceiver receiver;
	IntentFilter filter;
	Timer timer = new Timer();
	int last = 0;
	int l1 = 0;
	int l2 = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tx1 = (TextView) findViewById(R.id.textView1);
		tx2 = (TextView) findViewById(R.id.textView2);
		tx3 = (TextView) findViewById(R.id.textView3);
		tx4 = (TextView) findViewById(R.id.textView4);
		btn1 = (Button) findViewById(R.id.button1);
		btn2 = (Button) findViewById(R.id.button2);
		btn3 = (Button) findViewById(R.id.button3);

		// task 2, wifi
		btn2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				batteryst();
				tx3.setText("Initial level of battery: " + last + "%");
				timer.schedule(new CheckWifi(), 60 * 10000, 60 * 10000);
			}
		});

		// task 2, gps
		btn3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				batteryst();
				tx3.setText("Initial level of battery: " + last + "%");
				LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
				Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				if(locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)) {
					;		
				}
				else {				
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
					alertDialog.setTitle("Setting the GPS");
					alertDialog.setItems(new String[] {"GPS is not enabled. Do you want to go to settings menu? "}, null);
					alertDialog.setPositiveButton("cancel", null);
					alertDialog.setNegativeButton("settings", null);
					Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					alertDialog.show();
					startActivityForResult(intent,0);
				}
				timer.schedule(new CheckGPS(), 60 * 10000, 60 * 10000);
			}
		});

		// task 1
		btn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				BatteryManager manager = new BatteryManager();
				if (manager.BATTERY_PLUGGED_USB != 2) {
					tx2.setText("Moblie is not charging");
				} 
				if (manager.BATTERY_PLUGGED_USB == 2){
					tx2.setText("Moblie is charging via USB");
				}
				// check the current status of the battery
				batteryst();
			}
		});
	}

	public void batteryst() {
		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				context.unregisterReceiver(this);

				int rawlevel = intent.getIntExtra("level", -1);
				int scale = intent.getIntExtra("scale", -1);
				int level = -1;
				if (rawlevel >= 0 && scale > 0) {
					level = (rawlevel * 100) / scale;
				}
				last = level;
				l1 = level;
				l2 = level;
				tx1.setText("Current level of battery is: " + level + "%");
			}
		};
		Intent batteryStatus;
		filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(receiver, filter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	class CheckWifi extends TimerTask {

		@Override
		public void run() {
			WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
			if (!wifiManager.isWifiEnabled()) {
				if (wifiManager.getWifiState() != WifiManager.WIFI_STATE_ENABLING) {
					wifiManager.setWifiEnabled(true);
				}
			}
			wifiManager.startScan();
			List<ScanResult> list = wifiManager.getScanResults();
			receiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					context.unregisterReceiver(this);
					int rawlevel = intent.getIntExtra("level", -1);
					int scale = intent.getIntExtra("scale", -1);
					int level = -1;
					if (rawlevel >= 0 && scale > 0) {
						level = (rawlevel * 100) / scale;
					}
					int d = l1 - level;
					tx4.setText("Wifi consumed battery:" + d + "%");
				}
			};
			Intent batteryStatus;
			filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
			registerReceiver(receiver, filter);
		}
	}

	class CheckGPS extends TimerTask {

		@Override
		public void run() {
			receiver = new BroadcastReceiver(){
				@Override
				public void onReceive(Context context, Intent intent) {
					context.unregisterReceiver(this);
					int rawlevel = intent.getIntExtra("level", -1);
					int scale = intent.getIntExtra("scale", -1);
					int level = -1;
					if (rawlevel >= 0 && scale > 0) {
						level = (rawlevel * 100) / scale;
					}
					int d = l2 - level;
					tx4.setText("GPS consumed battery:" + d + "%");
					}
				};
			Intent batteryStatus;
			filter=new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
			registerReceiver(receiver, filter);		
		}
	}
	
}
