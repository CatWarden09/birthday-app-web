package ru.catwarden.sltest;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

@Component
public class Config implements WebMvcConfigurer {
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

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String absolutePath = Paths.get("images").toAbsolutePath().toUri().toString();
        registry.addResourceHandler("/images/**")
                .addResourceLocations(absolutePath);

    }
}
