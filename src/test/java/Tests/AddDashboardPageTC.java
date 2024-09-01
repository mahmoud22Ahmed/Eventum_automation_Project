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
    private final String LandPage_URL = DataUtil.getPropertyValue("Environment","LANDING_URL");
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

    @Test(groups = {"Valid"} , priority =  1)
    public void TC1_AddValidDashboard(){
        SoftAssert SAsserter = new SoftAssert();
        boolean AddCheck;
        new AddPage(DriverFactory.getDriver()).enterDashboardName(DashboardName)
                .enterDashboardDescription(DashboardDescription).chooseLandingDate(LandingDate[3])
                .chooseGroupsEdit(Groups[0]).chooseGroupsView(Groups[1]).clickSubmit();;
        AddCheck = new LandingPage(DriverFactory.getDriver()).CheckCurrentDashboard(DashboardName);
        SAsserter.assertTrue(AddCheck);
        SAsserter.assertAll();
    }

    @Test(groups = {"Invalid"} , priority =  1)
    public void TC2_CheckSubmitEmptyForm(){
        SoftAssert SAsserter = new SoftAssert();
        Boolean ValidationMessage;
        ValidationMessage = new AddPage(DriverFactory.getDriver()).clickSubmit().CheckErrorMessage("DashboardName",DashboardNameMessage);
        SAsserter.assertTrue(ValidationMessage);
        SAsserter.assertAll();
    }

    @Test(groups = {"Valid"} , priority =  2)
    public void TC3_CheckSelectCustomRange(){
        SoftAssert SAsserter = new SoftAssert();
        boolean DateChecker ;
        DateChecker = new AddPage(DriverFactory.getDriver()).chooseLandingDate(LandingDate[0])
                .SelectCustomRange(CustomDateStart,CustomDateEnd)
                .CheckCustomRange(CustomDateStart,CustomDateEnd);
        SAsserter.assertTrue(DateChecker);
        LogsUtils.info("it is = "+ DateChecker);
        SAsserter.assertAll();
    }

    @Test(groups = {"Valid"} , priority =  3)
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
