import io.qameta.allure.Attachment;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.LocatorsRepo;
import utils.MyScreenRecorder;
import utils.ReadProperty;
import utils.Utility;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class BaseTest implements IHookable {
    final static Logger logger = Logger.getLogger(BaseTest.class);

    public WebDriver driver;
    public static String locale;
    public String hostname;
    public static LocatorsRepo locators;

    @Parameters("browser")
    @BeforeClass
    public void beforeClass(String browser) throws Exception {
        locale = Utility.readPropertyOrEnv("locale", "en_US");
        hostname = Utility.readPropertyOrEnv("hostname","hostname");
        if(locale.equalsIgnoreCase("en_US") || locale.equalsIgnoreCase("fr_FR") || locale.equalsIgnoreCase("de_DE") ||
                locale.equalsIgnoreCase("it_IT") || locale.equalsIgnoreCase("pt_BR"))
            locators = new LocatorsRepo("./src/test/resources/"+locale+".properties");
        else {
            try {
                Runtime.getRuntime().exec("native2ascii -encoding utf8 " + "./src/test/resources/" + locale + ".properties" + " ./src/test/resources/" + locale + "_ascii.properties");
            } catch (Exception e) {
                System.out.println("HEY Buddy ! U r Doing Something Wrong ");
                e.printStackTrace();
            }
            Thread.sleep(5000);
            locators = new LocatorsRepo("./src/test/resources/" + locale + "_ascii.properties");
        }
        if(browser.equalsIgnoreCase("firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            options.setAcceptInsecureCerts(true);
            FirefoxProfile profile = new FirefoxProfile();
            profile.setPreference("intl.accept_languages",locale);
            profile.setAcceptUntrustedCertificates(true);
            options.setCapability("firefox_profile",profile);
            System.setProperty("webdriver.gecko.driver", "../satellite/library/geckodriver");
            driver = new FirefoxDriver(options);
            // If browser is Chrome, then do this
        }else if (browser.equalsIgnoreCase("chrome")) {
            ChromeOptions options = new ChromeOptions();
            Map<String, Object> prefs = new HashMap<>();
            prefs.put("intl.accept_languages", locale);
            options.setExperimentalOption("prefs", prefs);
            System.setProperty("webdriver.chrome.driver", "../satellite/library/chromedriver");
            driver = new ChromeDriver(options);
        }
        driver.get("https://"+ReadProperty.getConfig(hostname));
        driver.manage().window().maximize();
    }

    @BeforeMethod
    public void beforeMethod(ITestResult result) throws Exception {
        MyScreenRecorder.startRecording(result.getMethod().getMethodName());
        saveScreenshot(result.getMethod().getMethodName(),driver);
    }

    @AfterMethod
    public void afterMethod(ITestResult result) throws Exception {
        MyScreenRecorder.stopRecording();
        logger.info("AfterMethod \n");
    }

    @AfterClass
    public void tearDown(){
        driver.close();
//        driver.quit();
    }


    @Override
    public void run(IHookCallBack callBack, ITestResult testResult){

        callBack.runTestMethod(testResult);
        try {
            saveScreenshot(testResult.getMethod().getMethodName(),driver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Attachment(value = "{0}", type = "image/png")
    private static byte[] saveScreenshot(String testMethodName,WebDriver dr) throws Exception
    {
        try {
            String currentDir = System.getProperty("user.dir");
            String Screenshotpath = currentDir + "/screens/snapshot/";
            File scrFile = ((TakesScreenshot) dr).getScreenshotAs(OutputType.FILE);
            String imgFile= Screenshotpath + testMethodName + Instant.now().toEpochMilli() + ".jpg";
            File imgf = new File(imgFile);
            FileUtils.copyFile(scrFile, new File(imgFile));
            return toByteArray(imgf);
        }
        catch(Exception e)
        {
            logger.error("Exception while taking screenshot : " + e.getMessage());
        }
        return new byte[0];
    }

    private static byte[] toByteArray(File file) throws IOException {
        return Files.readAllBytes(Paths.get(file.getPath()));
    }


}
