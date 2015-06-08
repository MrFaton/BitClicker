package com.mr_faton.settings;

import java.util.Map;

/**
 * Created by root on 08.06.2015.
 */
public interface SettingsAPI {
    Map<String,String> getWalletsMap();

    String getUrl();

    Map<String,String> getMapMailFrom();

    String getMailTo();
}
