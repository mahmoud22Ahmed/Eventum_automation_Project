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

public class Fav_UnFavTC
{
    private final String ValidUsername = (String) DataUtil.getJsonData("ValidLoginData","username");
    private final String ValidPassword = (String) DataUtil.getJsonData("ValidLoginData","password");
    private final String DashboardDescription = (String)DataUtil.getJsonData("AddDashboardData","dashboard_description");
    private final String[] Groups = (String[]) DataUtil.getJsonData("AddDashboardData","groups");
    private final String[] LandingDate = (String[]) DataUtil.getJsonData("AddDashboardData","landing_date");



    private final String LoginPage_URL = DataUtil.getPropertyValue("Environment","LOGIN_URL");
    private final String Browser = DataUtil.getPropertyValue("Environment","BROWSER");


    public Fav_UnFavTC() throws IOException {
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
    @Title: Test case for make any Dashboard Favorite
    @Steps:
        1. Add a valid dashboard
        2. Make the dashboard Favorite
        3. Verify  favorite Dashboards list
    @Expected result: it must become  favorite
   */
    @Test(groups = {"Valid"} , priority =  1)
    public void TC1_FavDashboard(){

        boolean CloneCheck;
        String RandomDashboradName = DataUtil.generateRandomString(10);
        new AddPage(DriverFactory.getDriver()).enterDashboardName(RandomDashboradName)
                .enterDashboardDescription(DashboardDescription).chooseLandingDate(LandingDate[3])
                .chooseGroupsEdit(Groups[0]).chooseGroupsView(Groups[1]).clickSubmit();;

        CloneCheck = new LandingPage(DriverFactory.getDriver())
                .ClickFavoriteButton(RandomDashboradName).CheckFavoriteDashboard(RandomDashboradName);
        Assert.assertTrue(CloneCheck);
        Clean(RandomDashboradName);

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

        boolean CloneCheck;
        String RandomDashboradName = DataUtil.generateRandomString(10);
        new AddPage(DriverFactory.getDriver()).enterDashboardName(RandomDashboradName)
                .enterDashboardDescription(DashboardDescription).chooseLandingDate(LandingDate[3])
                .chooseGroupsEdit(Groups[0]).chooseGroupsView(Groups[1]).clickSubmit();

        new LandingPage(DriverFactory.getDriver())
                .ClickFavoriteButton(RandomDashboradName);

        CloneCheck = new LandingPage(DriverFactory.getDriver())
                .ClickFavoriteButton(RandomDashboradName).CheckFavoriteDashboard(RandomDashboradName);

        Assert.assertFalse(CloneCheck);
        Clean(RandomDashboradName);
    }

    @AfterMethod(alwaysRun = true)
    private void tearout(){

        DriverFactory.quitDriver();
    }


}
