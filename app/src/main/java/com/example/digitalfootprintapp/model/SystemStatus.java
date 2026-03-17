package com.example.digitalfootprintapp.model;

/**
 * Model class for system-wide settings.
 * Member 2 will add points to the score if these are ON.
 */
public class SystemStatus {
    private boolean isLocationOn;
    private boolean isBluetoothOn;
    private boolean isWifiScanningOn;

    public SystemStatus(boolean isLocationOn, boolean isBluetoothOn, boolean isWifiScanningOn) {
        this.isLocationOn = isLocationOn;
        this.isBluetoothOn = isBluetoothOn;
        this.isWifiScanningOn = isWifiScanningOn;
    }

    public boolean isLocationOn() { return isLocationOn; }
    public boolean isBluetoothOn() { return isBluetoothOn; }
    public boolean isWifiScanningOn() { return isWifiScanningOn; }
}