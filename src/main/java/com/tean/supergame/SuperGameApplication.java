package com.tean.supergame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableLoadTimeWeaving;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;

@SpringBootApplication
public class SuperGameApplication {

    public static void main(String[] args) {
        SpringApplication.run(SuperGameApplication.class, args);
    }

}