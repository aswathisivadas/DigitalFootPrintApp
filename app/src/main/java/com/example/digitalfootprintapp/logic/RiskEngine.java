package com.example.digitalfootprintapp.logic;

import com.example.digitalfootprintapp.model.AppInfo;
import com.example.digitalfootprintapp.model.SystemStatus;
import java.util.ArrayList;
import java.util.List;

/**
 * Member 2: The Logic Engine (Final Professional Version)
 */
public class RiskEngine {

    // Scoring Weights
    private static final int WEIGHT_LOCATION_ON = 5;
    private static final int WEIGHT_BLUETOOTH_ON = 2;
    private static final int WEIGHT_WIFI_SCANNING_ON = 3;
    
    private static final int WEIGHT_APP_LOCATION = 2;
    private static final int WEIGHT_APP_MICROPHONE = 3;
    private static final int WEIGHT_APP_CAMERA = 2;
    private static final int WEIGHT_FREQUENTLY_USED_RISKY = 4;
    private static final int WEIGHT_HIGH_PERMISSION_DENSITY = 10;

    public static class RiskResult {
        public final int score;
        public final String category;
        public final String colorHex;
        public final List<String> riskReasons; // Added for Member 4 (Recommendations)

        public RiskResult(int score, String category, String colorHex, List<String> riskReasons) {
            this.score = score;
            this.category = category;
            this.colorHex = colorHex;
            this.riskReasons = riskReasons;
        }
    }

    public RiskResult calculateRisk(List<AppInfo> apps, SystemStatus status) {
        int totalScore = 0;
        List<String> reasons = new ArrayList<>();

        // 1. System-level checks
        if (status.isLocationOn()) {
            totalScore += WEIGHT_LOCATION_ON;
            reasons.add("System Location is turned ON");
        }
        if (status.isBluetoothOn()) {
            totalScore += WEIGHT_BLUETOOTH_ON;
            reasons.add("Bluetooth is active (Proximity tracking risk)");
        }
        if (status.isWifiScanningOn()) {
            totalScore += WEIGHT_WIFI_SCANNING_ON;
            reasons.add("WiFi Scanning is active");
        }

        int dangerousAppsCount = 0;

        // 2. App-level checks
        for (AppInfo app : apps) {
            boolean isRiskyApp = false;
            for (String permission : app.getPermissions()) {
                if (permission.contains("LOCATION")) { totalScore += WEIGHT_APP_LOCATION; isRiskyApp = true; }
                if (permission.contains("MICROPHONE") || permission.contains("RECORD_AUDIO")) { totalScore += WEIGHT_APP_MICROPHONE; isRiskyApp = true; }
                if (permission.contains("CAMERA")) { totalScore += WEIGHT_APP_CAMERA; isRiskyApp = true; }
            }

            if (isRiskyApp) {
                dangerousAppsCount++;
                if (app.isFrequentlyUsed()) {
                    totalScore += WEIGHT_FREQUENTLY_USED_RISKY;
                    reasons.add("Frequent use of risky app: " + app.getAppName());
                }
            }
        }

        // 3. Density check
        if (dangerousAppsCount > 10) {
            totalScore += WEIGHT_HIGH_PERMISSION_DENSITY;
            reasons.add("High Permission Density: >10 apps have dangerous access");
        }

        return finalizeResult(totalScore, reasons);
    }

    private RiskResult finalizeResult(int score, List<String> reasons) {
        if (score <= 30) return new RiskResult(score, "Low Risk", "#4CAF50", reasons);
        if (score <= 60) return new RiskResult(score, "Medium Risk", "#FFC107", reasons);
        return new RiskResult(score, "High Risk", "#F44336", reasons);
    }
}