package com.example.digitalfootprintapp.logic;

import com.example.digitalfootprintapp.model.AppInfo;
import com.example.digitalfootprintapp.model.SystemStatus;
import java.util.List;

/**
 * Member 2: The Logic Engine
 */
public class RiskEngine {

    // Scoring Weights (Step 2)
    private static final int WEIGHT_LOCATION_ON = 5;
    private static final int WEIGHT_BLUETOOTH_ON = 2;
    private static final int WEIGHT_WIFI_SCANNING_ON = 3;
    
    private static final int WEIGHT_APP_LOCATION = 2;
    private static final int WEIGHT_APP_MICROPHONE = 3;
    private static final int WEIGHT_APP_CAMERA = 2;
    private static final int WEIGHT_FREQUENTLY_USED_RISKY = 4;
    private static final int WEIGHT_HIGH_PERMISSION_DENSITY = 10;

    /**
     * Updated Result class for Member 3 (UI)
     */
    public static class RiskResult {
        public final int score;
        public final String category;
        public final String colorHex; // Color for the UI (e.g., "#FF0000" for Red)

        public RiskResult(int score, String category, String colorHex) {
            this.score = score;
            this.category = category;
            this.colorHex = colorHex;
        }
    }

    // Calculation Logic (Step 3)
    public RiskResult calculateRisk(List<AppInfo> apps, SystemStatus status) {
        int totalScore = 0;
        if (status.isLocationOn()) totalScore += WEIGHT_LOCATION_ON;
        if (status.isBluetoothOn()) totalScore += WEIGHT_BLUETOOTH_ON;
        if (status.isWifiScanningOn()) totalScore += WEIGHT_WIFI_SCANNING_ON;

        int dangerousAppsCount = 0;
        for (AppInfo app : apps) {
            boolean isRiskyApp = false;
            int appRiskContribution = 0;
            for (String permission : app.getPermissions()) {
                if (permission.contains("LOCATION")) { appRiskContribution += WEIGHT_APP_LOCATION; isRiskyApp = true; }
                if (permission.contains("MICROPHONE") || permission.contains("RECORD_AUDIO")) { appRiskContribution += WEIGHT_APP_MICROPHONE; isRiskyApp = true; }
                if (permission.contains("CAMERA")) { appRiskContribution += WEIGHT_APP_CAMERA; isRiskyApp = true; }
            }
            if (isRiskyApp && app.isFrequentlyUsed()) appRiskContribution += WEIGHT_FREQUENTLY_USED_RISKY;
            if (isRiskyApp) dangerousAppsCount++;
            totalScore += appRiskContribution;
        }
        if (dangerousAppsCount > 10) totalScore += WEIGHT_HIGH_PERMISSION_DENSITY;

        return finalizeResult(totalScore);
    }

    /**
     * The Verdict (Step 4)
     * Categorizes the score and assigns a color for the Dashboard.
     */
    private RiskResult finalizeResult(int score) {
        if (score <= 30) {
            return new RiskResult(score, "Low Risk", "#4CAF50"); // Green
        } else if (score <= 60) {
            return new RiskResult(score, "Medium Risk", "#FFC107"); // Yellow/Amber
        } else {
            return new RiskResult(score, "High Risk", "#F44336"); // Red
        }
    }
}