package com.temah.adapter.conf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.temah.adapter.mapper.AlarmMapper;
import com.temah.common.alarm.dispatcher.AlarmDispatcher;
import com.temah.common.http.RestTemplateUtils;
import com.temah.common.json.CustomJacksonObjectMapper;
import com.temah.common.kafka.KafkaProducer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public KafkaProducer kafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        return new KafkaProducer(kafkaTemplate);
    }

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
    public AlarmDispatcher alarmDispatcher(KafkaProducer kafkaProducer) {
        ObjectMapper objectMapper = new CustomJacksonObjectMapper();
        RestTemplateUtils restTemplateUtils = new RestTemplateUtils(restTemplate());
        return new AlarmDispatcher(objectMapper, restTemplateUtils, kafkaProducer);
    }

    @Bean
    public AlarmMapper alarmMapper() {
        return new AlarmMapper();
    }
}
