package com.kylin.core.web.event;


import com.kylin.core.utils.log.LatteLogger;

/**
 * Created by ywm
 */

public class UndefineEvent extends Event {
    @Override
    public String execute(String params) {
        LatteLogger.e("UndefineEvent", params);
        return null;
    }
}
