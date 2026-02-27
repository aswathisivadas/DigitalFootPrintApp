package com.example.digitalfootprintapp.model;

import java.util.List;

/**
 * Model class to hold information about an installed app.
 * This is the data Member 2 will use to calculate risk.
 */
public class AppInfo {
    private String appName;          // Name of the app (e.g., "WhatsApp")
    private List<String> permissions; // List of permissions it has (e.g., "CAMERA")
    private boolean isFrequentlyUsed; // Whether the user uses this app often

    public AppInfo(String appName, List<String> permissions, boolean isFrequentlyUsed) {
        this.appName = appName;
        this.permissions = permissions;
        this.isFrequentlyUsed = isFrequentlyUsed;
    }

    // Getters for the Risk Engine to read data
    public String getAppName() { return appName; }
    public List<String> getPermissions() { return permissions; }
    public boolean isFrequentlyUsed() { return isFrequentlyUsed; }
}