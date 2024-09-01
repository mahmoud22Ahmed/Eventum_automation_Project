package Tests;

import DriverFactory.DriverFactory;
import Pages.LoginPage;
import Utilities.DataUtil;
import Utilities.LogsUtils;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.time.Duration;

public class LandingPageTC {
    private final String ValidUsername = (String) DataUtil.getJsonData("ValidLoginData","username");
    private final String ValidPassword = (String) DataUtil.getJsonData("ValidLoginData","password");
    private final String LoginPage_URL = DataUtil.getPropertyValue("Environment","LOGIN_URL");
    private final String LandPage_URL = DataUtil.getPropertyValue("Environment","LANDING_URL");
    private final String Browser = DataUtil.getPropertyValue("Environment","BROWSER");


    public LandingPageTC() throws IOException {
    }


    @BeforeMethod(alwaysRun = true)
    private void Setup() throws IOException {
        DriverFactory.setupDriver(Browser);
        DriverFactory.getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        DriverFactory.getDriver().get(LoginPage_URL);
        new LoginPage(DriverFactory.getDriver()).EnterUsername(ValidUsername)
                .EnterPassword(ValidPassword).ClickLogin();

    }

    /*
      @Title : check logout functionality
      @Steps: 1) login with a valid data
              2) logout by click on Welcome menu
              3) click on logout button
      @Expected result :
                         - return back to login page
    */
    @Test(groups = {"Valid"} , priority =  1)
    private void TC1_CheckLogout() throws InterruptedException, IOException {
        SoftAssert SAsserter = new SoftAssert();
        boolean LoginCheck;
        boolean LogOut;


        LogOut = new Pages.LandingPage(DriverFactory.getDriver()).PressOnLogout()
                .CheckLoginCurrentURL(LoginPage_URL);
       // LogsUtils.info("login Check:" + LoginCheck);

        SAsserter.assertTrue(LogOut);
        SAsserter.assertAll();
    }

    /*
      @Title : check logout functionality
      @Steps: 1) login with a valid data
              2) logout by click on Welcome menu
              3) click on logout button
      @Expected result :
                         - return back to login page
    */
    @Test(groups = {"Valid"} , priority =  1)
    private void TC2_SearchingInDashboardDropdown() throws InterruptedException, IOException {
        boolean DashboardFind;
        SoftAssert SAsserter = new SoftAssert();



        DashboardFind = new Pages.LandingPage(DriverFactory.getDriver())
                            .SearchInDashboardDropdown("Mahmoud");

        LogsUtils.info("Dashboard find:" + DashboardFind);

        SAsserter.assertTrue(DashboardFind);
        SAsserter.assertAll();
    }

    /*
     @Title : check logout functionality
     @Steps: 1) login with a valid data
             2) logout by click on Welcome menu
             3) click on logout button
     @Expected result :
                        - return back to login page
   */
    @Test(groups = {"Valid"} , priority =  1)
    private void TC3_CheckAddDashboardButton() throws InterruptedException, IOException {
        boolean DashboardPage;
        SoftAssert SAsserter = new SoftAssert();



        DashboardPage = new Pages.LandingPage(DriverFactory.getDriver())
                .clickAddDashboard().CheckPageTitle("New Dashboard");

        LogsUtils.info("Dashboard find:" + DashboardPage);

        SAsserter.assertTrue(DashboardPage);
        SAsserter.assertAll();
    }

    @AfterMethod(alwaysRun = true)
    private void tearout(){
        DriverFactory.quitDriver();
    }
}
