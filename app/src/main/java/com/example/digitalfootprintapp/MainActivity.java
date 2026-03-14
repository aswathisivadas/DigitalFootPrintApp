package com.example.digitalfootprintapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.util.Log;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import java.util.List;
import android.Manifest;
import android.location.LocationManager;
import android.bluetooth.BluetoothAdapter;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<AppData> appList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Button scanButton = findViewById(R.id.scanButton);

        scanButton.setOnClickListener(v -> {
            scanInstalledApps();
            checkLocationStatus();
            checkBluetoothStatus();
            checkUsageStats();
        });
    }

    private void scanInstalledApps() {
        appList.clear();
        PackageManager pm = getPackageManager();
        List<ApplicationInfo> apps = pm.getInstalledApplications(0);

        for (ApplicationInfo app : apps) {

            String packageName = app.packageName;
            String appName = pm.getApplicationLabel(app).toString();

            boolean camera =
                    pm.checkPermission(android.Manifest.permission.CAMERA, packageName)
                            == PackageManager.PERMISSION_GRANTED;

            boolean microphone =
                    pm.checkPermission(android.Manifest.permission.RECORD_AUDIO, packageName)
                            == PackageManager.PERMISSION_GRANTED;

            boolean location =
                    pm.checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, packageName)
                            == PackageManager.PERMISSION_GRANTED;

            boolean contacts =
                    pm.checkPermission(android.Manifest.permission.READ_CONTACTS, packageName)
                            == PackageManager.PERMISSION_GRANTED;

            boolean storage =
                    pm.checkPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE, packageName)
                            == PackageManager.PERMISSION_GRANTED;

            boolean sms =
                    pm.checkPermission(android.Manifest.permission.READ_SMS, packageName)
                            == PackageManager.PERMISSION_GRANTED;
            AppData data = new AppData(
                    appName,
                    camera,
                    microphone,
                    location,
                    contacts,
                    storage,
                    sms,
                    0
            );

            appList.add(data);

            Log.d("APP_DATA", "App: " + appName);
            Log.d("APP_DATA", "Camera: " + camera);
            Log.d("APP_DATA", "Microphone: " + microphone);
            Log.d("APP_DATA", "Location: " + location);
            Log.d("APP_DATA", "Contacts: " + contacts);
            Log.d("APP_DATA", "Storage: " + storage);
            Log.d("APP_DATA", "SMS: " + sms);
            Log.d("APP_DATA", "----------------------");
        }

        Log.d("APP_LIST_SIZE", "Total apps scanned: " + appList.size());
    }
    private void checkLocationStatus() {

        LocationManager locationManager =
                (LocationManager) getSystemService(LOCATION_SERVICE);

        boolean isLocationEnabled =
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        Log.d("SYSTEM_STATUS", "Location Enabled: " + isLocationEnabled);
    }
    private void checkBluetoothStatus() {

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter != null) {
            boolean isBluetoothEnabled = bluetoothAdapter.isEnabled();
            Log.d("SYSTEM_STATUS", "Bluetooth Enabled: " + isBluetoothEnabled);
        } else {
            Log.d("SYSTEM_STATUS", "Bluetooth not supported on this device");
        }
    }
    private void checkUsageStats() {

        UsageStatsManager usageStatsManager =
                (UsageStatsManager) getSystemService(USAGE_STATS_SERVICE);

        long currentTime = System.currentTimeMillis();

        List<UsageStats> stats = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                currentTime - (1000 * 60 * 60 * 24),
                currentTime
        );

        if (stats != null) {

            SortedMap<Long, UsageStats> sortedMap = new TreeMap<>();

            for (UsageStats usageStats : stats) {
                sortedMap.put(usageStats.getLastTimeUsed(), usageStats);
            }

            if (!sortedMap.isEmpty()) {

                for (Map.Entry<Long, UsageStats> entry : sortedMap.entrySet()) {

                    PackageManager pm = getPackageManager();

                    try {

                        ApplicationInfo ai = pm.getApplicationInfo(entry.getValue().getPackageName(), 0);
                        String appName = pm.getApplicationLabel(ai).toString();
                        long timeUsed = entry.getValue().getTotalTimeInForeground();
                        for (AppData app : appList) {
                            if (app.appName.equals(appName)) {
                                app.usageTime = timeUsed;
                            }
                        }

                        Log.d("USAGE_STATS", "App: " + appName + " Time Used: " + timeUsed);

                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}