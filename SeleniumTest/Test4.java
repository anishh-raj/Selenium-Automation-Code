package SeleniumTest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Test4 {
    private static WebDriver driver;
    private static WebDriverWait wait;

    public static WebDriver setupDriver() {
        ChromeOptions opt = new ChromeOptions();
        opt.addArguments("--disable-notifications");
        opt.addArguments("--remote-allow-origins=*");

        System.setProperty("webdriver.chrome.driver", "//Users/apple/eclipse-workspace/Selenium/src/test/java/chromedriver");

        return new ChromeDriver(opt);
    }

    public static void main(String[] args) {
        // Call the method for different websites
        runTestForWebsite("https://thebridge.in/");
        runTestForWebsite("https://livelaw.in/");

    }

    public static void runTestForWebsite(String websiteUrl) {
        driver = setupDriver();
        wait = new WebDriverWait(driver, 100); // Adjust the wait timeout as needed

        driver.get("https://pagespeed.web.dev/");

        WebElement urlInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("i4")));
        urlInput.sendKeys(websiteUrl); // Pass the URL as a parameter

        WebElement analyzeButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Analyze']")));
        analyzeButton.click();

        wait.until(ExpectedConditions.urlContains("https://pagespeed.web.dev/analysis/"));

        // Mobile assessment status
        PrintAssessmentStatus("mobile", 1); // Use index 1 for "mobile" assessments
        
        // Mobile Details
        String[] PrintMetrics = {
            "Largest Contentful Paint (LCP)",
            "First Input Delay (FID)",
            "Cumulative Layout Shift (CLS)",
            "First Contentful Paint (FCP)",
            "Interaction to Next Paint (INP)",
            "Time to First Byte (TTFB)"
        };

        String[] CustomMetrics = {
            "First Contentful Paint",
            "Largest Contentful Paint",
            "Total Blocking Time",
            "Cumulative Layout Shift",
            "Speed Index"
        };

        PrintMetrics(PrintMetrics, 1);
        CustomMetrics(CustomMetrics, 1);

        // Clicking the desktop button
        WebElement desktopButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//span[@class='VfPpkd-YVzG2b' and @jsname='ksKsZd'])[2]")));
        desktopButton.click();

        PrintAssessmentStatus("desktop", 3); // Use index 3 for "desktop" assessments

        // Desktop Details
        PrintMetrics(PrintMetrics, 13);
        CustomMetrics(CustomMetrics, 2);

        driver.quit();
    }

    public static void PrintAssessmentStatus(String assessmentType, int indexForPassFail) {
        if ("desktop".equals(assessmentType) || "mobile".equals(assessmentType)) {
            WebElement assessmentStatusElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("(//div[contains(text(),'Core Web Vitals Assessment:')])[" + indexForPassFail + "]/span[1]")));
            String assessmentStatus = assessmentStatusElement.getText();
            System.out.println("Core Web Vitals Assessment(" + assessmentType + "): " + assessmentStatus);
        } else {
            System.out.println("Invalid assessment type: " + assessmentType);
        }
    }

    public static void PrintMetrics(String[] Printmetrics, int index) {
        for (int i = 0; i < Printmetrics.length; i++) {
            String PrintMetrics = "//a[contains(text(),'" + Printmetrics[i] + "')]/following::span[contains(@class, 'f49ZR')][" + index + "]/span";

            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(PrintMetrics)));
            String text = element.getText();
            System.out.println(Printmetrics[i] + ": " + text);
        }
    }

    public static void CustomMetrics(String[] customMetrics, int index) {
        for (int i = 0; i < customMetrics.length; i++) {
            String xpathExpression = "(//span[text()='" + customMetrics[i] + "']/following::div[1])[" + index + "]";

            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathExpression)));
            WebElement element = driver.findElement(By.xpath(xpathExpression));
            String text = element.getText();
            System.out.println(customMetrics[i] + ": " + text);
        }
    }
}
