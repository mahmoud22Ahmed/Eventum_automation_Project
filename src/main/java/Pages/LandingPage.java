package Pages;

import Utilities.LogsUtils;
import Utilities.SelenuimUtil;



import org.openqa.selenium.*;
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
    private final By SeachedDashboardName = By.xpath("//a[@class = 'item-inactive-bg-color']");
    private final By DropdownSeatchingInput = By.xpath("//input[contains(@class ,'mainTreeSearchBar-search-input')]");
    private final By NewDashboardButton =By.id("addDashboard");
    private final By AddDashboardButton = By.xpath("//a[text() = 'Dashboard']");
    public static final By DashboardPageTitle = By.xpath("//h6[@class ='title-text']");
    private final By CurrentDashboard = By.xpath("//label[@class = 'navBtnText']");
    private final By ActionsMenuButton = By.id("designSpaceActionMenu");
    private final By DeleteDashboardButton = By.id("deleteDesignSpace");
    private final By EditDashboardButton = By.id("editDesignSpace");
    private final By CloneDashboardButton = By.id("cloneDesignSpace");
    private final By DeletePopupYesButton = By.xpath("//button[text() = 'Yes']") ;
    private final By FavoriteButton = By.xpath("//button[contains(@class,'favUnfav')]");
    private final By NavbarTitle  =By.xpath("//div[@class = 'navbar-title headerEllipsis']");


    private WebDriver Driver;

    public LandingPage(WebDriver driver){
        this.Driver = driver;
    }

    public LoginPage pressOnLogout() throws IOException {
        Driver.findElement(LogoutMenu).click();
        Driver.findElement(LogoutButton).click();
        return new LoginPage(Driver);
    }

    private WebElement searchOnDashboard(String DashboardName){
        String CurrentDashboard;
        SelenuimUtil.clickingOnElement(Driver,DashboardsDropdownButton);
        SelenuimUtil.clearTextField(Driver,DropdownSeatchingInput);
        SelenuimUtil.sendData(Driver,DropdownSeatchingInput,DashboardName + Keys.ENTER);
        try {
            // Wait until dashboards are visible and there are more than 2
            SelenuimUtil.generalWait(Driver).until(driver -> {
                FindenDashboards = driver.findElements(SeachedDashboardName);
                // Check if there are more than 2 dashboards and they are visible
                return !FindenDashboards.get(FindenDashboards.size() - 1).getText().equals("Default")
                        && FindenDashboards.stream().allMatch(WebElement::isDisplayed);
            });
            // SelenuimUtil.generalWait(Driver).until(ExpectedConditions.visibilityOfElementLocated(SeachedDashboardName));
        }catch (TimeoutException e ){
            LogsUtils.info("Exception @ :"+ e.getStackTrace());
            return null;
        }
        FindenDashboards = Driver.findElements(SeachedDashboardName);
        for (WebElement Dashboard:FindenDashboards) {
            CurrentDashboard = Dashboard.getText();
            LogsUtils.info(CurrentDashboard);
            if (CurrentDashboard.equals(DashboardName)) {
                return  Dashboard;
            }
        }
        LogsUtils.info("not found");
        return null;
    }

    public boolean checkSearchinDashboardDropdown(String DashboardName){
        WebElement FindenDashboard;
        FindenDashboard = searchOnDashboard(DashboardName);

        if (FindenDashboard == null){
            return false  ;
        }

        if (FindenDashboard.getText().equals(DashboardName))
        {
            return true;
        }
        else {
            return false;
        }


    }

    public LandingPage clickAddDashboard(){
        Driver.findElement(NewDashboardButton).click();
        Driver.findElement(AddDashboardButton).click();
        return this;
    }

    public LandingPage clickEditButton(String DashboardName){
        WebElement DesiredDashboard;
        DesiredDashboard = searchOnDashboard(DashboardName);
        if (DesiredDashboard == null){
            LogsUtils.info("there is no dashboard with this name");
            throw new RuntimeException("the dashboard isn't find");
        }
        DesiredDashboard.click();
        SelenuimUtil.clickingOnElement(Driver,ActionsMenuButton);
        SelenuimUtil.clickingOnElement(Driver,EditDashboardButton);
        return this;
    }

    public LandingPage cloneDashboard(String DashboardName){
        SelenuimUtil.clickingOnElement(Driver,ActionsMenuButton);
        SelenuimUtil.clickingOnElement(Driver,CloneDashboardButton);
        Driver.findElement(NavbarTitle).click();
        return this;
    }

    public LandingPage clickFavoriteButton(String DashboardName){
        WebElement DesiredDashboard;
        SelenuimUtil.clickingOnElement(Driver,FavoriteButton);
        return this;
    }

    public boolean checkFavoriteDashboard(String DashboardName){
        String CurrentDashboard;

       // Driver.navigate().refresh();

        SelenuimUtil.clickingOnElement(Driver,DashboardsDropdownButton);
        SelenuimUtil.clearTextField(Driver,DropdownSeatchingInput);
        SelenuimUtil.sendData(Driver,DropdownSeatchingInput,""+Keys.ENTER);
        
        try {
            SelenuimUtil.generalWait(Driver).until(ExpectedConditions.visibilityOfElementLocated(SeachedDashboardName));
        }catch (TimeoutException e){
            LogsUtils.info("Exception @ :"+ e.getStackTrace());
            return false;
        }
        FindenDashboards = Driver.findElements(SeachedDashboardName);
        LogsUtils.info("loop start");

        for (WebElement Dashboard:FindenDashboards){
            CurrentDashboard=Dashboard.getText();
            LogsUtils.info(CurrentDashboard);

            if (CurrentDashboard.equals(DashboardName)){
                return true;
            }
            else if (CurrentDashboard.equals("Default")){
                return false;
            }

        }
        return false;
    }

    public void deleteCurrentDashboard(){
        SelenuimUtil.clickingOnElement(Driver, ActionsMenuButton);
        SelenuimUtil.clickingOnElement(Driver, DeleteDashboardButton);
        SelenuimUtil.clickingOnElement(Driver, DeletePopupYesButton);
    }





    public boolean deleteDashboard(String DashboardName){
        int NumberOfDashboards;
        WebElement DesiredDashboard;
        DesiredDashboard = searchOnDashboard(DashboardName);
        if (DesiredDashboard == null){
            LogsUtils.info("there is no dashboard with this name");
            return false;
        }
        FindenDashboards = Driver.findElements(SeachedDashboardName);
        NumberOfDashboards = FindenDashboards.size();
        LogsUtils.info("before deleting"+NumberOfDashboards);
        DesiredDashboard.click();
        SelenuimUtil.clickingOnElement(Driver,ActionsMenuButton);
        SelenuimUtil.clickingOnElement(Driver,DeleteDashboardButton);
        SelenuimUtil.clickingOnElement(Driver,DeletePopupYesButton);
        searchOnDashboard(DashboardName);
        FindenDashboards = Driver.findElements(SeachedDashboardName);
        LogsUtils.info("after deleting"+FindenDashboards.size());

        if (FindenDashboards.size() <= NumberOfDashboards -1 ){
            return true;
        }
        else {
            return false;
        }

    }

    public boolean checkPageTitle(String PageTitle){
        String ActualPageTitle;
        ActualPageTitle = SelenuimUtil.getText(Driver, DashboardPageTitle);
        LogsUtils.info(ActualPageTitle);
        return   ActualPageTitle.equals(PageTitle);
    }

    public boolean checkCurrentDashboard(String name){
        String DashboardName;
        DashboardName = SelenuimUtil.getText(Driver,CurrentDashboard);
        LogsUtils.info(DashboardName);
        return DashboardName.equals(name);
    }


}
