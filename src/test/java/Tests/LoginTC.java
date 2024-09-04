package Tests;

import DriverFactory.DriverFactory;
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
    String popupMessage = (String) DataUtil.getJsonData("LoginErrorMessage","popup");
    String emptyPasswordMessage = (String) DataUtil.getJsonData("LoginErrorMessage","empty_password");
    String emptyUsernameMessage = (String) DataUtil.getJsonData("LoginErrorMessage","empty_username");

    //WebDriver chormeDriver =new ChromeDriver();


    public LoginTC() throws IOException {
    }


    @BeforeMethod(alwaysRun = true)
    private void Setup(){
        DriverFactory.setupDriver(Browser);
        DriverFactory.getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
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
        LoginCheck = new LoginPage(DriverFactory.getDriver()).EnterUsername(ValidUsername)
                .EnterPassword(ValidPassword).ClickLogin().CheckLoginSuccess();
        Assert.assertTrue(LoginCheck);

    }

    /*
      @Title : check login with a invalid username and password
      @Steps: 1) enter a invalid username in username field
              2) enter a invalid password in password field
              3)press on login button
              4)repeat it again with only inval
      @Expected result : -the user could not log in to the website
                         -error message will appear
    */
    @Test(groups = {"Invalid"} , priority =  1)
    private void TC2_InvalidLogin() throws InterruptedException, IOException {
        SoftAssert SAsserter = new SoftAssert();
        boolean LoginCheck;
        boolean errorcheck;

        //login with invalid username and password
        LoginCheck = new LoginPage(DriverFactory.getDriver()).EnterUsername(InvalidUsername)
                .EnterPassword(InvalidPassword).ClickLogin().CheckLoginSuccess();
        errorcheck = new LoginPage(DriverFactory.getDriver()).CheckErrorMessage(popup_tag,popupMessage);
        SAsserter.assertFalse(LoginCheck | !errorcheck);

        //login with invalid username and valid password
        LoginCheck = new LoginPage(DriverFactory.getDriver()).EnterUsername(ValidUsername)
                .EnterPassword(InvalidPassword).ClickLogin().CheckLoginSuccess();

        errorcheck = new LoginPage(DriverFactory.getDriver()).CheckErrorMessage(popup_tag,popupMessage);
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

        //login with invalid username and password
        LoginButtonCheck = new LoginPage(DriverFactory.getDriver()).EnterUsername(" ")
                .EnterPassword(" ").checkLoginButton();
        LogsUtils.info("login button checking:" + LoginButtonCheck);
        errorcheck = new LoginPage(DriverFactory.getDriver()).CheckErrorMessage(password_tag,emptyPasswordMessage) &
                new LoginPage(DriverFactory.getDriver()).CheckErrorMessage(username_tag,emptyUsernameMessage);
        LogsUtils.info("error checking:" + errorcheck);
        Assert.assertTrue(!LoginButtonCheck & errorcheck);
    }







    @AfterMethod(alwaysRun = true)
    private void tearout(){
        DriverFactory.quitDriver();
    }
}
