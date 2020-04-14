package com.bot;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Prop {
    public static final String PROP_PATH = "src/main/resources/data.properties";

    public static String getDataFromProp(String key) {
        String data = null;
        FileInputStream fis;
        Properties prop = new Properties();
        try {
            fis = new FileInputStream(PROP_PATH);
            prop.load(fis);

            data = prop.getProperty(key);
        } catch (IOException ex) {
            System.out.println("Error.");
        }

        return data;
    }
}
