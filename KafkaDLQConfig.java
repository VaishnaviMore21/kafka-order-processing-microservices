//package com.example.processing_service.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import org.springframework.kafka.listener.DefaultErrorHandler;
//import org.springframework.kafka.core.KafkaTemplate;
//
//import org.springframework.util.backoff.FixedBackOff;
//
//@Configuration
//public class KafkaDLQConfig {
//
//    @Bean
//    public DefaultErrorHandler errorHandler(KafkaTemplate<String, String> kafkaTemplate) {
//
//        return new DefaultErrorHandler(
//                (record, exception) -> {
//                    System.out.println("Sending message to DLQ...");
////                    kafkaTemplate.send("orders-dlq", (String) record.value());
//                    kafkaTemplate.send("orders-dlq", record.value().toString());
//                },
//                new FixedBackOff(0L, 0) // no retry → direct DLQ
//        );
//    }
//}