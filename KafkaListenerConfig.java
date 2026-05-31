//package com.example.processing_service.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.kafka.listener.DefaultErrorHandler;
//
//@Configuration
//public class KafkaListenerConfig {
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
//            ConsumerFactory<String, String> consumerFactory,
//            DefaultErrorHandler errorHandler) {
//
//        ConcurrentKafkaListenerContainerFactory<String, String> factory =
//                new ConcurrentKafkaListenerContainerFactory<>();
//
//        factory.setConsumerFactory(consumerFactory);
//
//        // 🔥 attach DLQ handler
//        factory.setCommonErrorHandler(errorHandler);
//
//        return factory;
//    }
//}
//
//package com.example.processing_service.config;
//
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.common.serialization.StringDeserializer;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//import org.springframework.kafka.core.ConsumerFactory;


//changed by vaishnavi on 20th march

//import org.springframework.kafka.listener.DefaultErrorHandler;
//import org.springframework.kafka.core.KafkaTemplate;
//
//import org.springframework.util.backoff.FixedBackOff;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//public class KafkaListenerConfig {
//
//    // ✅ Consumer Config
//    @Bean
//    public ConsumerFactory<String, String> consumerFactory() {
//
//        Map<String, Object> config = new HashMap<>();
//
//        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        config.put(ConsumerConfig.GROUP_ID_CONFIG, "order-group");
//        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//
//        return new DefaultKafkaConsumerFactory<>(config);
//    }
//
//    // ✅ DLQ Error Handler (NO RETRY → DIRECT DLQ)
//    @Bean
//    public DefaultErrorHandler errorHandler(KafkaTemplate<String, String> kafkaTemplate) {
//
//        return new DefaultErrorHandler(
//                (record, exception) -> {
//                    System.out.println("Sending message to DLQ...");
//                    //kafkaTemplate.send("orders-dlq", record.value());
//                    kafkaTemplate.send("orders-dlq", record.value().toString());
//                },
//                new FixedBackOff(0L, 0) // 🔥 no retry
//        );
//    }
//
//    // ✅ Kafka Listener Factory (IMPORTANT LINK)
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
//            ConsumerFactory<String, String> consumerFactory,
//            DefaultErrorHandler errorHandler) {
//
//        ConcurrentKafkaListenerContainerFactory<String, String> factory =
//                new ConcurrentKafkaListenerContainerFactory<>();
//
//        factory.setConsumerFactory(consumerFactory);
//
//        // 🔥 THIS LINE CONNECTS DLQ
//        factory.setCommonErrorHandler(errorHandler);
//
//        return factory;
//    }


package com.example.processing_service.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.ConsumerFactory;

import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;

import org.springframework.util.backoff.FixedBackOff;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaListenerConfig {


    @Bean
    public ConsumerFactory<String, String> consumerFactory() {

        Map<String, Object> config = new HashMap<>();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "order-group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(config);
    }


    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<String, String> kafkaTemplate) {

        return new DefaultErrorHandler(
                (record, exception) -> {
                    System.out.println("Sending message to DLQ...");
                    //kafkaTemplate.send("orders-dlq", record.value());
                    kafkaTemplate.send("orders-dlq", record.value().toString());
                },
                new FixedBackOff(0L, 0)
        );
    }

    // ✅ Kafka Listener Factory + Rebalancing
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
            ConsumerFactory<String, String> consumerFactory,
            DefaultErrorHandler errorHandler) {

        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(errorHandler);

        // 🔥 Enable parallel consumers (important for rebalancing)
        factory.setConcurrency(2);

        // 🔥 Rebalance Listener
        factory.getContainerProperties().setConsumerRebalanceListener(
                new ConsumerRebalanceListener() {

                    @Override
                    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                        System.out.println("❌ Revoked: " + partitions);
                    }

                    @Override
                    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                        System.out.println("✅ Assigned: " + partitions);
                    }
                }
        );

        return factory;
    }
}