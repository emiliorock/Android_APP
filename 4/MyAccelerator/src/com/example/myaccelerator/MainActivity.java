package com.example.myaccelerator;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

	SensorManager SensorManager;
	Sensor aSensor;
	Sensor gSensor;
	String xa;
	String ya;
	String za;
	String xg;
	String yg;
	String zg;
	String str1 = "";
	String str2 = "";
	
	TextView tx;
	TextView tx2;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		SensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		aSensor = SensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);	
		gSensor = SensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
		tx = (TextView) findViewById(R.id.textView1);
		tx2 = (TextView) findViewById(R.id.textView2);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		
		//task2
		if(event.sensor == aSensor) {
			double ax = event.values[0];
			double ay = event.values[1];
			double az = event.values[2];
			xa = "X: " + String.valueOf(ax) + "\n";
			ya = "Y: " + String.valueOf(ay) + "\n";
			za = "Z: " + String.valueOf(az) + "\n";
			str1 = "Acceleration force without gravity\n" + xa + ya + za;
		}
		if(event.sensor == gSensor) {
			double gx = event.values[0];
			double gy = event.values[1];
			double gz = event.values[2];
			xg = "X: " + String.valueOf(gx) + "\n";
			yg = "Y: " + String.valueOf(gy) + "\n";
			zg = "Z: " + String.valueOf(gz) + "\n";
			str2 = "Acceleration force including gravity\n" + xg + yg + zg;
		}
		tx.setText(str1 + "\n" + str2);	
		
		/*
		// task 3
		if(event.sensor == aSensor) {
			double ax = event.values[0];
			double ay = event.values[1];
			double az = event.values[2];
			// on the table
			if(ax < 1.5 && ax > -1.5 && ay < 1.5 && ay > -1.5 && az > 8 && az < 10) {
				tx.setText("Orientation of phone");
				tx2.setText("On the table");
			}
			// default
			else if(ax < 1.5 && ax > -1.5 && ay > 8 && ay < 10) {
				tx.setText("Orientation of phone");
				tx2.setText("Default");
			}
			// upside down
			else if(ax < 1.5 && ax > -1.5 && az < 1.5 && az > -1.5 && ay > -10 && ay < -8) {
				tx.setText("Orientation of phone");
				tx2.setText("Upside Down");
			}
			// left
			else if(ax < 10 && ax > 8 && az < 1.5 && az > -1.5 && ay > -1.5 && ay < 1.5) {
				tx.setText("Orientation of phone");
				tx2.setText("Left");
			}
			// right
			else if(ax < -8 && ax > -10 && az < 1.5 && az > -1.5 && ay > -1.5 && ay < 1.5) {
				tx.setText("Orientation of phone");
				tx2.setText("Right");
			}
			else {
				;
			}
		}
		*/
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onResume() {
	    super.onResume();
	    SensorManager.registerListener(this, aSensor, SensorManager.SENSOR_DELAY_NORMAL);
	    SensorManager.registerListener(this, gSensor, SensorManager.SENSOR_DELAY_NORMAL);
	  }
	
	@Override
	protected void onPause() {
	    super.onPause();
	    SensorManager.unregisterListener(this);
	  }

}