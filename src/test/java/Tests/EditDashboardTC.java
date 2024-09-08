package Tests;

import DriverFactory.DriverFactory;
import static GeneralClasses.General.Clean;
import static GeneralClasses.General.getValidationMessages;

import GeneralClasses.General;
import Pages.DashboardPage;
import Pages.LandingPage;
import Pages.LoginPage;
import Utilities.DataUtil;
import Utilities.LogsUtils;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

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

    private String RandomDashboarddescri,RandomDashboardName;

    public EditDashboardTC() throws IOException {
    }

    @BeforeClass(alwaysRun = true)
    private void Start() throws IOException {
        DriverFactory.setupDriver(Browser);
        DriverFactory.getDriver().manage().window().maximize();

        DriverFactory.getDriver().get(LoginPage_URL);
        new LoginPage(DriverFactory.getDriver()).enterUsername(ValidUsername)
                .enterPassword(ValidPassword).clickLogin();

    }


    @BeforeMethod(alwaysRun = true)
    private void Setup() throws IOException {
        General.getLandingPage(DriverFactory.getDriver());
        DriverFactory.getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        new LandingPage(DriverFactory.getDriver()).clickAddDashboard();
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
        RandomDashboardName = DataUtil.generateRandomString(10);
        RandomDashboarddescri = DataUtil.generateRandomString(10);

        boolean Editcheck;
        new DashboardPage(DriverFactory.getDriver()).enterDashboardName(RandomDashboardName)
                .enterDashboardDescription(RandomDashboarddescri).chooseLandingDate(LandingDate[3])
                .chooseGroupsEdit(Groups[0]).chooseGroupsView(Groups[1]).clickSubmit();

        new LandingPage(DriverFactory.getDriver()).clickEditButton(RandomDashboardName);

        RandomDashboardName = DataUtil.generateRandomString(10);
        RandomDashboarddescri = DataUtil.generateRandomString(10);
        new DashboardPage(DriverFactory.getDriver()).enterDashboardName(RandomDashboardName)
                .enterDashboardDescription(RandomDashboarddescri)
                .chooseLandingDate(LandingDate[2]).clickSubmit();
        Editcheck = new LandingPage(DriverFactory.getDriver()).checkCurrentDashboard(RandomDashboardName);
        Assert.assertTrue(Editcheck);

        
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
        RandomDashboardName = DataUtil.generateRandomString(5);
        new DashboardPage(DriverFactory.getDriver()).enterDashboardName(RandomDashboardName)
                .enterDashboardDescription(DashboardDescription).chooseLandingDate(LandingDate[3])
                .chooseGroupsEdit(Groups[0]).chooseGroupsView(Groups[1]).clickSubmit();

        TitleChecker = new LandingPage(DriverFactory.getDriver()).clickEditButton(RandomDashboardName)
                .checkPageTitle("Edit Dashboard");
        Assert.assertTrue(TitleChecker);

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

        Boolean ValidationMessageChecker;
        List<String> Messages = new ArrayList<String>(3);

        new DashboardPage(DriverFactory.getDriver())
                .enterDashboardName(" ").clickSubmit();
        Messages = General.getValidationMessages(DriverFactory.getDriver());
        ValidationMessageChecker = Messages.contains(DashboardNameMessage);
        Assert.assertTrue(ValidationMessageChecker);

    }

    /*
      @Title: Test case for checking valid dashboard name and description length
      @Steps:
          1. Test with a 2-character dashboard name and verify the error message.
          2. Test with a 51-character dashboard name and verify the error message.
          3. Test with a 1001-character dashboard description and verify the error message.
      @Expected result: Correct error messages are displayed for invalid lengths.
    */
    @Test(groups = {"Invalid"}, priority = 3 ,dependsOnMethods = "TC2_CheckEditDashboardButton"
    ,dataProvider = "ValidationMessagesProvider")
    public void TC4_CheckValidRange(String dashboardName,String dashboardDescrip,String message) {
        boolean LengthChecker;

        List<String> Messages = new ArrayList<String>(3);

        new DashboardPage(DriverFactory.getDriver())
                .enterDashboardName(dashboardName)
                .enterDashboardDescription(dashboardDescrip)
                .clickSubmit();
        Messages = General.getValidationMessages(DriverFactory.getDriver());
        LengthChecker = Messages.contains(message);
        Assert.assertTrue(LengthChecker);;


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
        RandomDashboardName = DataUtil.generateRandomString(10);


        new DashboardPage(DriverFactory.getDriver()).enterDashboardName(RandomDashboardName)
                .chooseGroupsEdit(Groups[1]).chooseGroupsView(Groups[2])
                .chooseGroupsEdit(Groups[3]).chooseGroupsView(Groups[0]);

        new DashboardPage(DriverFactory.getDriver()).clickSubmit();

        new LandingPage(DriverFactory.getDriver()).clickEditButton(RandomDashboardName);
        CounterChecker = new DashboardPage(DriverFactory.getDriver()).CheckAllowedGroups();
        Assert.assertTrue(CounterChecker);

    }

    @AfterMethod
    private void testEnd(){
        Clean(DashboardName);
        Clean(RandomDashboardName);
    }

    @AfterClass(alwaysRun = true)
    private void tearout() {
        DriverFactory.quitDriver();
    }
    @DataProvider(name="ValidationMessagesProvider")

    public Object[][] getDataFromDataprovider(){
        return new Object[][]
                {
                        { DataUtil.generateRandomString(2)," ", DashboardNameMessage },
                        { DataUtil.generateRandomString(51)," ", DashboardNameMessage  },
                        { " ",DataUtil.generateRandomString(1001), DashboarddescriMessage}
                };

    }
}