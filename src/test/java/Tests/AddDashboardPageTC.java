package Tests;

import DriverFactory.DriverFactory;
import GeneralClasses.General;
import Pages.DashboardPage;
import Pages.LandingPage;
import Pages.LoginPage;
import Utilities.DataUtil;
import Utilities.LogsUtils;
import org.testng.Assert;
import org.testng.annotations.*;

import static GeneralClasses.General.Clean;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

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
        new DashboardPage(DriverFactory.getDriver()).enterDashboardName(DashboardName)
                .enterDashboardDescription(DashboardDescription).chooseLandingDate(LandingDate[3])
                .chooseGroupsEdit(Groups[0]).chooseGroupsView(Groups[1]).clickSubmit();;
        AddCheck = new LandingPage(DriverFactory.getDriver()).checkCurrentDashboard(DashboardName);

        Assert.assertTrue(AddCheck);


    }

    @Test(groups = {"Valid"},priority = 1)
    public void TC_CheckAddDashboardButton(){
        boolean PageTitle;
        PageTitle =new LandingPage(DriverFactory.getDriver())
                .checkPageTitle("New Dashboard");
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

        Boolean ValidationMessageChecker;
        List<String> Messages = new ArrayList<String>(3);

        new DashboardPage(DriverFactory.getDriver())
                .enterDashboardName(" ").clickSubmit();
        Messages = General.getValidationMessages(DriverFactory.getDriver());
        ValidationMessageChecker = Messages.contains(DashboardNameMessage);
        Assert.assertTrue(ValidationMessageChecker);

    }


    /*
        @Title: Test case for selecting a custom date range
        @Steps:
            1. Choose a landing date
            2. Select a custom date range
            3. Verify the selected custom date range is correct
        @Expected result: Custom date range is correctly applied and verified
    */
    @Test(groups = {"Valid"} , priority =  3
            ,dependsOnMethods = "TC_CheckAddDashboardButton" )
    public void TC3_CheckSelectCustomRange(){

        boolean DateChecker ;
        DateChecker = new DashboardPage(DriverFactory.getDriver()).chooseLandingDate(LandingDate[0])
                .selectCustomRange(CustomDateStart,CustomDateEnd)
                .checkCustomRange(CustomDateStart,CustomDateEnd);
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
    @Test(groups = {"Valid"} , priority =  3 ,dependsOnMethods = "TC_CheckAddDashboardButton"
            ,dataProvider = "ValidationMessagesProvider")
    public void TC4_CheckValidRange(String dashboardName
            ,String dashboardDescrip,String message){
        //SoftAssert SAsserter = new SoftAssert();
        boolean LengthChecker;
        String Randomstr;
        List<String> Messages = new ArrayList<String>(3);
        Randomstr = DataUtil.generateRandomString(2);
        LogsUtils.info("Test Dashboard name with 2 characters");

        new DashboardPage(DriverFactory.getDriver())
                .enterDashboardName(dashboardName)
                .enterDashboardDescription(dashboardDescrip)
                .clickSubmit();
        Messages = General.getValidationMessages(DriverFactory.getDriver());
        LengthChecker = Messages.contains(message);
        Assert.assertTrue(LengthChecker);


    }

    @AfterMethod
    private void testEnd(){
            Clean(DashboardName);
    }

    @AfterClass(alwaysRun = true)
    private void tearout(){
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
