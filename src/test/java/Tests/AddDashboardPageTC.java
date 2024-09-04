package Tests;

import DriverFactory.DriverFactory;
import Pages.AddPage;
import Pages.LandingPage;
import Pages.LoginPage;
import Utilities.DataUtil;
import Utilities.LogsUtils;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import static HelperFn.Helper.Clean;

import java.io.IOException;
import java.time.Duration;

public class AddDashboardPageTC {

    private final String ValidUsername = (String) DataUtil.getJsonData("ValidLoginData","username");
    private final String ValidPassword = (String) DataUtil.getJsonData("ValidLoginData","password");
    private final String DashboardName = (String)DataUtil.getJsonData("AddDashboardData","dashboard_name");
    private final String DashboardDescription = (String)DataUtil.getJsonData("AddDashboardData","dashboard_description");
    private final String[] Groups = (String[]) DataUtil.getJsonData("AddDashboardData","groups");
    private final String[] LandingDate = (String[]) DataUtil.getJsonData("AddDashboardData","landing_date");
    private final String CustomDateStart = (String) DataUtil.getJsonData("AddDashboardData","custom_range_start");
    private final String CustomDateEnd = (String) DataUtil.getJsonData("AddDashboardData","custom_range_end");

    private final String LoginPage_URL = DataUtil.getPropertyValue("Environment","LOGIN_URL");
    private final String Browser = DataUtil.getPropertyValue("Environment","BROWSER");

    //error Messages
    private final String DashboardNameMessage = (String) DataUtil.getJsonData("AddDashboardErrorMessage","dashboard_name");
    private final String DashboarddescriMessage = (String)DataUtil.getJsonData("AddDashboardErrorMessage","dashboard_description");
    public AddDashboardPageTC() throws IOException {
    }

    @BeforeMethod(alwaysRun = true)
    private void Setup() throws IOException {
        DriverFactory.setupDriver(Browser);
        DriverFactory.getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        DriverFactory.getDriver().get(LoginPage_URL);
        new LoginPage(DriverFactory.getDriver()).EnterUsername(ValidUsername)
                      .EnterPassword(ValidPassword).ClickLogin();
        new LandingPage(DriverFactory.getDriver()).clickAddDashboard();

    }

    /*
    @Title: Test case for adding a valid dashboard
    @Steps:
        1. Enter valid dashboard name
        2. Enter valid dashboard description
        3. Choose valid landing date
        4. Choose valid groups for edit and view
        5. Submit the form
        6. Verify the dashboard is added
    @Expected result: Dashboard successfully added and verified on the landing page
   */
    @Test(groups = {"Valid"} , priority =  2 ,dependsOnMethods = "TC_CheckAddDashboardButton")
    public void TC1_AddValidDashboard(){

        boolean AddCheck;
        new AddPage(DriverFactory.getDriver()).enterDashboardName(DashboardName)
                .enterDashboardDescription(DashboardDescription).chooseLandingDate(LandingDate[3])
                .chooseGroupsEdit(Groups[0]).chooseGroupsView(Groups[1]).clickSubmit();;
        AddCheck = new LandingPage(DriverFactory.getDriver()).CheckCurrentDashboard(DashboardName);

        Assert.assertTrue(AddCheck);
        Clean(DashboardName);

    }

    @Test(groups = {"Valid"},priority = 1)
    public void TC_CheckAddDashboardButton(){
        boolean PageTitle;
        PageTitle =new LandingPage(DriverFactory.getDriver())
                .CheckPageTitle("New Dashboard");
        Assert.assertTrue(PageTitle);
    }

    /*
    @Title: Test case for checking validation messages with an empty form
    @Steps:
        1. Attempt to submit the form without filling any fields
        2. Verify the error message for the dashboard name field
    @Expected result: Correct error message is displayed for the empty dashboard name field
   */
    @Test(groups = {"Invalid"} , priority =  3,dependsOnMethods = "TC_CheckAddDashboardButton")
    public void TC2_CheckSubmitEmptyForm(){

        Boolean ValidationMessage;
        ValidationMessage = new AddPage(DriverFactory.getDriver()).clickSubmit().CheckErrorMessage("DashboardName",DashboardNameMessage);
        Assert.assertTrue(ValidationMessage);
    }


    /*
        @Title: Test case for selecting a custom date range
        @Steps:
            1. Choose a landing date
            2. Select a custom date range
            3. Verify the selected custom date range is correct
        @Expected result: Custom date range is correctly applied and verified
    */
    @Test(groups = {"Valid"} , priority =  3,dependsOnMethods = "TC_CheckAddDashboardButton")
    public void TC3_CheckSelectCustomRange(){

        boolean DateChecker ;
        DateChecker = new AddPage(DriverFactory.getDriver()).chooseLandingDate(LandingDate[0])
                .SelectCustomRange(CustomDateStart,CustomDateEnd)
                .CheckCustomRange(CustomDateStart,CustomDateEnd);
        LogsUtils.info("it is = "+ DateChecker);
        Assert.assertTrue(DateChecker);
    }

    /*
    @Title: Test case for checking valid dashboard name and description length
    @Steps:
        1. Test with a 2-character dashboard name and verify the error message
        2. Test with a 51-character dashboard name and verify the error message
        3. Test with a 1001-character dashboard description and verify the error message
    @Expected result: Correct error messages are displayed for invalid lengths
    */
    @Test(groups = {"Valid"} , priority =  3 ,dependsOnMethods = "TC_CheckAddDashboardButton")
    public void TC4_CheckValidRange(){
        SoftAssert SAsserter = new SoftAssert();
        boolean LengthChecker;
        String Randomstr;
        Randomstr = DataUtil.generateRandomString(2);
        LogsUtils.info("Test Dashboard name with 2 characters");
        LengthChecker = new AddPage(DriverFactory.getDriver())
                    .enterDashboardName(Randomstr)
                    .clickSubmit().CheckErrorMessage("DashboardName", DashboardNameMessage);
        SAsserter.assertTrue(LengthChecker);


        try {
            Randomstr = DataUtil.generateRandomString(51);
            LogsUtils.info("Test Dashboard name with 51 characters");
            LengthChecker = new AddPage(DriverFactory.getDriver())
                    .enterDashboardName(Randomstr)
                    .clickSubmit().CheckErrorMessage("DashboardName", DashboardNameMessage);
            SAsserter.assertTrue(LengthChecker);
        }catch (Exception e){
            LogsUtils.info("get again to Add page");
            new LandingPage(DriverFactory.getDriver()).clickAddDashboard();
            Randomstr = DataUtil.generateRandomString(51);
            LogsUtils.info("Test Dashboard name with 51 characters");
            LengthChecker = new AddPage(DriverFactory.getDriver())
                    .enterDashboardName(Randomstr)
                    .clickSubmit().CheckErrorMessage("DashboardName", DashboardNameMessage);
            SAsserter.assertTrue(LengthChecker);
        }

        Randomstr = DataUtil.generateRandomString(1001);
        LogsUtils.info("Test Dashboard dashboard with 1001 characters");
        LengthChecker = new AddPage(DriverFactory.getDriver())
                .enterDashboardDescription(Randomstr)
                .clickSubmit().CheckErrorMessage("DashboardDescription",DashboarddescriMessage);
        SAsserter.assertTrue(LengthChecker);

        SAsserter.assertAll();

    }


    @AfterMethod(alwaysRun = true)
    private void tearout(){
        DriverFactory.quitDriver();
    }
}
