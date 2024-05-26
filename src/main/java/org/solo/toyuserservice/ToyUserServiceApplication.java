package org.solo.toyuserservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ToyUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToyUserServiceApplication.class, args);
    }

}
