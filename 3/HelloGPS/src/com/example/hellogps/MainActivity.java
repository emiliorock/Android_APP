package com.example.hellogps;

import android.support.v7.app.ActionBarActivity;
import java.sql.Date;
import java.text.SimpleDateFormat;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
		final Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		
		Button Btn1 = (Button) findViewById(R.id.button1);
		Btn1.setOnClickListener(new
				View.OnClickListener() {
					public void onClick(View v) {
						myClick(v); 
					}
					public void myClick(View v) {
						if(locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)) {
							TextView txCounter = (TextView) findViewById(R.id.textView1);
							txCounter.setText("GPS is active");		
						}
						else {
							TextView tx1 = (TextView) findViewById(R.id.textView1);
							tx1.setText("GPS is not active");
							
							AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
						
							alertDialog.setTitle("Setting the GPS");
							alertDialog.setItems(new String[] {"GPS is not enabled. Do you want to go to settings menu? "}, null);
							alertDialog.setPositiveButton("cancel", null);
							alertDialog.setNegativeButton("settings", null);
							Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
							alertDialog.show();
							startActivityForResult(intent,0);
						}
					} 
		});
		
		
		/*
		Button Btn2 = (Button) findViewById(R.id.button2);
		Btn2.setOnClickListener(new
				View.OnClickListener() {
					public void onClick(View v) {
						myclick(v);
					}
					public void myclick(View v) {
						
		*/				
						
							GPS(location);
						
							locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 50, 1, new LocationListener() {
							
								public void onLocationChanged(Location location) {
									GPS(location);
								}

								@Override
								public void onStatusChanged(String provider, int status, Bundle extras) {
									// TODO Auto-generated method stub
								}

								@Override
								public void onProviderEnabled(String provider) {
									// TODO Auto-generated method stub
									GPS(locationManager.getLastKnownLocation(provider));
								}

								@Override
								public void onProviderDisabled(String provider) {
									// TODO Auto-generated method stub
									GPS(null);
								}
							});			
					}
						
		//});
	//}

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

	public void GPS(Location location) {
		if(location != null) {
			
			String str = new String();
			str = "Date/Time: ";
			
			long time = location.getTime(); 
			Date date = new Date(time);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String textofDate = sdf.format(date);
			str = str + textofDate + "\nProvider: network\nAccuracy: ";
			String temp = String.valueOf(location.getAccuracy());
			
			str = str + temp + "\nLongitude: ";
			temp = Double.toString(location.getLongitude());
		
			str = str + temp + "\nLatitude: ";
			temp = Double.toString(location.getLatitude());
		
			str = str + temp + "\nSpeed: ";
			temp = String.valueOf(location.getSpeed());
		
			str = str + temp;
			
			TextView tx2 = (TextView) findViewById(R.id.textView2);
			tx2.setText(str);
		}
		else if(location == null) {
			TextView tx2 = (TextView) findViewById(R.id.textView2);
			tx2.setText("No info");
		}
	}
}