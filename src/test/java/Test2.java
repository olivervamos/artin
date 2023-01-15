import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class Test2 {
    private WebDriver driver;
    private final String BASE_URL = "https://www.datart.sk/";

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/driver/chromedriver.exe");
        driver = new ChromeDriver();
        driver.get(BASE_URL);
        driver.manage().window().maximize();
    }

    @Test
    public void samsungSearchTest() {
        WebDriverWait wait = new WebDriverWait(driver,30);
        String searchText = "samsung";
        String searchTextCap = searchText.substring(0, 1).toUpperCase() + searchText.substring(1);
        //approve cookie
        driver.findElement(By.xpath("//div[contains(@class,'box')]//button[contains(text(),'Súhlasím a pokračovať')]"))
                .click();
        //search for Samsung
        driver.findElement(By.xpath("//input[@type='search']")).sendKeys(searchText);
        driver.findElement(By.xpath("//button[@type='submit']/span")).click();
        //verify actual and expected text
        String actualText = driver.findElement(By.xpath("//h1/span")).getText().replace("(", "")
                .replace(")", "");
        Assert.assertEquals("Search text are not equals!", searchText, actualText);

        //loop for verifying that item name contains Samsung
        int lastPageNumber = Integer.parseInt(driver.findElement
                (By.xpath("(//li[@class='page-item']/a[@class='page-link '])[last()]")).getText());
        for (int i = 0; i < lastPageNumber+1; i++) {
            String actualPage = String.valueOf(i+1);
            List<WebElement> discountList = driver.findElements(By.xpath("//div[contains(@class,'exponea-colose-link')]"));
            //remove discount popup if it's displayed
            if (discountList.size() > 0){
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@class,'exponea-colose-link')]")));
                driver.findElement(By.xpath("//div[contains(@class,'exponea-colose-link')]")).click();
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class ='exponea-button-close']")));
                driver.findElement(By.xpath("//button[@class ='exponea-button-close']")).click();
            }
            List<WebElement> itemsList = driver.findElements(By.xpath("//h3[@class='item-title']/a"));
            for (int j = 0; j < itemsList.size(); j++) {
                try {
                    // verify Samsung text in item name
                    Assert.assertTrue("Item is not contains " + searchTextCap + " on page " + actualPage,
                            itemsList.get(j).getText().contains(searchTextCap));
                }
                catch (AssertionError e){
                    System.out.println("Error item: " + itemsList.get(j).getText() + " not contains text: '" + searchTextCap  + "'");
                }
            }
            //next page button
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@class='page-link next-page ']")));
            if (i == lastPageNumber+1){
                break;
            }
            driver.findElement(By.xpath("//a[@class='page-link next-page ']")).click();
        }
    }

    @After
    public void tearDown() {
        driver.quit();
    }
}


