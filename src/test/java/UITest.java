import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.List;

public class UITest {
    static WebDriver driver;

    @BeforeAll
    public static void setUp(){
        driver = WebDriverManager.chromedriver().create();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://rahulshettyacademy.com/AutomationPractice/");
    }

    @Test
    public void testRadioButtons() {

        driver.findElement(By.xpath("//*[@id=\"radio-btn-example\"]/fieldset/label[3]/input")).click();
        List<WebElement> radioButtons = driver.findElements(By.xpath("//*[@id=\"radio-btn-example\"]/fieldset/label/input"));

        int numOfSelectedRadioButtons = 0;

        for(WebElement elem: radioButtons){
            if (elem.isSelected()){
                numOfSelectedRadioButtons++;
            }
        }

        System.out.println("Assert Number of Selected Radio Buttons: \nExpected: 1 \nActual: "+numOfSelectedRadioButtons);
        Assertions.assertEquals(numOfSelectedRadioButtons, 1, "Expected number of selected radio buttons to be ");

        driver.findElement(By.xpath("//*[@id=\"radio-btn-example\"]/fieldset/label[2]/input")).click();
        List<WebElement> newRadioButtons = driver.findElements(By.xpath("//*[@id=\"radio-btn-example\"]/fieldset/label/input"));

        boolean isCorrect = newRadioButtons.get(1).isSelected() && !newRadioButtons.get(0).isSelected() && !newRadioButtons.get(2).isSelected();

        System.out.println("Validate Only Radio 2 is Selected");
        Assertions.assertTrue(isCorrect, "Expected only Radio 2 to be selected");
    }

    @Test
    public void testSuggestion(){
        driver.findElement(By.id("autocomplete")).click();
        driver.findElement(By.id("autocomplete")).sendKeys("South"+Keys.ARROW_DOWN);
        driver.findElements(By.xpath("//li[@class='ui-menu-item']")).get(1).click();

        driver.findElement(By.id("autocomplete")).clear();
        driver.findElement(By.id("autocomplete")).sendKeys("Republic"+Keys.ARROW_DOWN);
        driver.findElements(By.xpath("//li[@class='ui-menu-item']")).get(0).click();
    }

    @Test
    public void testCheckboxes(){
        List<WebElement> checkboxes = driver.findElements(By.xpath("//div[@id='checkbox-example']//input"));

        for(WebElement checkbox: checkboxes){
            checkbox.click();
            Assertions.assertTrue(checkbox.isSelected());
        }

        checkboxes.get(0).click();
        List<WebElement> newCheckboxes = driver.findElements(By.xpath("//div[@id='checkbox-example']//input"));
        boolean firstOneUnselected = !newCheckboxes.get(0).isSelected() && newCheckboxes.get(1).isSelected()
                && newCheckboxes.get(2).isSelected();
        Assertions.assertTrue(firstOneUnselected, "Expected only the first checkbox to be unchecked");
    }

    @Test
    public void testShowHide(){
        driver.findElement(By.id("hide-textbox")).click();
        boolean isHidden = !driver.findElement(By.id("displayed-text")).isDisplayed();
        Assertions.assertTrue(isHidden, "Expected element to be hidden");

        driver.findElement(By.id("show-textbox")).click();
        boolean isDisplayed = driver.findElement(By.id("displayed-text")).isDisplayed();
        Assertions.assertTrue(isDisplayed, "Expected element to be displayed");
    }

    @Test
    public void testWebTableFixedHeader(){
        String amount = driver.findElement(By.xpath("//tr/td[text()='Joe']/following-sibling::td[text()='Postman']/following-sibling::td[text()='Chennai']/following-sibling::*")).getText();
        Assertions.assertEquals(amount, "46", "Mismatch between expected and actual");

        String totalLabel = driver.findElement(By.xpath("//div[contains(text(), 'Total Amount Collected')]")).getText();
        Assertions.assertTrue(totalLabel.contains("296"), "Expected 296 to be contained in the label");

        List<WebElement> amounts = driver.findElements(By.xpath("//*[@id=\"product\"]/tbody/tr/td[4]"));
        int sum = 0;

        for(WebElement eachAmount: amounts){
            sum += Integer.parseInt(eachAmount.getText());
        }

        int totalFromLabel = Integer.parseInt(totalLabel.split(":")[1].trim());
        Assertions.assertEquals(sum, totalFromLabel, "Expected sum ='"+sum+"', but found '"+totalFromLabel+"'.");

    }

    @Test
    public void testIFrames(){
        // Validate that there are more than iframes
        int size = driver.findElements(By.tagName("iframe")).size();
        Assertions.assertTrue(size>0, "Expected the page to have more than 1 frame");

        WebElement coursesFrame = driver.findElement(By.id("courses-iframe"));
        driver.switchTo().frame(coursesFrame).findElement(By.linkText("JOIN NOW")).click();
    }

    @AfterAll
    public static void tearDown() throws InterruptedException {
        Thread.sleep(6000);
        driver.close();
        driver.quit();
    }
}
