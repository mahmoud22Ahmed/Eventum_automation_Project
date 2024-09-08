package Tests;

import DriverFactory.DriverFactory;
import GeneralClasses.General;
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

public class LoginTC {
    //element tags
    String popup_tag = "popup";
    String username_tag = "username";
    String password_tag = "password";

    //Test Cases Data
    private final String ValidUsername = (String) DataUtil.getJsonData("ValidLoginData","username");
    private final String ValidPassword = (String) DataUtil.getJsonData("ValidLoginData","password");
    private final String InvalidUsername = (String) DataUtil.getJsonData("InvalidLoginData","username");
    private final String InvalidPassword = (String) DataUtil.getJsonData("InvalidLoginData","password");
    private final String LoginPage_URL = DataUtil.getPropertyValue("Environment","LOGIN_URL");
    private final String LandPage_URL = DataUtil.getPropertyValue("Environment","LANDING_URL");
    private final String Browser = DataUtil.getPropertyValue("Environment","BROWSER");

    //error Meesages
    String emptyPasswordMessage = (String) DataUtil.getJsonData("LoginErrorMessage","empty_password");
    String emptyUsernameMessage = (String) DataUtil.getJsonData("LoginErrorMessage","empty_username");

    //WebDriver chormeDriver =new ChromeDriver();


    public LoginTC() throws IOException {
    }


    @BeforeMethod(alwaysRun = true)
    private void Setup(){
        DriverFactory.setupDriver(Browser);
        DriverFactory.getDriver().manage().window().maximize();
        DriverFactory.getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        DriverFactory.getDriver().get(LoginPage_URL);


    }

    /*
       @Title : check login with a valid username and password
       @Steps: 1) enter a valid username in username field
               2) enter a valid password in password field
               3)press on login button
       @Expected result : -the user could log in to the website
                          -directed to landing page
     */
    @Test(groups = {"Valid"} , priority = 1)
    private void TC1_ValidLogin() throws InterruptedException, IOException {
        
        boolean LoginCheck;
        LoginCheck = new LoginPage(DriverFactory.getDriver()).enterUsername(ValidUsername)
                .enterPassword(ValidPassword).clickLogin().checkLoginSuccess();
        Assert.assertTrue(LoginCheck);

    }

    /*
      @Title : check login with a invalid username and password
      @Steps: 1) enter a invalid username in username field
              2) enter a invalid password in password field
              3)press on login button
              4)repeat it again with only invalid password
      @Expected result : -the user could not log in to the website
                         -error message will appear
    */
    @Test(groups = {"Invalid"} , priority =  1)
    private void TC2_InvalidLogin() throws InterruptedException, IOException {
        SoftAssert SAsserter = new SoftAssert();
        boolean LoginCheck;
        boolean errorcheck;

        //login with invalid username and password
        LoginCheck = new LoginPage(DriverFactory.getDriver()).enterUsername(InvalidUsername)
                .enterPassword(InvalidPassword).clickLogin().checkLoginSuccess();
        errorcheck = new LoginPage(DriverFactory.getDriver())
                .checkInvalidLoginPopup(DriverFactory.getDriver());
        SAsserter.assertFalse(LoginCheck | !errorcheck);

        //login with invalid username and valid password
        LoginCheck = new LoginPage(DriverFactory.getDriver()).enterUsername(ValidUsername)
                .enterPassword(InvalidPassword).clickLogin().checkLoginSuccess();

        errorcheck = new LoginPage(DriverFactory.getDriver())
                .checkInvalidLoginPopup(DriverFactory.getDriver());

        SAsserter.assertFalse(LoginCheck | !errorcheck);


        SAsserter.assertAll();
    }


    /*
      @Title : check login with empty data field
      @Steps: 1) make username and password fields empty
              4) try to click on login button
      @Expected result :
                         -errormessage "password is required" in password field
                         -errormessage "username is required" in username field
                         -the login button isn't enable
    */
    @Test(groups = {"Invalid"} , priority =  1)
    private void TC3_EmptyLogin() throws InterruptedException, IOException {
        boolean LoginButtonCheck;
        boolean errorcheck;
        List<String> Messages = new ArrayList<String>(3);

        //login with invalid username and password
        LoginButtonCheck = new LoginPage(DriverFactory.getDriver()).enterUsername(" ")
                .enterPassword(" ").checkLoginButtonIsEnable();
        LogsUtils.info("login button checking:" + LoginButtonCheck);
        Messages = General.getValidationMessages(DriverFactory.getDriver());
        errorcheck = Messages.contains(emptyPasswordMessage)
                & Messages.contains(emptyUsernameMessage);
        LogsUtils.info("error checking:" + errorcheck);
        Assert.assertTrue(!LoginButtonCheck & errorcheck);
    }







    @AfterMethod(alwaysRun = true)
    private void tearout(){
        DriverFactory.quitDriver();
    }
}
