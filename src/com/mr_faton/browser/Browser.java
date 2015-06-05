package com.mr_faton.browser;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by root on 05.06.2015.
 */
public class Browser {
    public static void main(String[] args) {
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

        query = driver.findElement(By.id("free_play_form_button"));
        query.click();
    }
}
