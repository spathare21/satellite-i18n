import org.testng.annotations.Test;

public class LoginTest extends BaseTest{

    @Test
    public void login() throws Exception {
        Thread.sleep(10);
        driver.findElement(locators.getbjectLocator("username")).sendKeys("admin");
        driver.findElement(locators.getbjectLocator("password")).sendKeys("changeme");
        driver.findElement(locators.getbjectLocator("login")).click();
    }

}
