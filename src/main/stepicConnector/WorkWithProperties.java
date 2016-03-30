package main.stepicConnector;

import javafx.util.Pair;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class WorkWithProperties {
//    "src/resources/client_info.properties"
//    client_id      -- as key
//    client_secret  -- as value
    public static Pair<String, String> getProperties() {

        Properties prop = new Properties();
        InputStream input = null;

        Pair<String, String> pair = null;
        try {
//            input = new FileInputStream("src/test/client_info.properties");
            input = new FileInputStream("src/resources/client_info.properties");
            prop.load(input);

            pair = new Pair<>(prop.getProperty("client_id"), prop.getProperty("client_secret"));
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return pair;
    }
}
