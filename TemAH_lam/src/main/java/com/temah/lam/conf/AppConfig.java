package com.temah.lam.conf;

import com.temah.common.http.RestTemplateUtils;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    /*
    添加@LoadBalanced注解来赋予RestTemplate使用负载均衡的能力，
    这样就可以直接使用远程应用在注册中心注册时使用的spring.application.name来替代实际的host和port
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RestTemplateUtils restTemplateUtils() {
        return new RestTemplateUtils(restTemplate());
    }
}
