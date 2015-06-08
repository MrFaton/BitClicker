package com.mr_faton;

import com.mr_faton.browser.Browser;
import com.mr_faton.browser.impl.FireFoxBrowser;
import com.mr_faton.message.MessageAPI;
import com.mr_faton.message.impl.EmailMessenger;
import com.mr_faton.settings.SettingsAPI;
import com.mr_faton.settings.impl.XMLSettingsHandler;

import java.util.Map;

/**
 * Created by root on 08.06.2015.
 */
public class BitClicker {
    private static Browser browser;
    private static MessageAPI messageAPI;
    private static SettingsAPI settingsAPI;

    static {
        browser = new FireFoxBrowser();
        messageAPI = new EmailMessenger();
        settingsAPI = new XMLSettingsHandler();
    }

    public static void main(String[] args) throws InterruptedException {
        browser.init();

        Map<String, String> mapMailFrom = settingsAPI.getMapMailFrom();
        String mailTo = settingsAPI.getMailTo();

        String url = settingsAPI.getUrl();
        Map<String, String> walletList = settingsAPI.getWalletsMap();

        if (walletList == null || url == null) {
            System.err.println("Произошла ошибка... Возможные причиты:\n" +
                    "* список кошельков пуст\n" +
                    "* не указан ЮРЛ сайта");
            Thread.sleep(Long.MAX_VALUE);
        }

        messageAPI.setMailFrom(mapMailFrom);
        messageAPI.setMailTo(mailTo);

        browser.setUrl(url);


        browser.checkURL();
        long sleepTime;
        while (true) {
            for (Map.Entry<String, String> entry : walletList.entrySet()) {
                browser.login(entry.getKey(), entry.getValue());
                Thread.sleep(2_000);
                sleepTime = browser.getSleepTime();
                if (sleepTime == 0) {
                    browser.selectCaptcha();
                    boolean isEarn = false;
                    while (isEarn == false) {
                        browser.saveCaptcha();
                        try {
                            messageAPI.sendCaptcha();
                        } catch (Exception e) {
                            System.err.println("Произошла ошибка: отправка каптчи по почте не удалась...");
                            Thread.sleep(Long.MAX_VALUE);
                        }

                        String solvedCaptcha = getSolvedCaptcha();
                        browser.typeCaptcha(solvedCaptcha);
                        browser.pushEarnButton();
                        isEarn = browser.confirmEarning();
                        if (isEarn) {
                            browser.logout();
                        } else {
                            browser.refresh();
                        }
                    }
                } else {
                    System.out.println("must sleep: " + sleepTime);
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    private static String getSolvedCaptcha() throws InterruptedException {
        int i = 0;
        String solvedCaptcha = null;
        while (solvedCaptcha == null) {
            i++;
            if (i <= 4) {
                Thread.sleep(2 * 60 * 1000);
            } else if (i <= 8) {
                Thread.sleep(3 * 60 * 1000);
            } else if (i <= 12) {
                Thread.sleep(4 * 60 * 1000);
            } else {
                Thread.sleep(5 * 60 * 1000);
            }

            try {
                solvedCaptcha = messageAPI.getSolvedCaptcha();
            } catch (Exception e) {
                System.err.println("Произошла ошибка: получение каптчи по почте не удалось...");
                e.printStackTrace();
                Thread.sleep(Long.MAX_VALUE);
            }
        }
        return solvedCaptcha;
    }
}
