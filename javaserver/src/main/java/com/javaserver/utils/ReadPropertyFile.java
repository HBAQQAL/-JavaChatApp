package com.javaserver.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

public class ReadPropertyFile {
    public String DATABASE_URL;

    // this file can be used to read the properties from the config.properties file
    // and use it in the application
    // such as environment variables, database url, etc.
    // here i used it to read the database url from the config.properties file but i
    // no longer use it
    public ReadPropertyFile() {
        Properties properties = new Properties();

        try {
            FileInputStream fileInputStream = new FileInputStream("src/main/resources/config.properties");
            properties.load(fileInputStream);
            DATABASE_URL = properties.getProperty("DATABASE_URL");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}