package Tests;

import DriverFactory.DriverFactory;
import static HelperFn.Helper.Clean;

import HelperFn.Helper;
import Pages.AddPage;
import Pages.EditPage;
import Pages.LandingPage;
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

public class EditDashboardTC {
    private final String ValidUsername = (String) DataUtil.getJsonData("ValidLoginData", "username");
    private final String ValidPassword = (String) DataUtil.getJsonData("ValidLoginData", "password");
    private final String DashboardName = (String) DataUtil.getJsonData("AddDashboardData", "dashboard_name");
    private final String DashboardDescription = (String) DataUtil.getJsonData("AddDashboardData", "dashboard_description");
    private final String[] Groups = (String[]) DataUtil.getJsonData("AddDashboardData", "groups");
    private final String[] LandingDate = (String[]) DataUtil.getJsonData("AddDashboardData", "landing_date");
    private final String CustomDateStart = (String) DataUtil.getJsonData("AddDashboardData", "custom_range_start");
    private final String CustomDateEnd = (String) DataUtil.getJsonData("AddDashboardData", "custom_range_end");

    private final String LoginPage_URL = DataUtil.getPropertyValue("Environment", "LOGIN_URL");
    private final String Browser = DataUtil.getPropertyValue("Environment", "BROWSER");

    // Error Messages
    private final String DashboardNameMessage = (String) DataUtil.getJsonData("AddDashboardErrorMessage", "dashboard_name");
    private final String DashboarddescriMessage = (String) DataUtil.getJsonData("AddDashboardErrorMessage", "dashboard_description");

    public EditDashboardTC() throws IOException {
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
      @Title: Test case for editing a valid dashboard
      @Steps:
          1. Add a dashboard with a valid name and description.
          2. Edit the dashboard with a new random name and description.
          3. Choose valid landing date.
          4. Submit the form.
          5. Verify the dashboard is updated with the new name.
      @Expected result: Dashboard is successfully edited and verified on the landing page.
    */
    @Test(groups = {"Valid"}, priority = 2 ,dependsOnMethods = "TC2_CheckEditDashboardButton")
    public void TC1_EditValidDashboard() {
        String RandomDashboardName = DataUtil.generateRandomString(10);
        String RandomDashboarddescri = DataUtil.generateRandomString(50);

        boolean Editcheck;

        new LandingPage(DriverFactory.getDriver()).clickAddDashboard();
        new AddPage(DriverFactory.getDriver()).enterDashboardName(DashboardName)
                .enterDashboardDescription(DashboardDescription).chooseLandingDate(LandingDate[3])
                .chooseGroupsEdit(Groups[0]).chooseGroupsView(Groups[1]).clickSubmit();

        new LandingPage(DriverFactory.getDriver()).clickEditButton(DashboardName);
        new EditPage(DriverFactory.getDriver()).enterDashboardName(RandomDashboardName)
                .enterDashboardDescription(RandomDashboarddescri)
                .chooseLandingDate(LandingDate[2]).clickSubmit();
        Editcheck = new LandingPage(DriverFactory.getDriver()).CheckCurrentDashboard(RandomDashboardName);
        Assert.assertTrue(Editcheck);
        Clean(RandomDashboardName);
        
    }

    /*
      @Title: Test case for checking the Edit Dashboard button functionality
      @Steps:
          1. Add a dashboard with a valid name and description.
          2. Click the Edit button for the added dashboard.
          3. Verify that the Edit Dashboard page is opened.
      @Expected result: The Edit Dashboard page should open correctly.
    */
    @Test(groups = {"Valid"}, priority = 1)
    public void TC2_CheckEditDashboardButton() {
        boolean TitleChecker;
        new LandingPage(DriverFactory.getDriver()).clickAddDashboard();
        new AddPage(DriverFactory.getDriver()).enterDashboardName(DashboardName)
                .enterDashboardDescription(DashboardDescription).chooseLandingDate(LandingDate[3])
                .chooseGroupsEdit(Groups[0]).chooseGroupsView(Groups[1]).clickSubmit();

        TitleChecker = new LandingPage(DriverFactory.getDriver()).clickEditButton(DashboardName)
                .CheckPageTitle("Edit Dashboard");
        Assert.assertTrue(TitleChecker);
        Clean(DashboardName);
    }

    /*
      @Title: Test case for checking validation messages with an empty form
      @Steps:
          1. Add a dashboard with valid details.
          2. Edit the dashboard and clear the dashboard name field.
          3. Attempt to submit the form without filling the dashboard name field.
          4. Verify the error message for the empty dashboard name field.
      @Expected result: Correct error message is displayed for the empty dashboard name field.
    */
    @Test(groups = {"Invalid"}, priority = 2 ,dependsOnMethods = "TC2_CheckEditDashboardButton")
    public void TC3_CheckEditEmptyForm() {
        boolean ValidationMessage;
        new LandingPage(DriverFactory.getDriver()).clickAddDashboard();
        new AddPage(DriverFactory.getDriver()).enterDashboardName(DashboardName)
                .enterDashboardDescription(DashboardDescription).chooseLandingDate(LandingDate[3])
                .chooseGroupsEdit(Groups[0]).chooseGroupsView(Groups[1]).clickSubmit();

        new LandingPage(DriverFactory.getDriver()).clickEditButton(DashboardName);
        ValidationMessage = new EditPage(DriverFactory.getDriver())
                .enterDashboardName(" ").clickSubmit()
                .CheckErrorMessage("DashboardName", DashboardNameMessage);
        Assert.assertTrue(ValidationMessage);
        Clean(DashboardName);
    }

    /*
      @Title: Test case for checking valid dashboard name and description length
      @Steps:
          1. Test with a 2-character dashboard name and verify the error message.
          2. Test with a 51-character dashboard name and verify the error message.
          3. Test with a 1001-character dashboard description and verify the error message.
      @Expected result: Correct error messages are displayed for invalid lengths.
    */
    @Test(groups = {"Invalid"}, priority = 3 ,dependsOnMethods = "TC2_CheckEditDashboardButton")
    public void TC4_CheckValidRange() {
        SoftAssert SAsserter = new SoftAssert();
        boolean LengthChecker;
        String Randomstr = DataUtil.generateRandomString(2);

        new LandingPage(DriverFactory.getDriver()).clickAddDashboard();
        new AddPage(DriverFactory.getDriver()).enterDashboardName(DashboardName)
                .enterDashboardDescription(DashboardDescription).chooseLandingDate(LandingDate[3])
                .chooseGroupsEdit(Groups[0]).chooseGroupsView(Groups[1]).clickSubmit();

        new LandingPage(DriverFactory.getDriver()).clickEditButton(DashboardName);
        LogsUtils.info("Test Dashboard name with 2 characters");
        LengthChecker = new EditPage(DriverFactory.getDriver())
                .enterDashboardName(Randomstr)
                .clickSubmit().CheckErrorMessage("DashboardName", DashboardNameMessage);
        SAsserter.assertTrue(LengthChecker);

        try {
            Randomstr = DataUtil.generateRandomString(51);
            LogsUtils.info("Test Dashboard name with 51 characters");
            LengthChecker = new EditPage(DriverFactory.getDriver())
                    .enterDashboardName(Randomstr)
                    .clickSubmit().CheckErrorMessage("DashboardName", DashboardNameMessage);
            SAsserter.assertTrue(LengthChecker);
        } catch (Exception e) {
            LogsUtils.info("Get again to Add page");
            new LandingPage(DriverFactory.getDriver()).clickEditButton(DashboardName);
            Randomstr = DataUtil.generateRandomString(51);
            LogsUtils.info("Test Dashboard name with 51 characters");
            LengthChecker = new EditPage(DriverFactory.getDriver())
                    .enterDashboardName(Randomstr)
                    .clickSubmit().CheckErrorMessage("DashboardName", DashboardNameMessage);
            SAsserter.assertTrue(LengthChecker);
        }

        Randomstr = DataUtil.generateRandomString(1001);
        LogsUtils.info("Test Dashboard description with 1001 characters");
        LengthChecker = new EditPage(DriverFactory.getDriver())
                .enterDashboardDescription(Randomstr)
                .clickSubmit().CheckErrorMessage("DashboardDescription", DashboarddescriMessage);
        SAsserter.assertTrue(LengthChecker);

        SAsserter.assertAll();

        Clean(DashboardName);
    }

    /*
       @Title: Test case for checking the allowed groups counter
       @Steps:
           1. Add a dashboard with valid details.
           2. Choose multiple groups for edit and view permissions.
           3. Verify that the correct groups are selected after adding the dashboard.
       @Expected result: The allowed groups counter should display the correct count.
     */
    @Test(groups = {"Valid"}, priority = 4 ,dependsOnMethods = "TC2_CheckEditDashboardButton")
    public void TC5_checkAllowedGroupsCounter() {
        boolean CounterChecker;
        String RandomDashboardName = DataUtil.generateRandomString(10);

        new LandingPage(DriverFactory.getDriver()).clickAddDashboard();
        new AddPage(DriverFactory.getDriver()).enterDashboardName(RandomDashboardName)
                .chooseGroupsEdit(Groups[1]).chooseGroupsView(Groups[2])
                .chooseGroupsEdit(Groups[3]).chooseGroupsView(Groups[0]);

        new AddPage(DriverFactory.getDriver()).clickSubmit();

        new LandingPage(DriverFactory.getDriver()).clickEditButton(RandomDashboardName);
        CounterChecker = new EditPage(DriverFactory.getDriver()).CheckAllowedGroups();
        Assert.assertTrue(CounterChecker);
        Clean(DashboardName);
    }

    @AfterMethod(alwaysRun = true)
    private void tearout() {
        DriverFactory.quitDriver();
    }
}