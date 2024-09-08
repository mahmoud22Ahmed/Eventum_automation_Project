package GeneralClasses;

import DriverFactory.DriverFactory;
import Pages.LandingPage;
import Utilities.LogsUtils;
import Utilities.SelenuimUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

public class General {
    private static List<WebElement> ValidationMessages = new ArrayList<WebElement>(3);
    private static final By ValidtionMessageLocetor = By.xpath("//span[@class = 'warning-span ']");
    private static final By HomePage = By.xpath("//img");
    private static final By HomePageDiv =By.id("page-content-wrapper");


    public static void Clean(String DashboardName) {


        try {
            SelenuimUtil.clickingOnElement(DriverFactory.getDriver(), HomePage);
            DriverFactory.getDriver().navigate().refresh();
            new LandingPage(DriverFactory.getDriver()).deleteDashboard(DashboardName);
        } catch (Exception e) {
            LogsUtils.info("there is dashboard in this step");
        }
    }

    public static List<String> getValidationMessages(WebDriver driver){
        List<String> Messages = new ArrayList<String>();
        try {
            ValidationMessages = driver.findElements(ValidtionMessageLocetor);
        }catch (TimeoutException e){
            return null;
        }
        for (WebElement Element : ValidationMessages){
            Messages.add((String) Element.getText());
        }
        return Messages;
    }

    public static void getLandingPage(WebDriver Driver){
        SelenuimUtil.clickingOnElement(Driver,HomePage);
        SelenuimUtil.clickingOnElement(Driver,HomePageDiv);
    }

    public static boolean checkCurrentURL(WebDriver Driver,String ExpectedURL) throws InterruptedException {
        try {
            SelenuimUtil.generalWait(Driver).until(ExpectedConditions.urlToBe(ExpectedURL));
            return true;
        }catch (TimeoutException e){
            return false;
        }

    }
}
