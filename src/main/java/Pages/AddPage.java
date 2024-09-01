package Pages;

import Utilities.LogsUtils;
import Utilities.SelenuimUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

public class AddPage {

    //locetors
    private final By DashboardName = By.xpath("//input[@name = 'name']");
    private final By DashboardDescription = By.xpath("//textarea[contains(@class,'input-text')]");
    private final By GroupsEdit = By.xpath("(//div[contains(@class,'react-tags__search-input')] /input)[1]");
    private final By GroupsView = By.xpath("(//div[contains(@class,'react-tags__search-input')] /input)[2]");
    private final By DatepickerDropdown = By.xpath("//div[@class = 'dashboard-datepicker  ']");
    private final By SubmitButton = By.xpath("//button[text() = 'Submit']");
    private final By DashboardNameMessageLocetor = By.xpath("//div[contains(@class,'name')] //span[@class = 'warning-span ']");
    private final By DashboarddescriMessageLocetor = By.xpath("//div[contains(@class,'descri')] //span[@class = 'warning-span ']");
    private final By CustomRangeStart = By.xpath("//input[@name = 'daterangepicker_start']");
    private final By CustomRangeEnd = By.xpath("//input[@name = 'daterangepicker_end']");
    private final By DatepickerApplyButton = By.xpath("//button[text() = 'Apply']");
    private final By DatepickerValue = By.xpath("//span[@class=\"datepicker\"]");
    private final String DatepickerElement = "//li[@data-range-key = ";


    //driver
    private WebDriver Driver;

    public AddPage(WebDriver driver) {
        this.Driver = driver;
    }

    public AddPage enterDashboardName(String name){
        SelenuimUtil.clearTextField(Driver,DashboardName);
        SelenuimUtil.sendData(Driver,DashboardName,name);
        return  this;
    }

    public AddPage enterDashboardDescription(String name){
        SelenuimUtil.clearTextField(Driver,DashboardDescription);
        SelenuimUtil.sendData(Driver,DashboardDescription,name);
        return this;
    }

    public AddPage chooseGroupsEdit(String name){
        SelenuimUtil.clearTextField(Driver,GroupsEdit);
        SelenuimUtil.sendData(Driver,GroupsEdit,name + Keys.ENTER);
        return this;
    }

    public AddPage chooseGroupsView(String name){
        SelenuimUtil.clearTextField(Driver,GroupsView );
        SelenuimUtil.sendData(Driver,GroupsView,name + Keys.ENTER);
        return this;
    }

    public AddPage chooseLandingDate(String Date){
        By Datepicker = By.xpath(DatepickerElement + "\""+Date+"\"]");
        SelenuimUtil.clickingOnElement(Driver,DatepickerDropdown);
        SelenuimUtil.clickingOnElement(Driver,Datepicker);
        return this;
    }

    public AddPage clickSubmit() {
        SelenuimUtil.clickingOnElement(Driver,SubmitButton);
        return this;
    }

    public AddPage SelectCustomRange(String StartDate ,String EndDate){
        SelenuimUtil.clearTextField(Driver,CustomRangeStart);
        SelenuimUtil.sendData(Driver,CustomRangeStart,StartDate + Keys.ENTER);

        SelenuimUtil.clearTextField(Driver,CustomRangeEnd);
        SelenuimUtil.sendData(Driver,CustomRangeEnd,EndDate + Keys.ENTER);
        SelenuimUtil.clearTextField(Driver,CustomRangeEnd);
        SelenuimUtil.sendData(Driver,CustomRangeEnd,EndDate + Keys.ENTER);
        SelenuimUtil.clickingOnElement(Driver,DatepickerApplyButton);
        return this;
    }

    public boolean CheckErrorMessage(String element,String expectedMessage){
        String Message = "Not Found";
        switch (element){
            case "DashboardName":
                try {
                  Message = SelenuimUtil.getText(Driver,DashboardNameMessageLocetor);
                }
                catch (Exception e){
                    LogsUtils.info("the error message in Dashboard name isn't appeared");
                    return false;
                }
                break;
            case "DashboardDescription":
                try {
                    Message = SelenuimUtil.getText(Driver, DashboarddescriMessageLocetor);
                }
                catch (Exception e){
                    LogsUtils.info("the error message in Dashboard Description isn't appeared");
                    return false;
                }
                break;
            default:
                break;
        }

        LogsUtils.info("error message: " + Message);
        return Message.equals(expectedMessage);
    }

    public boolean CheckCustomRange(String StartDate ,String EndDate){
        String Date, ExpectedDate = StartDate +" - "+EndDate ;
        Date = SelenuimUtil.getText(Driver,DatepickerValue);
        LogsUtils.info(Date);
        LogsUtils.info(ExpectedDate);
        return Date.equals(ExpectedDate);
    }



}
