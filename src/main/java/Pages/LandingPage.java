package Pages;

import Utilities.LogsUtils;
import Utilities.SelenuimUtil;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LandingPage {


    //locetors
    private List<WebElement> FindenDashboards = new ArrayList<WebElement>();
    private final By LogoutMenu = By.xpath("//a[@id = 'logout']");
    private final By LogoutButton = By.xpath("//a[contains(text(), 'Logout')]");
    private final By DashboardsDropdownButton = By.xpath("//button[contains(@class ,'navigationBtn-btn')]");
    private final By SeachedDashboardName = By.xpath("//mark[@class = 'matched-word ' ]");
    private final By DropdownSeatchingInput = By.xpath("//input[contains(@class ,'mainTreeSearchBar-search-input')]");
    private final By NewDashboardButton =By.xpath("//button[@id = 'addDashboard']");
    private final By AddDashboardButton = By.xpath("//a[text() = 'Dashboard']");
    private final By AddDashboardPageTitle = By.xpath("//h6[text() = 'New Dashboard']");
    private final By CurrentDashboard = By.xpath("//label[@class = 'navBtnText']");
    private WebDriver Driver;

    public LandingPage(WebDriver driver){
        this.Driver = driver;
    }

    public LoginPage PressOnLogout() throws IOException {
        Driver.findElement(LogoutMenu).click();
        Driver.findElement(LogoutButton).click();
        return new LoginPage(Driver);
    }

    public boolean SearchInDashboardDropdown(String DashboardName){
        String CurrentDashboard;

        SelenuimUtil.clickingOnElement(Driver,DashboardsDropdownButton);
        SelenuimUtil.sendData(Driver,DropdownSeatchingInput,DashboardName + Keys.ENTER);
        //Driver.findElement(DropdownSeatchingInput).sendKeys(DashboardName + Keys.ENTER);
        try {
            SelenuimUtil.generalWait(Driver).until(ExpectedConditions.visibilityOfElementLocated(SeachedDashboardName));
        }catch (Exception e){
            LogsUtils.info("exception at SearchInDashboardDropdown");
            return false;
        }
        FindenDashboards = Driver.findElements(SeachedDashboardName);
        for (WebElement Dashboard:FindenDashboards) {
            CurrentDashboard = Dashboard.getText();
            LogsUtils.info(CurrentDashboard);
            if (CurrentDashboard.equals(DashboardName)) {
                return true;
            }
        }
        LogsUtils.info("not found");
        return false;

    }

    public LandingPage clickAddDashboard(){
        Driver.findElement(NewDashboardButton).click();
        Driver.findElement(AddDashboardButton).click();
        return this;
    }
    public boolean CheckPageTitle(String PageTitle){
        String ActualPageTitle;
        ActualPageTitle = SelenuimUtil.getText(Driver,AddDashboardPageTitle);
        LogsUtils.info(ActualPageTitle);
        return   ActualPageTitle.equals(PageTitle);
    }

    public boolean CheckCurrentDashboard(String name){
        String DashboardName;
        DashboardName = SelenuimUtil.getText(Driver,CurrentDashboard);
        LogsUtils.info(DashboardName);
        return DashboardName.equals(name);
    }

}
