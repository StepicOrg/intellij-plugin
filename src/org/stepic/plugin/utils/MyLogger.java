package org.stepic.plugin.utils;

import com.intellij.openapi.diagnostic.Logger;

public class MyLogger {
    private Logger LOG;
    private static MyLogger ourInstance = new MyLogger();

    public static MyLogger getInstance() {
        return ourInstance;
    }

    private MyLogger() {
        LOG = Logger.getInstance(MyLogger.class);
    }

    public Logger getLOG() {
        return LOG;
    }
}
