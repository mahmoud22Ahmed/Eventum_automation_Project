package HelperFn;

import DriverFactory.DriverFactory;
import Pages.LandingPage;
import Utilities.LogsUtils;
import Utilities.SelenuimUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.lang.reflect.Field;

public class Helper {

    WebElement Driver;
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

    public boolean checkErrorMessage(String element, String expectedMessage){
        String Message;
        switch (element){
            case "popup":
                Message = Driver.findElement(popupMessageLocetor).getText();
                break;
            case "password":
                Message = Driver.findElement(passwordMessageLocetor).getText();
                break;
            case "username":
                Message = Driver.findElement(usernameMessageLocetor).getText();
                break;
            default:
                Message = "not found";
                break;
        }

        LogsUtils.info("error message: " + Message);
        return Message.equals(expectedMessage);
    }
}
