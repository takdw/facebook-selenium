/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facebookcrawler;

import java.io.File;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import facebookcrawler.Resource;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.Locatable;

/**
 *
 * @author Tamrat Assefa
 */
public class FacebookCrawler {

    private static final String CHROMEDRIVERPATH = "C:\\chromedriver.exe";
    private static final String ULTRASURFEXTPATH = "C:\\ultrasurf.crx";
    private static Resource rs = new Resource();
    private static WebDriver wd = startVPNChrome();
    
    public static WebDriver startVPNChrome() {
        System.setProperty("webdriver.chrome.driver", CHROMEDRIVERPATH);
        
        ChromeOptions options = new ChromeOptions();
        options.addExtensions(new File(ULTRASURFEXTPATH));
        options.addArguments("--disable-notifications");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        
        return new ChromeDriver(capabilities);
    }
    
    public static WebDriver startChrome() {
        System.setProperty("webdriver.chrome.driver", CHROMEDRIVERPATH);
        
        return new ChromeDriver();
    }
    
    public static void login() {
        wd.get("https://facebook.com");
        
        WebElement emailField = wd.findElement(By.name("email"));
        WebElement passField = wd.findElement(By.name("pass"));
        WebElement loginBtn = wd.findElement(By.id("u_0_q"));
        
        emailField.sendKeys(rs.username);
        passField.sendKeys(rs.password);
        loginBtn.click();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        doLogin();
        
//        WebElement profileBtn = wd.findElement(By.xpath("//*[@id=\"u_0_4\"]/div[1]/div[1]/div/a"));
//        wd.navigate().to(profileBtn.getAttribute("href"));
//        
//        WebElement friendsBtn = wd.findElement(By.className("_gs6"));
//        System.out.println(friendsBtn.getText());
//        
//        WebElement friend = wd.findElement(By.xpath("//*[@id=\"u_0_q\"]/div/a[3]"));
//        String friendsPageLink = friend.getAttribute("href");
//        wd.navigate().to(friendsPageLink);
        
    }
    
    public static void doLogin() {

        // connecting to facebook
        wd.get("https://facebook.com");
        
        // entering login information
        wd.findElement(By.name("email")).sendKeys(rs.username);
        wd.findElement(By.name("pass")).sendKeys(rs.password);
        
        // press the login button
        wd.findElement(By.id("u_0_q")).click();
        
        // click the profile name
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
            System.out.println("Error");
        }
        wd.findElement(By.xpath("//*[@title='Profile']")).click();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(FacebookCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // find the number of friends
        int fCount = Integer.parseInt(wd.findElement(By.xpath("//*[@data-tab-key='friends']")).getText().substring(7));

        // click on frineds tab
        wd.findElement(By.xpath("//*[@data-tab-key='friends']")).click();

        // find current loaded frineds count and get it in a list
       int found = 0;
        int counter = 1;
        List<WebElement> friends;
        do  {    
            
            try {
                Thread.sleep(2000);
                System.out.println("Sleeping...");
            } catch (Exception e) {
                System.out.println("Error!");
            }
            
            friends = wd.findElements(By.xpath("//*[@class='fsl fwb fcb']"));
            found = friends.size();
            
            System.out.println("pass " + counter + ": " + found);
            counter++;
            if (wd.findElement(By.className("uiHeaderTitle")).isDisplayed()) {
                break;
            }
            
            JavascriptExecutor myJSExe = (JavascriptExecutor)wd;
            myJSExe.executeScript("window.scrollBy(0,1000)", "");
        } while (found < fCount);
        
        System.out.println(found);
        System.out.println(fCount);
        
        System.out.println("***Friends List***");
        
        for (int i=0; i<found; i++){
            System.out.println(friends.get(i).getText());
        }
        
    }

}


