package ru.maxima.booksshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BooksShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(BooksShopApplication.class, args);
    }



}
