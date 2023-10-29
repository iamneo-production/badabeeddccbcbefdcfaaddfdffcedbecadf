package testcases;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.Duration;
import com.aventstack.extentreports.Status;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import utils.EventHandler;
import utils.LoggerHandler;
import java.util.logging.Logger;
import utils.Screenshot;
import utils.Reporter;

public class Testcase1 extends Base {

    EventHandler e;
    ExtentSparkReporter sparkReporter;
    ExtentReports reporter = Reporter.generateExtentReport();
    java.util.logging.Logger log =  LoggerHandler.getLogger();
    Screenshot screenshotHandler = new Screenshot();
    // ChromeOptions options = new ChromeOptions();
    WebDriverWait wait;

    @DataProvider(name = "testData")
    public Object[][] readTestData() throws IOException {
        String excelFilePath = System.getProperty("user.dir") + "/src/test/java/resources/Testdata.xlsx";
        String sheetName = "Sheet1";

        try (FileInputStream fileInputStream = new FileInputStream(excelFilePath);
             Workbook workbook = WorkbookFactory.create(fileInputStream)) {

            Sheet sheet = workbook.getSheet(sheetName);
            int rowCount = sheet.getLastRowNum();

            Object[][] searchDataArray = new Object[rowCount][2];

            for (int i = 1; i <= rowCount; i++) {
                Row row = sheet.getRow(i);

                searchDataArray[i - 1][0] = getStringCellValue(row.getCell(0));
                searchDataArray[i - 1][1] = getStringCellValue(row.getCell(1));
            }

            return searchDataArray;
        }
    }


    private String getStringCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    double numericValue = cell.getNumericCellValue();
                    if (numericValue == (long) numericValue) {
                        return String.format("%d", (long) numericValue);
                    } else {
                        return String.valueOf(numericValue);
                    }
                }
            default:
                return "";
        }
    }


    @Test(priority = 1)
    public void Tc001() throws InterruptedException, IOException {
        try {
            ExtentTest test = reporter.createTest("Abhibus booking page");
            e = new EventHandler();
            driver.get(prop.getProperty("url") + "/");
            //options.addArguments("--remote-allow-origins=*");
            log.info("Browser launched");
            driver.manage().window().maximize();
            driver.findElement(By.linkText("Trains")).click();
            log.info("Redirected to Train booking page");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		    Assert.assertTrue(driver.getCurrentUrl().contains("trains"));
            log.info("Asserted the URL contains keyword of Train");
            driver.findElement(By.xpath("//div/a/img[@alt='Abhibus']"));
            WebElement nav = driver.findElement(By.xpath("//div/a/img[@alt='Abhibus']"));
            nav.click();
            log.info("Navigated back to homepage");
            WebDriverWait wait01 = new WebDriverWait(driver, Duration.ofSeconds(10));
            driver.findElement(By.linkText("Login / Register")).click();
            WebElement linkElement = driver.findElement(By.linkText("Login/Signup with OTP"));
            Assert.assertTrue(linkElement.isDisplayed()); // or linkElement.isEnabled()
            log.info("Page asserted for having the keyword Login/Signup with OTP ");
            test.pass("Test passed successfully");
        }   

        catch (Exception ex) {
        ExtentTest test = reporter.createTest("Bus Booking");
        test.log(Status.FAIL, "Unable to load the url",
        MediaEntityBuilder.createScreenCaptureFromPath(screenshotHandler.captureScreenshot(driver, "Ticket Booking")).build());
        }
    }

    @Test(priority = 2, dataProvider = "testData")
    public void Tc002(String Departure, String Destination) throws InterruptedException, IOException {

            ExtentTest test = reporter.createTest("Abhibus booking page");
            e = new EventHandler();
            // driver.get(prop.getProperty("url") + "/");
            // options.addArguments("--remote-allow-origins=*");
            log.info("Browser launched");
            driver.manage().window().maximize();
            WebDriverWait wait0 = new WebDriverWait(driver, Duration.ofSeconds(30));
            WebElement from= driver.findElement(By.xpath("//div/input[@id='source']"));
            from.click();
            from.sendKeys(Departure);
            WebElement to= driver.findElement(By.xpath("//div/input[@id='destination']"));
            to.click();
            to.sendKeys(Destination);
            driver.findElement(By.xpath("//div/input[@id='datepicker1']")).click();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            driver.findElement(By.xpath("//div[2]/table/tbody/tr[5]/td[1]")).click();
            driver.findElement(By.xpath("//div/a[contains(text(),'Search')]")).click();
        
           

            test.pass("Test passed successfully");
            log.info("Page asserted with keyword of depature and destination");
            
    
    }

    // @Test(priority = 3)
    // public void Tc003() throws InterruptedException, IOException {
    //     try {
    //         ExtentTest test = reporter.createTest("Abhibus booking page");
    //         e = new EventHandler();
    //         driver.get(prop.getProperty("url") + "/");
    //         //options.addArguments("--remote-allow-origins=*");
    //         log.info("Browser launched");
    //         driver.manage().window().maximize();
    //         WebDriverWait wait0 = new WebDriverWait(driver, Duration.ofSeconds(30));
    //         WebElement from= driver.findElement(By.xpath("//div/input[@id='source']"));
    //         from.click();
    //         from.sendKeys("Coimbatore");
    //         WebElement to= driver.findElement(By.xpath("//div/input[@id='destination']"));
    //         to.click();
    //         to.sendKeys("Bengaluru");
    //         driver.findElement(By.xpath("//div/input[@id='datepicker1']")).click();
    //         WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    //         driver.findElement(By.xpath("//div[2]/table/tbody/tr[5]/td[1]")).click();
    //         driver.findElement(By.xpath("//div/a[contains(text(),'Search')]")).click();
    //         log.info("Search click");
    //         WebDriverWait wait03 = new WebDriverWait(driver, Duration.ofSeconds(10));
    //        String currenturl1 = driver.getCurrentUrl();
    //        Assert.assertTrue(currenturl1.contains("Bangalore") && currenturl1.contains("Coimbatore"), "Page URL doesn't contain both 'Bangalore' and 'Coimbatore' keywords");  
    //         log.info("Final page url Asserted");
    //         test.pass("Test passed successfully");
    //     }   

    //      catch (Exception ex) {
    //     ExtentTest test = reporter.createTest("Bus Booking");
    //     test.log(Status.FAIL, "Unable to Book Bus Ticket",
    //      MediaEntityBuilder.createScreenCaptureFromPath(screenshotHandler.captureScreenshot(driver, "Ticket Booking")).build());
    //     }
    // }

    
   
@BeforeMethod
public void beforeMethod() throws MalformedURLException {
    openBrowser();

}

    @AfterMethod
    public void afterMethod() {
        
        // driver.quit();
        reporter.flush();
        log.info("Browser closed");
        LoggerHandler.closeHandler();
    }
}