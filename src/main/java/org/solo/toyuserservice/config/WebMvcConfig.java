package org.solo.toyuserservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebMvcConfig {

    //eureka의 로드밸런싱을 사용하기 위해 webclient 설정
    @LoadBalanced
    @Bean
    public WebClient.Builder webClientBuilder() {

        return WebClient.builder();
    }
}
