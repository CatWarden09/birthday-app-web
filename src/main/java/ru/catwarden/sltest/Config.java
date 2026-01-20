package ru.catwarden.sltest;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Component
public class Config {
    private Properties properties;

    public Config(){
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")){
            properties.load(fis);
        } catch (IOException exception){
            exception.printStackTrace();
        }
    }

    public String getDatabaseUrl(){
        return properties.getProperty("db.url");
    }

    public String getDatabaseUser(){
        return properties.getProperty("db.user");

    }

    public String getDatabasePassword(){
        return properties.getProperty("db.password");
    }
}
