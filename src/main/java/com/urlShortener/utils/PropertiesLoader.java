package com.urlShortener.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by tbuldina on 18/03/2018.
 */
public class PropertiesLoader {
    public static final String propertiesFileName = "config.properties";
    public static final String JETTY_PORT = "jetty.port";
    public static final String JETTY_HOST = "jetty.host";
    public static final String DATABASE_URL = "database.url";
    private static final Logger logger = Logger.getLogger(PropertiesLoader.class.getName());

    Properties properties = new Properties();

    public PropertiesLoader() {
        InputStream input = PropertiesLoader.class.getClassLoader().getResourceAsStream(propertiesFileName);
        try {
            properties.load(input);
            input.close();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Exception on loading properties file: " + e.getMessage());
            throw new RuntimeException("[ERROR] " + e.getMessage());
        }
    }

    public String getDatabaseUrl() {
        return properties.getProperty(DATABASE_URL);
    }

    public String getJettyHost() {
        return properties.getProperty(JETTY_HOST);
    }

    public Integer getJettyPort() {
        return Integer.valueOf(properties.getProperty(JETTY_PORT));
    }

    public void setJettyPort(String propertyValue) {
        properties.setProperty(JETTY_PORT, propertyValue);
    }
}
