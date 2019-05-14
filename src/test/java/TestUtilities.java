import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.LocatorsRepo;

public class TestUtilities {


    public static LocatorsRepo locators;

    public static void waitForElement(WebDriver webDriver, String ElementName, int timout) throws Exception
    {
        locators = new LocatorsRepo("./src/test/resources/"+BaseTest.locale+".properties");

        try {
            (new WebDriverWait(webDriver, timout))
                    .until(ExpectedConditions.presenceOfElementLocated(locators.getbjectLocator(ElementName)));
        }
        catch (ElementNotVisibleException e)
        {
            System.out.println("Element = "+ locators.getbjectLocator(ElementName) + " not found on page \n" + e.getMessage());
        }

    }


    public static void closeSubTabs(WebDriver webDriver) throws Exception
    {
        String originalWin = webDriver.getWindowHandle();
        for(String handle : webDriver.getWindowHandles()) {
            if (!handle.equals(originalWin)) {
                webDriver.switchTo().window(handle);
                webDriver.close();
            }
        }
        webDriver.switchTo().window(originalWin);
    }

    public static boolean verifyElementPresence(WebDriver webdriver,String locator) {
        WebElement element;
        try {
            element = webdriver.findElement(locators.getbjectLocator(locator));

        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
        return true;
    }

    public static void clickOnHiddenElement(String ele,WebDriver webDriver){
        JavascriptExecutor js = (JavascriptExecutor)webDriver;
        WebElement element = webDriver.findElement(locators.getbjectLocator(ele));
        js.executeScript("arguments[0].click()",element);
    }

    // scroll to the view
    public static void scrollTo(WebDriver webDriver, String element1) {
        WebElement elem = webDriver.findElement(locators.getbjectLocator(element1));
        ((JavascriptExecutor) webDriver).executeScript(
                "arguments[0].scrollIntoView();", elem);
    }



}
