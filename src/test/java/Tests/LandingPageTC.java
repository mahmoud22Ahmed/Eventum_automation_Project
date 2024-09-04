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

import static HelperFn.Helper.Clean;

public class LandingPageTC {
    private final String ValidUsername = (String) DataUtil.getJsonData("ValidLoginData", "username");
    private final String ValidPassword = (String) DataUtil.getJsonData("ValidLoginData", "password");
    private final String LoginPage_URL = DataUtil.getPropertyValue("Environment", "LOGIN_URL");

    private final String Browser = DataUtil.getPropertyValue("Environment", "BROWSER");

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
      @Title : Check logout functionality
      @Steps:
          1) Log in with valid credentials.
          2) Click on the Welcome menu.
          3) Click on the logout button.
      @Expected result : The user should be redirected back to the login page.
    */
    @Test(groups = {"Valid"}, priority = 1)
    private void TC1_CheckLogout() throws InterruptedException, IOException {
        boolean LogOut;
        LogOut = new Pages.LandingPage(DriverFactory.getDriver()).PressOnLogout()
                .CheckLoginCurrentURL(LoginPage_URL);
        Assert.assertTrue(LogOut);
    }

    /*
      @Title : Search for a newly added dashboard in the dropdown
      @Steps:
          1) Log in with valid credentials.
          2) Click on the 'Add Dashboard' button.
          3) Enter a random dashboard name and submit.
          4) Search for the dashboard in the dashboard dropdown.
      @Expected result : The newly added dashboard should be found in the dropdown.
    */
    @Test(groups = {"Valid"}, priority = 1)
    private void TC2_SearchingInDashboardDropdown() throws InterruptedException, IOException {
        boolean DashboardFind;
        String RandomDashboardName = DataUtil.generateRandomString(10);

        new Pages.LandingPage(DriverFactory.getDriver()).clickAddDashboard();
        new Pages.AddPage(DriverFactory.getDriver())
                .enterDashboardName(RandomDashboardName).clickSubmit();

        DashboardFind = new Pages.LandingPage(DriverFactory.getDriver())
                .checkSearchinDashboardDropdown(RandomDashboardName);
        LogsUtils.info("Dashboard found: " + DashboardFind);
        Assert.assertTrue(DashboardFind);
        Clean(RandomDashboardName);
    }

    /*
      @Title : Check the functionality of the 'Add Dashboard' button
      @Steps:
          1) Log in with valid credentials.
          2) Click on the 'Add Dashboard' button.
      @Expected result : The user should be redirected to the 'New Dashboard' page.
    */
    @Test(groups = {"Valid"}, priority = 1)
    private void TC3_CheckAddDashboardButton() throws InterruptedException, IOException {
        boolean DashboardPage;

        DashboardPage = new Pages.LandingPage(DriverFactory.getDriver())
                .clickAddDashboard().CheckPageTitle("New Dashboard");
        LogsUtils.info("Dashboard page opened: " + DashboardPage);

        Assert.assertTrue(DashboardPage);
    }

    /*
      @Title : Check the functionality of deleting a dashboard
      @Steps:
          1) Log in with valid credentials.
          2) Add a dashboard with the name "UCL".
          3) Delete the dashboard named "UCL".
      @Expected result : The dashboard should be deleted successfully.
    */
    @Test(groups = {"Valid"}, priority = 1)
    public void TC4_checkDeleteDashboard() {
        boolean checker;
        new Pages.LandingPage(DriverFactory.getDriver()).clickAddDashboard();
        new Pages.AddPage(DriverFactory.getDriver()).enterDashboardName("UCL")
                .clickSubmit();
        checker = new Pages.LandingPage(DriverFactory.getDriver())
                .DeleteDashboard("UCL");

        Assert.assertTrue(checker);
    }

    @AfterMethod(alwaysRun = true)
    private void tearout() {
        DriverFactory.quitDriver();
    }
}
