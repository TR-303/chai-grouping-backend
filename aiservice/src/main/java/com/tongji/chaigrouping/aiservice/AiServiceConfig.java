package com.tongji.chaigrouping.aiservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AiServiceConfig {
    @Bean
    public MoonshotAiUtils moonshotAiUtils() {
        return new MoonshotAiUtils();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
