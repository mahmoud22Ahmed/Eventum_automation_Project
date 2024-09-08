package Pages;

import Utilities.LogsUtils;
import Utilities.SelenuimUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class DashboardPage {

    //locetors
    private List<WebElement> Groups = new ArrayList<WebElement>();
    private final By DashboardName = By.xpath("//input[@name = 'name']");
    private final By DashboardDescription = By.xpath("//textarea[contains(@class,'input-text')]");
    private final By GroupsEdit = By.xpath("//input [@placeholder = 'Choose groups that can edit']");
    private final By GroupsView = By.xpath("//input [@placeholder = 'Choose groups that can view only']");
    private final By DatepickerDropdown = By.xpath("//div[@class = 'dashboard-datepicker  ']");
    private final By SubmitButton = By.xpath("//button[text() = 'Submit']");
    private final By CustomRangeStart = By.xpath("//input[@name = 'daterangepicker_start']");
    private final By CustomRangeEnd = By.xpath("//input[@name = 'daterangepicker_end']");
    private final By DatepickerApplyButton = By.xpath("//button[text() = 'Apply']");
    private final By DatepickerValue = By.xpath("//span[@class=\"datepicker\"]");
    private final By AllowedGroupsCounter = By.xpath("//label[contains(text(),'Allowed Groups')]");
    private final String DatepickerElement = "//li[@data-range-key = ";
    private final By GroupRow = By.xpath("//span[@class ='groupName']");

    //driver
    private WebDriver Driver;
    public DashboardPage(WebDriver driver) {
        this.Driver = driver;
    }

    public DashboardPage enterDashboardName(String name){
        SelenuimUtil.clearTextField(Driver,DashboardName);
        SelenuimUtil.sendData(Driver,DashboardName,name);
        return  this;
    }

    public DashboardPage enterDashboardDescription(String name){
        SelenuimUtil.clearTextField(Driver,DashboardDescription);
        SelenuimUtil.sendData(Driver,DashboardDescription,name);
        return this;
    }

    public DashboardPage chooseGroupsEdit(String name){
        SelenuimUtil.clearTextField(Driver,GroupsEdit);
        SelenuimUtil.sendData(Driver,GroupsEdit,name + Keys.ENTER);
        return this;
    }

    public DashboardPage chooseGroupsView(String name){
        SelenuimUtil.clearTextField(Driver,GroupsView );
        SelenuimUtil.sendData(Driver,GroupsView,name + Keys.ENTER);
        return this;
    }

    public DashboardPage chooseLandingDate(String Date){
        By Datepicker = By.xpath(DatepickerElement + "\""+Date+"\"]");
        SelenuimUtil.clickingOnElement(Driver,DatepickerDropdown);
        SelenuimUtil.clickingOnElement(Driver,Datepicker);
        return this;
    }

    public DashboardPage clickSubmit() {
        SelenuimUtil.clickingOnElement(Driver,LandingPage.DashboardPageTitle);
        SelenuimUtil.scrolling(Driver,SubmitButton);
        SelenuimUtil.clickingOnElement(Driver,SubmitButton);
        return this;
    }


    public DashboardPage selectCustomRange(String StartDate , String EndDate){
        SelenuimUtil.clearTextField(Driver,CustomRangeStart);
        SelenuimUtil.sendData(Driver,CustomRangeStart,StartDate + Keys.ENTER);

        SelenuimUtil.clearTextField(Driver,CustomRangeEnd);
        SelenuimUtil.sendData(Driver,CustomRangeEnd,EndDate + Keys.ENTER);
        SelenuimUtil.clearTextField(Driver,CustomRangeEnd);
        SelenuimUtil.sendData(Driver,CustomRangeEnd,EndDate + Keys.ENTER);
        SelenuimUtil.clickingOnElement(Driver,DatepickerApplyButton);
        return this;
    }




    public boolean checkCustomRange(String StartDate ,String EndDate){
        String Date, ExpectedDate = StartDate +" - "+EndDate ;
        Date = SelenuimUtil.getText(Driver,DatepickerValue);
        LogsUtils.info(Date);
        LogsUtils.info(ExpectedDate);
        return Date.equals(ExpectedDate);
    }

    public boolean CheckAllowedGroups(){
        String AllowedGroup;
        int extractedNumber;

        AllowedGroup = SelenuimUtil.getText(Driver,AllowedGroupsCounter);
        String number = AllowedGroup.split(":")[1].trim();
        extractedNumber = Integer.parseInt(number);
        LogsUtils.info("Allowed Groups : " + extractedNumber);
        Groups = Driver.findElements(GroupRow);
        LogsUtils.info("number Groups : " + Groups.size());
        if (Groups.size() == extractedNumber){
            return true;
        }
        return  false;
    }
}
