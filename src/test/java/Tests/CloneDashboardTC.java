package Tests;

import DriverFactory.DriverFactory;
import GeneralClasses.General;
import Pages.DashboardPage;
import Pages.LandingPage;
import Pages.LoginPage;
import Utilities.DataUtil;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.time.Duration;

import static GeneralClasses.General.Clean;

public class CloneDashboardTC {

    private final String ValidUsername = (String) DataUtil.getJsonData("ValidLoginData","username");
    private final String ValidPassword = (String) DataUtil.getJsonData("ValidLoginData","password");
    private final String DashboardName = (String)DataUtil.getJsonData("AddDashboardData","dashboard_name");
    private final String DashboardDescription = (String)DataUtil.getJsonData("AddDashboardData","dashboard_description");
    private final String[] Groups = (String[]) DataUtil.getJsonData("AddDashboardData","groups");
    private final String[] LandingDate = (String[]) DataUtil.getJsonData("AddDashboardData","landing_date");



    private final String LoginPage_URL = DataUtil.getPropertyValue("Environment","LOGIN_URL");
    private final String Browser = DataUtil.getPropertyValue("Environment","BROWSER");

    String RandomDashboardName;


    public CloneDashboardTC() throws IOException {
    }
    
    @BeforeClass(alwaysRun = true)
    private void Start() throws IOException {
        DriverFactory.setupDriver(Browser);
        DriverFactory.getDriver().manage().window().maximize();
        DriverFactory.getDriver().get(LoginPage_URL);
        new LoginPage(DriverFactory.getDriver()).enterUsername(ValidUsername)
                .enterPassword(ValidPassword).clickLogin();
        DriverFactory.getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
    }


    @BeforeMethod(alwaysRun = true)
    private void Setup() throws IOException {
        General.getLandingPage(DriverFactory.getDriver());
        DriverFactory.getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
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
    @Description("TC validate that the user can clone dashboard")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("Mahmoud")
    @Feature("Clone Dashboard")
    @Story("Clone dashboard")
    @Test(groups = {"Valid"} , priority =  1)
    public void TC1_CloneDashboard(){

        boolean CloneCheck;
        RandomDashboardName = DataUtil.generateRandomString(4);
        new DashboardPage(DriverFactory.getDriver()).enterDashboardName(RandomDashboardName)
                .enterDashboardDescription(DashboardDescription).chooseLandingDate(LandingDate[3])
                .chooseGroupsEdit(Groups[0]).chooseGroupsView(Groups[1]).clickSubmit();;

        CloneCheck = new LandingPage(DriverFactory.getDriver()).cloneDashboard(RandomDashboardName)
                    .checkCurrentDashboard("Copy of "+RandomDashboardName);
        Assert.assertTrue(CloneCheck);

    }

    @AfterMethod(alwaysRun = true)
    private void tearout(){
        Clean(DashboardName);
        Clean("Copy of "+RandomDashboardName);
        DriverFactory.quitDriver();
    }
}
