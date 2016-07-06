package com.example.sd_two;

import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import java.util.ArrayList;

public class WifiAdmin {
    private final static String TAG = "WifiAdmin";
    private StringBuffer mStringBuffer = new StringBuffer();

    List<ScanResult> listResult;
    //private ScanResult mScanResult;
    // Define object of WifiManager.

    // Define object of WifiInfo
    private WifiInfo mWifiInfo;
    public WifiManager mWifiManager;

    /**
     * Construction method.
     */
    public WifiAdmin(Context context) {
        mWifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        mWifiInfo = mWifiManager.getConnectionInfo();
    }

    /**
     * Get scan result/
     */
    public List<String> getScanResult() {
        List<String> strs = new ArrayList();
        mWifiManager.startScan();
        listResult = mWifiManager.getScanResults();
        if (listResult != null) {
            for (int i = 0; i < listResult.size() - 1; i++) {
                //System.out.println(listResult.get(i));
                listResult.get(i).level = mWifiManager.calculateSignalLevel(listResult.get(i).level,100);
                for (int j = i+1; j < listResult.size(); j++) {
                    if (listResult.get(i).SSID.equals(listResult.get(j).SSID)) {
                        if (listResult.get(i).level < listResult.get(j).level) {
                            listResult.remove(i);
                            if (i != 0)
                                i = i - 1;
                        }
                        else {
                            listResult.remove(j);
                            j = j - 1;
                        }
                    }
                }
            }
            for (int i = 0; i < listResult.size(); i++) {
                for (int j = 0; j < i; j++) {
                    if (listResult.get(i).level > listResult.get(j).level) {
                        ScanResult temp = listResult.get(i);
                        listResult.remove(i);
                        listResult.add(j, temp);
                    }
                }
            }
            if (listResult.size() > 4) {
                int i = 4;
                while (listResult.size() > 4) {
                    listResult.remove(i);
                }
            }

            for (int i = 0; i < listResult.size(); i++) {
                if (i != listResult.size() - 1) {
                    strs.add("SSID: " + listResult.get(i).SSID + "\n" +
                            "MAC Address: " + listResult.get(i).BSSID + "\n" +
                            "Level: " + listResult.get(i).level + "\n");
                    //System.out.println(strs.get(i));
                } else {
                    strs.add("SSID: " + listResult.get(i).SSID + "\n" +
                            "MAC Address: " + listResult.get(i).BSSID + "\n" +
                            "Level: " + listResult.get(i).level);
                }
            }
        }
        //Log.i(TAG, mStringBuffer.toString());
        return strs;
    }
}


