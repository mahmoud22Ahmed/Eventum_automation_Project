package Pages;

import Utilities.LogsUtils;
import Utilities.SelenuimUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.IOException;

public class LoginPage {
    //Locetors
    private final By Username = By.xpath("//input[@name = 'username']");
    private final By Password = By.xpath("//input[@name = 'password']");
    private final By LoginButton = By.xpath("//button[contains(@class,'submitForm')]");
    private final By popupMessageLocetor = By.xpath("//div[@class = 'alert alert-danger'] /strong");
    private final By usernameMessageLocetor = By.xpath("(//span[@class = 'warning-span '])[1]");
    private final By passwordMessageLocetor = By.xpath("(//span[@class = 'warning-span '])[2]");
    private final By NavbarTitle  =By.xpath("//div[@class = 'navbar-title headerEllipsis']");;
    //Page driver
    private WebDriver Driver;

    private String  LandingPageTitle = "Capacity Monitoring System";

    public LoginPage(WebDriver driver) throws IOException {
        this.Driver = driver;
    }

    public LoginPage EnterUsername(String username){
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

    public boolean checkLoginCurrentURL(String ExpectedURL) throws InterruptedException {
        String CurrentURL;
        try {
            SelenuimUtil.generalWait(Driver).until(ExpectedConditions.urlToBe(ExpectedURL));
        }catch (Exception e){
              return false;
        }

        CurrentURL = Driver.getCurrentUrl();
        LogsUtils.info("CurrentURL: "+ CurrentURL);
        return CurrentURL.equals(ExpectedURL);

    }

    public boolean checkLoginSuccess(){
        try {

            SelenuimUtil.generalWait(Driver).until(ExpectedConditions
                    .visibilityOfElementLocated(NavbarTitle));
        }
        catch (Exception e){
            return false;
        }
        String CurrentTitle = SelenuimUtil.getText(Driver,NavbarTitle);
        if (CurrentTitle.equals(LandingPageTitle)){
             return true;
        }
        return false;
    }

    public boolean checkErrorMessage(String element, String expectedMessage){
        String Message;
        switch (element){
            case "popup":
                Message = Driver.findElement(popupMessageLocetor).getText();
                break;
            case "password":
                Message = Driver.findElement(passwordMessageLocetor).getText();
                break;
            case "username":
                Message = Driver.findElement(usernameMessageLocetor).getText();
                break;
            default:
                Message = "not found";
                break;
        }

        LogsUtils.info("error message: " + Message);
        return Message.equals(expectedMessage);
    }

    public boolean checkLoginButtonIsEnable(){
        WebElement Button = Driver.findElement(LoginButton);
        return  Button.isEnabled();
    }
}
