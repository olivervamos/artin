import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class Test3 {
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
    public void emptyBasketTest() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver,30);
        //approve cookie
        driver.findElement(By.xpath("//div[contains(@class,'box')]//button[contains(text(),'Súhlasím a pokračovať')]")).click();
        //navigate to pc category
        driver.findElement(By.xpath("//h2[contains(@class, 'footer')]/..//a[contains(@href, 'pc-notebooky')]")).click();
        Thread.sleep(1000);
        //add item to basket
        driver.findElement(By.xpath("//button[@data-lb-action='buy']/span")).click();
        // close popup
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@aria-label='Close']")));
        driver.findElement(By.xpath("//button[@aria-label='Close']")).click();
        // navigate to basket
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//img[@class='svg-cart-full']")));
        driver.findElement(By.xpath("//img[@class='svg-cart-full']")).click();
        //remove item from basket
        driver.findElement(By.xpath("//img[contains(@src,'remove')]")).click();
        //click on continue button
        driver.findElement(By.xpath("//a[contains(@class,'continue')]")).click();
        // verify if basket is empty
        String text = driver.findElement(By.xpath("//div[@class='modal-body']")).getText();
        Assert.assertEquals("Neplatná hodnota položky v košíku, košík byl vymazán",text);
    }

    @After
    public void tearDown() { driver.quit();
  }
}


