import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import utils.LocatorsRepo;
import utils.Utility;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BaseTest {

    private WebDriver driver;
    public static String locale;
    private static LocatorsRepo locators;

    @Parameters("browser")
    @BeforeClass
    public void beforeClass(String browser) throws IOException {
        // If the browser is Firefox, then do this
        locale = Utility.readPropertyOrEnv("locale", "en_US");
        if(locale.equalsIgnoreCase("en_US") || locale.equalsIgnoreCase("fr_FR") || locale.equalsIgnoreCase("de_DE"))
            locators = new LocatorsRepo("./src/test/resources/"+locale+".properties");
        else
            locators = new LocatorsRepo("./src/test/resources/"+locale+"_ascii.properties");
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
        driver.get("https://qeblade36.rhq.lab.eng.bos.redhat.com");
        driver.manage().window().maximize();
    }

    @Test
    public void loginTest() throws InterruptedException {

        Thread.sleep(10);
        driver.findElement(locators.getbjectLocator("username")).sendKeys("admin");
        driver.findElement(locators.getbjectLocator("password")).sendKeys("changeme");
        driver.findElement(locators.getbjectLocator("login")).click();

    }

    @AfterClass
    public void tearDown(){
        driver.close();
//        driver.quit();
    }


}
