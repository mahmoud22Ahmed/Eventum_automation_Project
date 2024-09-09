package HelperFn;

import DriverFactory.DriverFactory;
import Pages.LandingPage;
import Utilities.LogsUtils;
import Utilities.SelenuimUtil;
import org.openqa.selenium.By;

import java.lang.reflect.Field;

public class Helper {

    public static void Clean(String DashboardName){
        final By HomePage = By.xpath("//img");

        try {
            SelenuimUtil.clickingOnElement(DriverFactory.getDriver(),HomePage);
            DriverFactory.getDriver().navigate().refresh();
            new LandingPage(DriverFactory.getDriver()).DeleteDashboard(DashboardName);
        }
        catch (Exception e){
            LogsUtils.info("there is dashboard in this step");
        }
    }
}
