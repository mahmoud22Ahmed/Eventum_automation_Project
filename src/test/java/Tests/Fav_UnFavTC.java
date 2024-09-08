package Tests;

import DriverFactory.DriverFactory;
import GeneralClasses.General;
import Pages.DashboardPage;
import Pages.LandingPage;
import Pages.LoginPage;
import Utilities.DataUtil;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.time.Duration;

import static GeneralClasses.General.Clean;

public class Fav_UnFavTC
{
    private final String ValidUsername = (String) DataUtil.getJsonData("ValidLoginData","username");
    private final String ValidPassword = (String) DataUtil.getJsonData("ValidLoginData","password");
    private final String DashboardDescription = (String)DataUtil.getJsonData("AddDashboardData","dashboard_description");
    private final String[] Groups = (String[]) DataUtil.getJsonData("AddDashboardData","groups");
    private final String[] LandingDate = (String[]) DataUtil.getJsonData("AddDashboardData","landing_date");
    String RandomDashboradName;



    private final String LoginPage_URL = DataUtil.getPropertyValue("Environment","LOGIN_URL");
    private final String Browser = DataUtil.getPropertyValue("Environment","BROWSER");


    public Fav_UnFavTC() throws IOException {
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
    @Title: Test case for make any Dashboard Favorite
    @Steps:
        1. Add a valid dashboard
        2. Make the dashboard Favorite
        3. Verify  favorite Dashboards list
    @Expected result: it must become  favorite
   */
    @Test(groups = {"Valid"} , priority =  1)
    public void TC1_FavDashboard(){

        boolean FavCheck;
        RandomDashboradName = DataUtil.generateRandomString(10);
        new DashboardPage(DriverFactory.getDriver()).enterDashboardName(RandomDashboradName)
                .enterDashboardDescription(DashboardDescription).chooseLandingDate(LandingDate[3])
                .chooseGroupsEdit(Groups[0]).chooseGroupsView(Groups[1]).clickSubmit();;

        FavCheck = new LandingPage(DriverFactory.getDriver())
                .clickFavoriteButton(RandomDashboradName).checkFavoriteDashboard(RandomDashboradName);
        Assert.assertTrue(FavCheck);


    }


    /*
    @Title: Test case for make any Dashboard un Favorite
    @Steps:
        1. Add a valid dashboard
        2. Make the dashboard Favorite
        3. Make it un Favorite
        3. Verify  favorite Dashboards list

    @Expected result: it must become  unfavorite
   */
    @Test(groups = {"Invalid"} , priority =  1)
    public void TC2_UnFavDashboard(){

        boolean UnFavCheck;
        RandomDashboradName = DataUtil.generateRandomString(10);
        new DashboardPage(DriverFactory.getDriver()).enterDashboardName(RandomDashboradName)
                .enterDashboardDescription(DashboardDescription).chooseLandingDate(LandingDate[3])
                .chooseGroupsEdit(Groups[0]).chooseGroupsView(Groups[1]).clickSubmit();

        new LandingPage(DriverFactory.getDriver())
                .clickFavoriteButton(RandomDashboradName);

        UnFavCheck = new LandingPage(DriverFactory.getDriver())
                .clickFavoriteButton(RandomDashboradName).checkFavoriteDashboard(RandomDashboradName);

        Assert.assertFalse(UnFavCheck);

    }

    @AfterMethod(alwaysRun = true)
    private void testEnd(){
        Clean(RandomDashboradName);
    }
    
    @AfterClass(alwaysRun = true)
    private void tearout(){

        DriverFactory.quitDriver();
    }


}
