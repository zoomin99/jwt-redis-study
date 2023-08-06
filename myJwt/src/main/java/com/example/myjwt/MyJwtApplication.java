package com.example.myjwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@ConfigurationPropertiesScan
public class MyJwtApplication {

    @PostConstruct
    public void timeZoneSetting() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

    public static void main(String[] args) {
        SpringApplication.run(MyJwtApplication.class, args);
    }

}
