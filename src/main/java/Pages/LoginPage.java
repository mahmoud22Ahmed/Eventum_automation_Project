package Pages;

import Utilities.DataUtil;
import Utilities.LogsUtils;
import Utilities.SelenuimUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.IOException;
import java.sql.Driver;

public class LoginPage {
    //Locetors
    private final By Username = By.xpath("//input[@name = 'username']");
    private final By Password = By.xpath("//input[@name = 'password']");
    private final By LoginButton = By.xpath("//button[contains(@class,'submitForm')]");
    private final By NavbarTitle  =By.xpath("//div[@class = 'navbar-title headerEllipsis']");;
    private final By popupMessageLocetor = By.xpath("//div[@class = 'alert alert-danger'] /strong");
    //Page driver
    private WebDriver Driver;

    String popupMessage = (String) DataUtil.getJsonData("LoginErrorMessage","popup");

    private String  LandingPageTitle = "Capacity Monitoring System";

    public LoginPage(WebDriver driver) throws IOException {
        this.Driver = driver;
    }

    public LoginPage enterUsername(String username){
        SelenuimUtil.clearTextField(Driver,Username);
        SelenuimUtil.sendData(Driver,Username,username);
        LogsUtils.info("Username:" +username);
        return this;
    }

    public LoginPage enterPassword(String password){
        SelenuimUtil.clearTextField(Driver,Password);
        SelenuimUtil.sendData(Driver,Password,password);
        LogsUtils.info("Password:" +password);
        return  this;
    }

    public LoginPage clickLogin(){
        //SelenuimUtil.clickingOnElement(Driver,LoginButton);
        Driver.findElement(LoginButton).click();
        LogsUtils.info("button is clicked");
        return this;
    }



    public boolean checkLoginSuccess(){
        try {

            SelenuimUtil.generalWait(Driver).until(ExpectedConditions
                    .visibilityOfElementLocated(NavbarTitle));
            return  true;
        }
        catch (TimeoutException e){
            LogsUtils.info("Exception @ :"+ e.getStackTrace());
            return false;
        }
    }

    public boolean checkLoginButtonIsEnable(){
        WebElement Button = Driver.findElement(LoginButton);
        return  Button.isEnabled();
    }

    public boolean checkInvalidLoginPopup(WebDriver driver) {
        String popupMessage;
        try {
            SelenuimUtil.generalWait(driver)
                    .until(ExpectedConditions.visibilityOfElementLocated(popupMessageLocetor));
        } catch (TimeoutException e) {
            LogsUtils.info("expection :"+e.getStackTrace());
            return false;
        }
        popupMessage = SelenuimUtil.getText(Driver, popupMessageLocetor);
        if (popupMessage.equals(popupMessage)){
            return true;
       }
        return false;
    }
}
