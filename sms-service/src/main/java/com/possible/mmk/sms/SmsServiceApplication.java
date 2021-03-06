package com.possible.mmk.sms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
/**
 *
 * @author Abayomi
 */
@SpringBootApplication
@EnableEurekaClient
@EnableCaching
@EnableFeignClients(basePackages = "com.possible.mmk.feign")
public class SmsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmsServiceApplication.class, args);
    }
}
