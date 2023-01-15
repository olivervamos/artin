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

import java.util.ArrayList;
import java.util.List;


public class Test1 {
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
    public void addItemsToBasketAndRemoveOneTest() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver,30);
        //approve cookie
        driver.findElement(By.xpath("//div[contains(@class,'box')]//button[contains(text(),'Súhlasím a pokračovať')]")).click();
        // navigate to pc category
        driver.findElement(By.xpath("//h2[contains(@class, 'footer')]/..//a[contains(@href, 'pc-notebooky')]")).click();
        // sort page
        driver.findElement(By.xpath("//a[@data-lb-name='Najdrahší']")).click();
        Thread.sleep(1000);

        List<WebElement> buyButton = driver.findElements(By.xpath("//button[@data-lb-action='buy']/span"));
        List<WebElement> itemList = driver.findElements(By.xpath("//h3[@class='item-title']/a[contains(@data-lb-name,'Notebook')]"));
        List<String> itemNameList = new ArrayList<>();
        wait.until(ExpectedConditions.visibilityOfAllElements(buyButton));
        for (int i = 0; i < buyButton.size(); i++) {
            if (i == 3) {
                break;}
            //add item into basket
            buyButton.get(i).click();
            //get itemName and add it to list
            itemNameList.add(itemList.get(i).getText().replace("Notebook ",""));
            //close popup
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@aria-label='Close']")));
            driver.findElement(By.xpath("//button[@aria-label='Close']")).click();
        }
        //navigate to basket
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//img[@class='svg-cart-full']/../span")));
        driver.findElement(By.xpath("//img[@class='svg-cart-full']/../span")).click();

        List<WebElement> basketItemList = driver.findElements(By.xpath("//h2[@class='overflow-ellipsis']"));
        for (int i = 0; i < basketItemList.size(); i++) {
            //verify if actual and expected item name are equals
            Assert.assertEquals("Item name is not equals",itemNameList.get(i), basketItemList.get(i).getText());
        }
        //remove item from basket
        driver.findElement(By.xpath("//a[@rel='nofollow']")).click();
        Thread.sleep(1000);
        //verify if item was successfully removed
        List<WebElement> basketItemListAfterRemove = driver.findElements(By.xpath("//h2[@class='overflow-ellipsis']"));
        Assert.assertEquals("Count of items are not equals",2,basketItemListAfterRemove.size());
    }

    @After
    public void tearDown() { driver.quit(); }
}


