package com.mr_faton.browser;

/**
 * Created by root on 08.06.2015.
 */
public interface Browser {
    void checkURL();

    void login(String login, String password);

    long getSleepTime();

    void selectCaptcha();

    void saveCaptcha();

    void typeCaptcha(String solvedCaptcha);

    void pushEarnButton();

    boolean confirmEarning();

    void init();

    void setUrl(String url);

    void logout();

    void refresh();
}
