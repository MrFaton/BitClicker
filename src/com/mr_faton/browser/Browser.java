package com.mr_faton.browser;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by root on 05.06.2015.
 */
public class Browser {
    public static void main(String[] args) throws IOException {
        WebDriver driver = new FirefoxDriver();

        driver.get("https://freebitco.in");
        WebElement query = driver.findElement(By.id("switch_to_login_button"));
        query.click();

        query = driver.findElement(By.id("login_form_btc_address"));
        query.sendKeys("15h6BNwWHXQWtoiu6C1jTEGpZGxJugShgY");

        query = driver.findElement(By.id("login_form_password"));
        query.sendKeys("qwertyui");

        query = driver.findElement(By.id("login_button"));
        query.submit();

        WebDriverWait wait = new WebDriverWait(driver, 30);
        query = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("free_play_captcha_types")));
        query.sendKeys(Keys.ARROW_DOWN);
        query.sendKeys(Keys.ENTER);



        byte[] arrScreen = ((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES);
        BufferedImage imageScreen = ImageIO.read(new ByteArrayInputStream(arrScreen));
        query = driver.findElement(By.id("adcopy-puzzle-image-image"));
        Dimension capDimension = query.getSize();
        Point capLocation = query.getLocation();
        BufferedImage imgCap = imageScreen.getSubimage(capLocation.x, capLocation.y, capDimension.width, capDimension.height);


        try(FileOutputStream fos = new FileOutputStream("C:\\ttttttttttt\\img2.jpg")) {
            ImageIO.write(imgCap, "jpg", fos);
        }


//        query = driver.findElement(By.id("free_play_form_button"));
//        query.click();
    }
}
