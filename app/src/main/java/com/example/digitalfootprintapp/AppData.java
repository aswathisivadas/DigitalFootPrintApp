package com.example.digitalfootprintapp;

public class AppData {

    String appName;
    boolean camera;
    boolean microphone;
    boolean location;
    boolean contacts;
    boolean storage;
    boolean sms;
    long usageTime;

    public AppData(String appName, boolean camera, boolean microphone,
                   boolean location, boolean contacts, boolean storage,
                   boolean sms, long usageTime) {

        this.appName = appName;
        this.camera = camera;
        this.microphone = microphone;
        this.location = location;
        this.contacts = contacts;
        this.storage = storage;
        this.sms = sms;
        this.usageTime = usageTime;
    }
}

