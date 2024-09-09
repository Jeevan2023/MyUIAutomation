package com.lazerycode.selenium;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class MyTest {
    WebDriver  driver;
    @BeforeClass
    public void beforeClass()
    {
        driver = openBrowser("chrome");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }
    @Test (description = "Screenshot test case",dataProvider = "screen-resolution")
    public void test1(int len,int width) {
        driver.manage().window().setSize(new Dimension(len, width));
        driver.navigate().to("https://www.getcalley.com/page-sitemap.xml");
        sleep();
        List<WebElement> allSites = driver.findElements(By.xpath("//table[@id='sitemap']/tbody/tr"));
        if (allSites.size() >= 5) {
            for (int i = 0; i<5;i++)
            {
                allSites.get(i).click();
                File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                try {
                   /* File filePath = new File("D:/");
                    System.out.println(filePath.getAbsoluteFile());
                    File outputText = new File(filePath.getAbsoluteFile(), getScreenshotFileName(len,width));
                    System.out.println(outputText.getAbsoluteFile());*/
                    FileUtils.copyFile(src, new File("C:\\Users\\Victus\\Downloads\\Selenium-Maven-Template-master\\screenshots\\"+getScreenshotFileName(len,width)));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                driver.navigate().back();
                sleep();
                allSites = driver.findElements(By.xpath("//table[@id='sitemap']/tbody/tr"));
                Assert.assertTrue("The Site is not present to take screenshot",allSites.size()>=0);
            }
        }
    }

    @Test (description = "This will be a test to check the functional flow of the application using Selenium Web Drivers.")
    public void uploadTest()
    {
        driver.get(" https://demo.dealsdray.com/");
        driver.manage().window().maximize();
        driver.findElement(By.xpath("//input[@id='mui-1']")).sendKeys("prexo.mis@dealsdray.com");
        driver.findElement(By.xpath("//input[@id='mui-2']")).sendKeys("prexo.mis@dealsdray.com");
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        driver.findElement(By.xpath("//span[text()='Order']")).click();
        driver.findElement(By.xpath("//span[text()='Orders']")).click();
        driver.findElement(By.xpath("//button[text()='Add Bulk Orders']")).click();
        driver.findElement(By.xpath("//input[starts-with(@id, 'mui')]")).sendKeys("C:\\Users\\Victus\\Downloads\\demo-data.xlsx");
        sleep();
        driver.findElement(By.xpath("//button[text()='Import']")).click();

        String validate = driver.findElement(By.xpath("//button[text()='Validate Data']")).getText();

        Assert.assertEquals(validate,"Validate Data");

    }

    @DataProvider(name = "screen-resolution")
    public Object[][] dataProvFunc(){
        return new Object[][]{
//                {360,640},{414,896},{375,667},
                {1920,1080},{1366,768},{1536,864}
        };
    }
    @AfterClass
    public void afterClass()
    {
        driver.quit();
    }
    public WebDriver openBrowser(String browser) {
        WebDriver driver = null;
        if(browser.equalsIgnoreCase("firefox")){
            System.setProperty("webdriver.gecko.driver", "C:\\Users\\Victus\\Downloads\\Selenium-Maven-Template-master\\drivers\\geckodriver.exe");
            driver = new FirefoxDriver();
        }
        else if(browser.equalsIgnoreCase("chrome")) {
            System.setProperty("webdriver.chrome.driver", "C:\\Users\\Victus\\Downloads\\Selenium-Maven-Template-master\\drivers\\chromedriver.exe");
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");
            driver = new ChromeDriver(options);
        }

        else if(browser.equalsIgnoreCase("safari")){
            System.setProperty("Add the path to the firefox/gecko driver","C:\\Users\\Victus\\Downloads\\geckodriver-v0.35.0-win-aarch64");
            driver = new SafariDriver();
        }
        else{
            System.out.println("Browser not selected, Please select the browser");
        }
        return driver;
    }
    public void sleep()
    {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String getScreenshotFileName(int len,int wid)
    {
        String hostname = "Unknown";
        String finalScreenshotFileName = "";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime now = LocalDateTime.now();
        String currentTime = dtf.format(now);
        try
        {
            InetAddress addr;
            addr = InetAddress.getLocalHost();
            hostname = addr.getHostName();
            finalScreenshotFileName = hostname + Integer.toString(len)+"and"+Integer.toString(wid)+currentTime+ ".png";

        }
        catch (Exception ex)
        {
            System.out.println("Hostname can not be resolved");
        }
        System.out.println("The final screeshots name: "+finalScreenshotFileName);
        return finalScreenshotFileName;
    }
}
