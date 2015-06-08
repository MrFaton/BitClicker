package com.mr_faton.browser.impl;

import com.mr_faton.browser.Browser;
import com.mr_faton.message.MessageAPI;
import com.mr_faton.message.impl.EmailMessenger;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by root on 05.06.2015.
 */
public class FireFoxBrowser implements Browser {
    private String image = "temp.jpg";
    private String url;
    private WebDriver driver;
    WebElement query;

    @Override
    public void init() {
        driver = new FirefoxDriver();
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void checkURL() {
        driver.navigate().to(url);
        try {
            query = driver.findElement(By.id("tagline"));
        } catch (Exception ex) {
            System.err.println("Error: Bad URL");
            try {
                Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void login(String login, String password) {
        query = driver.findElement(By.id("login_form_btc_address"));
        if (!query.isDisplayed()) {
            query = driver.findElement(By.id("switch_to_login_button"));
            query.click();
        }
        query = driver.findElement(By.id("login_form_btc_address"));
        query.sendKeys(login);
        query = driver.findElement(By.id("login_form_password"));
        query.sendKeys(password);
        query = driver.findElement(By.id("login_button"));
        query.click();

    }

    @Override
    public long getSleepTime() {
        String title = driver.getTitle();
        if (!title.contains("m:")) {
            return 0;
        }
        System.out.println(title);
        String[] titleSplit = title.split("m:");
        String sleepMinutes = titleSplit[0];
        System.out.println(sleepMinutes);
        if (sleepMinutes == null) {
            return 0;
        }
        long sleep = 0;
        try {
            sleep = Long.valueOf(sleepMinutes);
        } catch (NumberFormatException ex) {
            System.err.println("Error while parsing title number minutes");
        }
       return sleep * 60 * 1000 + 60 * 1000;
    }

    @Override
    public void selectCaptcha() {
        query = driver.findElement(By.id("free_play_captcha_types"));
        query.sendKeys(Keys.ARROW_DOWN);
        query.sendKeys(Keys.ENTER);
    }

    @Override
    public void saveCaptcha() {
        byte[] arrScreen = ((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES);
        BufferedImage imageScreen = null;
        try {
            imageScreen = ImageIO.read(new ByteArrayInputStream(arrScreen));
        } catch (IOException e) {
            e.printStackTrace();
        }
        query = driver.findElement(By.id("adcopy-puzzle-image-image"));
        Dimension capDimension = query.getSize();
        Point capLocation = query.getLocation();
        BufferedImage imgCap = imageScreen.getSubimage(capLocation.x, capLocation.y, capDimension.width, capDimension.height);


        try(FileOutputStream fos = new FileOutputStream(image)) {
            ImageIO.write(imgCap, "jpg", fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void typeCaptcha(String solvedCaptcha) {
        query = driver.findElement(By.id("adcopy_response"));
        query.sendKeys(solvedCaptcha);
    }

    @Override
    public void pushEarnButton() {
        query = driver.findElement(By.id("free_play_form_button"));
        query.click();
    }

    @Override
    public boolean confirmEarning() {
        try {
            query = driver.findElement(By.id("winnings"));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public void logout() {
        query = driver.findElement(By.className("logout_link"));
        query.click();
    }

    @Override
    public void refresh() {
        driver.navigate().refresh();
    }

    //    public static void main(String[] args) throws Exception {
//        MessageAPI messageAPI = new EmailMessenger();
//
//
//        driver.get("https://freebitco.in");
//        WebElement query = driver.findElement(By.id("switch_to_login_button"));
//        query.click();
//
//        query = driver.findElement(By.id("login_form_btc_address"));
//        query.sendKeys(login);
//
//        query = driver.findElement(By.id("login_form_password"));
//        query.sendKeys(password);
//
//        query = driver.findElement(By.id("login_button"));
//        query.submit();
//
//        WebDriverWait wait = new WebDriverWait(driver, 30);
//        query = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("free_play_captcha_types")));
//        query.sendKeys(Keys.ARROW_DOWN);
//        query.sendKeys(Keys.ENTER);
//
//
//
//        byte[] arrScreen = ((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES);
//        BufferedImage imageScreen = ImageIO.read(new ByteArrayInputStream(arrScreen));
//        query = driver.findElement(By.id("adcopy-puzzle-image-image"));
//        Dimension capDimension = query.getSize();
//        Point capLocation = query.getLocation();
//        BufferedImage imgCap = imageScreen.getSubimage(capLocation.x, capLocation.y, capDimension.width, capDimension.height);
//
//
//        try(FileOutputStream fos = new FileOutputStream(image)) {
//            ImageIO.write(imgCap, "jpg", fos);
//        }
//
//        messageAPI.sendCaptcha();
//
//        Thread.sleep(120_000);
//
//        String solvedCaptcha = messageAPI.getSolvedCaptcha();
//
//        query = driver.findElement(By.id("adcopy_response"));
//        query.sendKeys(solvedCaptcha);
//
//        query = driver.findElement(By.id("free_play_form_button"));
//        query.click();
//    }

}
