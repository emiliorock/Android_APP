package com.example.mywifi;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.net.wifi.*;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // my work
        Button btn = (Button) findViewById(R.id.button);
        final ListView list = (ListView) findViewById(R.id.listView);
        final WifiManager mWifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        final List<ScanResult> aps = mWifiManager.getScanResults();
        final List<String> results = new ArrayList<String>();
        if (!mWifiManager.isWifiEnabled())
            if (mWifiManager.getWifiState() != WifiManager.WIFI_STATE_ENABLING)
                mWifiManager.setWifiEnabled(true);

        //remove signals hiding SSID
        for (int i = 0; i < aps.size(); i++) {
            if (aps.get(i).SSID.equals("")) {
                aps.remove(i);
                i--;
            }
        }
        // remove signals with same SSID
        for(int i = 0; i < aps.size(); i++) {
            for (int j = i + 1; j < aps.size(); j++) {
                if (aps.get(i).SSID.equals(aps.get(j).SSID)) {
                    if (Math.abs(aps.get(i).level) >= Math.abs(aps.get(j).level)) {
                        aps.remove(j);
                        j--;
                    } else {
                        aps.set(i, aps.get(j));
                        aps.remove(j);
                        j--;
                    }
                }
            }
        }
        // remove the least strong signal
        while (aps.size() > 4) {
            int min = 0;
            for (int i = 0; i < aps.size(); i++) {
                if (Math.abs(aps.get(min).level) >= Math.abs(aps.get(i).level)) {
                    min = i;
                }
            }
            aps.remove(min);
        }

        // transfer the output to the required format
        for (int i = 0; i < aps.size(); i++) {
            ScanResult strScan = aps.get(i);
            String str = i+1+"."+strScan.SSID+":"+strScan.level;
            results.add(i, str);
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, results);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.setAdapter(adapter);
                //task 3
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        final int index = position;
                        if (aps.get(position).capabilities.equals("[ESS]")) { 
                            WifiConfiguration config= new WifiConfiguration();
                            config.SSID = "\"" + aps.get(position).SSID + "\"";
                            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                            config.status = WifiConfiguration.Status.ENABLED;
                            int netId = mWifiManager.addNetwork(config);
                            mWifiManager.saveConfiguration();
                            mWifiManager.reconnect();
                            mWifiManager.enableNetwork(netId, true);

                            Toast.makeText(getApplicationContext(), aps.get(position).SSID, Toast.LENGTH_LONG).show();
                            while(true) {
                                WifiInfo mywifi = mWifiManager.getConnectionInfo();
                                if(mywifi.getIpAddress() != 0 && mywifi.getSSID().equals('"' + aps.get(position).SSID + '"')) {
                                    String ipAddress = longToIp(mywifi.getIpAddress());
                                    Log.i("ip_address",ipAddress);
                                    Toast.makeText(MainActivity.this, "IP_address is " + ipAddress + "\nESSID is " + mywifi.getSSID() + "\n", Toast.LENGTH_LONG).show();
                                    break;
                                }
                            }
                        } else {  
                            final Dialog dialog = new Dialog(MainActivity.this);
                            dialog.setTitle("Login");
                            dialog.setContentView(R.layout.login);
                            dialog.show();

                            final EditText user = (EditText) dialog.findViewById(R.id.editTextUserNameToLogin);
                            final EditText pass = (EditText) dialog.findViewById(R.id.editTextPasswordToLogin);
                            Button submitbutton = (Button) dialog.findViewById(R.id.buttonSignIn);
                            //Button cancelbutton = (Button) dialog.findViewById(R.id.button_cancel);

                            submitbutton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String username = user.getText().toString();
                                    String password = pass.getText().toString();
                                    WifiEnterpriseConfig enterpriseConfig = new WifiEnterpriseConfig();
                                    WifiConfiguration wifiConfig = new WifiConfiguration();
                                    wifiConfig.SSID = "\"" + aps.get(index).SSID + "\"";
                                    wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);
                                    wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.IEEE8021X);
                                    enterpriseConfig.setIdentity(username);
                                    enterpriseConfig.setPassword(password);
                                    enterpriseConfig.setEapMethod(WifiEnterpriseConfig.Eap.PEAP);
                                    wifiConfig.enterpriseConfig = enterpriseConfig;
                                    int netId = mWifiManager.addNetwork(wifiConfig);
                                    mWifiManager.enableNetwork(netId, true);
                                    dialog.cancel();
                                    while(true) {
                                        WifiInfo mywifi = mWifiManager.getConnectionInfo();
                                        if(mywifi.getIpAddress() != 0 && mywifi.getSSID().equals('"' + aps.get(index).SSID + '"')) {
                                            String ipAddress = longToIp(mywifi.getIpAddress());
                                            Log.i("ip_address",ipAddress);
                                            Toast.makeText(MainActivity.this, "IP_address is " + ipAddress + "\nESSID is " + mywifi.getSSID() + "\n", Toast.LENGTH_LONG).show();
                                            break;
                                        }
                                    }
                                }
                            });
               
                        }
                    }
                });
            }
        });
    }

    // Original code
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public static boolean isWifiConnected(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(wifiNetworkInfo.isConnected())
        {
            return true ;
        }

        return false ;
    }

    public static String longToIp(long ip) {
        StringBuilder sb = new StringBuilder(15);

        for (int i = 0; i < 4; i++) {
            sb.insert(0, Long.toString(ip & 0xff));

            if (i < 3) {
                sb.insert(0, '.');
            }

            ip >>= 8;
        }

        return sb.toString();
    }


}
