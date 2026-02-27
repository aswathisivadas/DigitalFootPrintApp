package com.example.digitalfootprintapp.logic;

import com.example.digitalfootprintapp.model.AppInfo;
import com.example.digitalfootprintapp.model.SystemStatus;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class RiskEngineTest {

    @Test
    public void testSafeUser() {
        // Step 1: Create a safe system status (all off)
        SystemStatus status = new SystemStatus(false, false, false);
        
        // Step 2: Create an empty list of apps
        List<AppInfo> apps = new ArrayList<>();
        
        // Step 3: Run the engine
        RiskEngine engine = new RiskEngine();
        RiskEngine.RiskResult result = engine.calculateRisk(apps, status);
        
        // Step 4: Verify the result is 0 (Low Risk)
        assertEquals(0, result.score);
        assertEquals("Low Risk", result.category);
    }

    @Test
    public void testRiskyAppUser() {
        // Step 1: System settings are safe
        SystemStatus status = new SystemStatus(false, false, false);
        
        // Step 2: One app with Camera (2) and Mic (3) = Total 5
        List<AppInfo> apps = new ArrayList<>();
        List<String> perms = new ArrayList<>();
        perms.add("CAMERA");
        perms.add("RECORD_AUDIO");
        apps.add(new AppInfo("SocialApp", perms, false));
        
        // Step 3: Run the engine
        RiskEngine engine = new RiskEngine();
        RiskEngine.RiskResult result = engine.calculateRisk(apps, status);
        
        // Step 4: Verify score is 5
        assertEquals(5, result.score);
        assertEquals("Low Risk", result.category); // 5 is still < 30
    }

    @Test
    public void testHighExposureScenario() {
        // Step 1: Global Location is ON (+5)
        SystemStatus status = new SystemStatus(true, false, false);
        
        // Step 2: One frequently used app with Location (+2 + 4) = Total 11
        List<AppInfo> apps = new ArrayList<>();
        List<String> perms = new ArrayList<>();
        perms.add("ACCESS_FINE_LOCATION");
        apps.add(new AppInfo("Maps", perms, true));
        
        // Step 3: Run the engine
        RiskEngine engine = new RiskEngine();
        RiskEngine.RiskResult result = engine.calculateRisk(apps, status);
        
        // Step 4: Verify total score (5 + 2 + 4 = 11)
        assertEquals(11, result.score);
    }
}