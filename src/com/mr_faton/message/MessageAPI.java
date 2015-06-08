package com.mr_faton.message;

import java.util.Map;

/**
 * Created by root on 05.06.2015.
 */
public interface MessageAPI {
    void sendCaptcha() throws Exception;

    String getSolvedCaptcha() throws Exception;

    void setMailFrom(Map<String, String> mapMailFrom);

    void setMailTo(String mailTo);
}
