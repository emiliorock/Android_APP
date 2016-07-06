package com.example.mybluetooth;

import android.support.v7.app.ActionBarActivity;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.MemoryHandler;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	TextView tx1;
	Button btn1;
	Button btn2;
	Button btn3;
	ListView listview;
	BluetoothAdapter btAdapter;
	ArrayAdapter<String> mArrayAdapter;
	Handler mHandler;
	EditText message;
	ArrayList<BluetoothDevice> deviceList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tx1 = (TextView) findViewById(R.id.textView1);
		listview = (ListView) findViewById(R.id.listView1);
		message = (EditText) findViewById(R.id.editText1);
		btn1 = (Button) findViewById(R.id.button1);
		btn2 = (Button) findViewById(R.id.button2);
		btn3 = (Button) findViewById(R.id.button3);
		deviceList = new ArrayList<BluetoothDevice>();
		mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		if (btAdapter == null) {
			tx1.setText("Oops, no bluetooth discoverd");
		} else {
			// task 1
			btn1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// check the current status of the battery
					checkStatus();
				}
			});
			// task 2
			btn2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// discover neighborhood
					if (btAdapter.startDiscovery()) {
						checkNeighbor();
					}
				}
			});
		}
	}

	public void checkStatus() {
		if (!btAdapter.isEnabled()) {
			btAdapter.enable();
		}
		tx1.setText("bluetooth is enabled");
	}

	public void checkNeighbor() {
		// Create a BroadcastReceiver for ACTION_FOUND
		final BroadcastReceiver mReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				// When discovery finds a device
				if (BluetoothDevice.ACTION_FOUND.equals(action)) {
					// Get the BluetoothDevice object from the Intent
					BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					// Add the name and address to an array adapter to show in a
					// ListView
					mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
					listview.setAdapter(mArrayAdapter);
					listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							signIn(view, position);
			                message.setFocusable(true);
						}
						public void signIn(View V, int Position) {
			                if (pair(deviceList.get(Position).getAddress(), "12345")) {
			                }
			            }
						
					});
				}
			}
		};
		// Register the BroadcastReceiver
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(mReceiver, filter);
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
	
	public static boolean pair(String strAddr, String strPsw) {
        boolean result = false;
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();
        // cancel finding process
        bluetoothAdapter.cancelDiscovery();
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }
        // Obtain other bluetooth from bluetooth device address
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(strAddr);
        if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
            try {
                ClsUtils.setPin(device.getClass(), device, strPsw);
                ClsUtils.createBond(device.getClass(), device);
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            } 
        } else {
            try {
                ClsUtils.createBond(device.getClass(), device);// Create bond
                ClsUtils.setPin(device.getClass(), device, strPsw); // pair
                ClsUtils.createBond(device.getClass(), device);
                // remoteDevice
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}

