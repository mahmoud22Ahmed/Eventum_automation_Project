package Tests;

import DriverFactory.DriverFactory;
import Pages.AddPage;
import Pages.LandingPage;
import Pages.LoginPage;
import Utilities.DataUtil;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.time.Duration;

import static HelperFn.Helper.Clean;

public class CloneDashboardTC {

    private final String ValidUsername = (String) DataUtil.getJsonData("ValidLoginData","username");
    private final String ValidPassword = (String) DataUtil.getJsonData("ValidLoginData","password");
    private final String DashboardName = (String)DataUtil.getJsonData("AddDashboardData","dashboard_name");
    private final String DashboardDescription = (String)DataUtil.getJsonData("AddDashboardData","dashboard_description");
    private final String[] Groups = (String[]) DataUtil.getJsonData("AddDashboardData","groups");
    private final String[] LandingDate = (String[]) DataUtil.getJsonData("AddDashboardData","landing_date");



    private final String LoginPage_URL = DataUtil.getPropertyValue("Environment","LOGIN_URL");
    private final String Browser = DataUtil.getPropertyValue("Environment","BROWSER");


    public CloneDashboardTC() throws IOException {
    }

    @BeforeMethod(alwaysRun = true)
    private void Setup() throws IOException {
        DriverFactory.setupDriver(Browser);
        DriverFactory.getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        DriverFactory.getDriver().get(LoginPage_URL);
        new LoginPage(DriverFactory.getDriver()).EnterUsername(ValidUsername)
                .enterPassword(ValidPassword).clickLogin();
        new LandingPage(DriverFactory.getDriver()).clickAddDashboard();
    }

    /*
    @Title: Test case for Clone Dashboard
    @Steps:
        1. Add a valid dashboard
        2. press on Clone Dashboard
        3. Check the name of the dashboardd
    @Expected result:
                1. It must clone succcessfully
                2. the name of the duplicted dashboard become Copy of ..
   */
    @Test(groups = {"Valid"} , priority =  1)
    public void TC1_CloneDashboard(){

        boolean CloneCheck;
        String RandomDashboardName = DataUtil.generateRandomString(4);
        new AddPage(DriverFactory.getDriver()).enterDashboardName(RandomDashboardName)
                .enterDashboardDescription(DashboardDescription).chooseLandingDate(LandingDate[3])
                .chooseGroupsEdit(Groups[0]).chooseGroupsView(Groups[1]).clickSubmit();;

        CloneCheck = new LandingPage(DriverFactory.getDriver()).CloneDashboard(RandomDashboardName)
                    .CheckCurrentDashboard("Copy of "+RandomDashboardName);
        Assert.assertTrue(CloneCheck);
        Clean(DashboardName);
        Clean("Copy of "+RandomDashboardName);
    }

    @AfterMethod(alwaysRun = true)
    private void tearout(){
        DriverFactory.quitDriver();
    }
}
