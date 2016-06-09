package main.projectWizard;

import com.intellij.openapi.diagnostic.Logger;

/**
 * Created by Petr on 09.06.2016.
 */
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
