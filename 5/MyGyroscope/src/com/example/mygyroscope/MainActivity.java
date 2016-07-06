package com.example.mygyroscope;

import android.app.Activity;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

	LocationManager locationManager;
	Location location;
	
	SensorManager SensorManager;
	Sensor gsensor;
	Sensor osensor;
	Sensor msensor;
	
	GeomagneticField geoField;
	
	double xx = 0;
	double yy = 0;
	double zz = 0;
	double zvalue = 0;
	double direction = 0;
	
	String x = "";
	String y = "";
	String z = "";
	String str = "";
	
	TextView tx;
	Button btn1;
	Button btn2;
	
	float NS2S = 1.0f / 1000000000.0f; 
	float timestamp;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		SensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		gsensor = SensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		osensor = SensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		msensor = SensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		
		locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
		
		SensorManager.registerListener(this, osensor, SensorManager.SENSOR_DELAY_NORMAL);
		
		tx = (TextView) findViewById(R.id.textView1);
		btn1 = (Button) findViewById(R.id.button1);
		btn2 = (Button) findViewById(R.id.button2);
		btn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onResume();
			}
		});
		btn2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onPause();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		
		// task 3 : geomagnetic field strength along the x, y and z axis
		if(event.sensor == msensor) {
			/*
			xx = event.values[0];
			yy = event.values[1];
			zz = event.values[2];
			x = String.valueOf(xx);
			y = String.valueOf(yy);
			z = String.valueOf(zz);
			str = "Geomagnetic field strength: \nx: " + x + "\ny: " + y + "\nz: " + z;
			*/
		}
		
		
		// task 4 & 5: heading from magnetometer x and y values while holding the phone horizontal
		// true north heading without declination angle
		else if(event.sensor == osensor) {
			
			xx = event.values[0];
			yy = event.values[1];
			if(xx > 0) {
				direction = 270 + Math.atan(yy / xx) * 180 / Math.PI;
			}
			if(xx < 0) {
				direction = 90 + Math.atan(yy / xx) * 180 / Math.PI;
			}
			if(xx == 0 && yy > 0) {
				direction = 0;
			}
			if(xx == 0 && yy < 0) {
				direction = 180;
			}
			location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			geoField = new GeomagneticField(Double.valueOf(location.getLatitude()).floatValue(), Double.valueOf(location.getLongitude()).floatValue(), Double.valueOf(location.getAltitude()).floatValue(), System.currentTimeMillis());
			float a = geoField.getDeclination();
			float sample = (float) (direction - a);
			str = "Round Value: " + String.valueOf(sample) + "\nExact Value: " + String.valueOf(xx);
			
		}
		else if(event.sensor == gsensor) {
			// task 1 : rate of rotation around the x, y and z axis
			// that is, evenrt.values[0-2]
			/*
			xx = event.values[0];
			yy = event.values[1];
			zz = event.values[2];
			x = String.valueOf(xx);
			y = String.valueOf(yy);
			z = String.valueOf(zz);
			str = "Rate of rotation: \nx: " + x + "\ny:" + y + "\nz: " + z;
			
			
			// task 2 : angle of mobile phone when rotating on a flat surface
			// that is, the angle of rotating around z axis
			// equals to rate * timestamp
			
			if (timestamp != 0) {  
			      float dT = (event.timestamp - timestamp) * NS2S;  
			      zz = event.values[2];
			      zvalue += ((zz * dT) * 180 / Math.PI) ;  
			   }  
			timestamp = event.timestamp;  
			z = String.valueOf(zvalue);
			str = "Rotation: " + z;
			*/
		}
		tx.setText(str);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub	
	}
	
	@Override
	protected void onResume() {
		zvalue = 0;
	    super.onResume();
	    //SensorManager.registerListener(this, gsensor, SensorManager.SENSOR_DELAY_NORMAL);
	    SensorManager.registerListener(this, osensor, SensorManager.SENSOR_DELAY_NORMAL);
	    //SensorManager.registerListener(this, msensor, SensorManager.SENSOR_DELAY_NORMAL);
	  }
	
	@Override
	protected void onPause() {
	    super.onPause();
	    SensorManager.unregisterListener(this);
	  }

}