package SeleniumTest;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;

public class Test1 {
    private static WebDriver driver;
    private static WebDriverWait wait;
    private static Workbook workbook;
    private static Sheet sheet;

    public static WebDriver setupDriver() {
        ChromeOptions opt = new ChromeOptions();
        opt.addArguments("--disable-notifications");
        opt.addArguments("--remote-allow-origins=*");

        System.setProperty("webdriver.chrome.driver", "//Users/apple/eclipse-workspace/Selenium/src/test/java/chromedriver");

        return new ChromeDriver(opt);
    }

    public static void main(String[] args) {
        driver = setupDriver();
        wait = new WebDriverWait(driver, 100); // Adjust the wait timeout as needed

        driver.get("https://pagespeed.web.dev/");

        WebElement urlInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("i4")));
        urlInput.sendKeys("https://thefederal.com/");

        WebElement analyzeButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Analyze']")));
        analyzeButton.click();

        wait.until(ExpectedConditions.urlContains("https://pagespeed.web.dev/analysis/"));

        // Create a new Excel workbook
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Web Metrics");
        // Mobile assessment status
        PrintAssessmentStatus("mobile", 0); // Specify rowIndex as 0 


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

        PrintMetrics(PrintMetrics ,"mobile", 1);
        CustomMetrics(CustomMetrics, "mobile",7);

        // Clicking the desktop button
        WebElement desktopButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//span[@class='VfPpkd-YVzG2b' and @jsname='ksKsZd'])[2]")));
        desktopButton.click();

     // Desktop assessment status
        PrintAssessmentStatus("desktop", 13); 


     // Desktop Details
        PrintMetrics(PrintMetrics, "desktop", 14); 
     // Desktop Details
        CustomMetrics(CustomMetrics, "desktop",20 ); 

        // Save the Excel file
        String xlsxFilePath = "/Users/apple/Desktop/Report/web_metrics123.xlsx";

        try (OutputStream outputStream = new FileOutputStream(xlsxFilePath)) {
            workbook.write(outputStream);
            System.out.println("Excel file has been created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Quit the WebDriver
        driver.quit();
    }

    public static void createRow(int rowNum, String[] data) {
        Row row = sheet.createRow(rowNum);
        for (int i = 0; i < data.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(data[i]);
        }
    }

    	public static void PrintAssessmentStatus(String assessmentType, int rowIndex) {
    	    String dynamicXPath = "";

    	    if ("desktop".equals(assessmentType) || "mobile".equals(assessmentType)) {
    	        dynamicXPath = "(//div[@aria-labelledby='" + assessmentType + "_tab']//div[contains(text(),'Core Web Vitals Assessment:')])/span[1]";
    	    } else {
    	        System.out.println("Invalid assessment type: " + assessmentType);
    	        return; // Exit the method for invalid input
    	    }

    	    WebElement assessmentStatusElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(dynamicXPath)));
    	    String assessmentStatus = assessmentStatusElement.getText();
    	    System.out.println("Core Web Vitals Assessment(" + assessmentType + "): " + assessmentStatus);

    	    // Write to Excel in the specified row index
    	    createRow(rowIndex, new String[]{"Core Web Vitals Assessment(" + assessmentType + "):", assessmentStatus});
    	}

    
    //(//div[@aria-labelledby='" + assessmentType + "_tab']//div[contains(text(),'Core Web Vitals Assessment:')])[1]/span[1]
    
  
    	public static void PrintMetrics(String[] Printmetrics, String assessmentType,int rowIndex) {
    	    for (int i = 0; i < Printmetrics.length; i++) {
    	        String PrintMetrics = "(//div[@aria-labelledby='" + assessmentType + "_tab']//descendant::a[text()='" + Printmetrics[i] + "']/following::span[contains(@class, 'f49ZR')])[1]/span";

    	        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(PrintMetrics)));
    	        String text = element.getText();
    	        System.out.println(Printmetrics[i] + ": " + text);

    	        // Write to Excel in the specified row index
    	        createRow(rowIndex + i, new String[]{Printmetrics[i] + ":", text});
    	    }
    	}

    
    //(//div[@aria-labelledby='"+asse']//descendant::a[text()='"+ Printmetrics[i]+"']/following::span[contains(@class, 'f49ZR')])[1]/span

    public static void CustomMetrics(String[] customMetrics, String assessmentType,int rowIndex) {
        for (int i = 0; i < customMetrics.length; i++) {
            String xpathExpression = "(//div[@aria-labelledby='" + assessmentType + "_tab']//span[text()='"+ customMetrics[i]+"'])//following::div[1]";

            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathExpression)));
            WebElement element = driver.findElement(By.xpath(xpathExpression));
            String text = element.getText();
            System.out.println(customMetrics[i] + ": " + text);

            // Write to Excel in the specified row index
            createRow(rowIndex + i, new String[]{customMetrics[i] + ":", text});
        }
    }
    
    //(//div[@aria-labelledby='" + assessmentType + "_tab']//span[text()='" customMetrics[i]+"'])//following::div[1]
};

  

   
