package com.example.digitalfootprintapp.logic.recommendation;

import com.example.digitalfootprintapp.logic.RiskEngine;
import com.example.digitalfootprintapp.model.AppInfo;
import com.example.digitalfootprintapp.model.SystemStatus;

import java.util.List;

public class PrivacyAlertGenerator {

    public String generateAlerts(List<AppInfo> apps,
                                 SystemStatus status,
                                 RiskEngine.RiskResult riskResult) {

        StringBuilder message = new StringBuilder();
        int riskyAppsCount = 0;

        // -----------------------
        // SYSTEM SETTINGS ALERTS
        // -----------------------

        if (status.isLocationOn()) {

            message.append("⚠ Privacy Alert\n");
            message.append("Location service is currently ON.\n\n");
            message.append("Keeping location enabled may expose your movement patterns.\n\n");
            message.append("Suggestion: Turn OFF location when not needed.\n\n");
        }

        if (status.isBluetoothOn()) {

            message.append("⚠ Privacy Alert\n");
            message.append("Bluetooth is currently ON.\n\n");
            message.append("Nearby devices may detect your phone.\n\n");
            message.append("Suggestion: Turn OFF Bluetooth when not using wireless devices.\n\n");
        }

        if (status.isWifiScanningOn()) {

            message.append("⚠ Privacy Alert\n");
            message.append("Wi-Fi scanning is active.\n\n");
            message.append("Your phone may continuously scan nearby networks.\n\n");
            message.append("Suggestion: Disable Wi-Fi scanning if not required.\n\n");
        }

        // -----------------------
        // APP PERMISSION ALERTS
        // -----------------------

        for (AppInfo app : apps) {

            boolean risky = false;

            for (String permission : app.getPermissions()) {

                if (permission.contains("LOCATION")) {

                    message.append("⚠ Privacy Notice\n");
                    message.append(app.getAppName())
                            .append(" has access to your location.\n\n");

                    message.append("Apps with location permission may track movement.\n\n");
                    message.append("Suggestion: Restrict location access if unnecessary.\n\n");

                    risky = true;
                }

                if (permission.contains("MICROPHONE") || permission.contains("RECORD_AUDIO")) {

                    message.append("⚠ Privacy Notice\n");
                    message.append(app.getAppName())
                            .append(" has microphone access.\n\n");

                    message.append("Microphone permissions allow apps to capture audio data.\n\n");
                    message.append("Suggestion: Disable microphone permission if unnecessary.\n\n");

                    risky = true;
                }

                if (permission.contains("CAMERA")) {

                    message.append("⚠ Privacy Notice\n");
                    message.append(app.getAppName())
                            .append(" has camera access.\n\n");

                    message.append("Frequent camera-enabled apps may increase your digital footprint.\n\n");
                    message.append("Suggestion: Review camera permission if not required.\n\n");

                    risky = true;
                }
            }

            if (risky) {
                riskyAppsCount++;
            }
        }

        // -----------------------
        // TOO MANY RISKY APPS
        // -----------------------

        if (riskyAppsCount > 10) {

            message.append("🚨 Privacy Warning\n");
            message.append("More than 10 apps on your device have sensitive permissions.\n\n");
            message.append("This increases your digital footprint exposure.\n\n");
            message.append("Suggestion: Review or remove unused apps.\n\n");
        }

        // -----------------------
        // FINAL RISK LEVEL
        // -----------------------

        message.append("\n");

        if (riskResult.category.equals("Low Risk")) {

            message.append("✅ Your device currently has a LOW digital footprint exposure.\n");
            message.append("Your privacy settings look safe.\n");

        }
        else if (riskResult.category.equals("Medium Risk")) {

            message.append("⚠ Your device has MODERATE privacy exposure.\n");
            message.append("Review app permissions and unused system settings.\n");

        }
        else {

            message.append("🚨 HIGH privacy exposure detected.\n");
            message.append("Multiple services and apps are collecting sensitive data.\n");
            message.append("Immediate review is recommended.\n");
        }

        return message.toString();
    }
}