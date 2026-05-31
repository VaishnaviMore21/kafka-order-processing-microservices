//package com.example.processing_service.consumer;
//
//
//
//import com.example.processing_service.entity.Order;
//import com.example.processing_service.service.OrderProcessingService;
////import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//
//@Service
//public class OrderConsumer {
//
//    @Autowired
//    private OrderProcessingService service;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @KafkaListener(topics = "orders-topic", groupId = "order-group")
//    public void consume(String message) throws Exception {
//
//        Order order = objectMapper.readValue(message, Order.class);
//
//        System.out.println("Received Order: " + order.getProductName());
//
//        service.processOrder(order);
//    }
//}

package com.example.processing_service.consumer;

import com.example.processing_service.entity.Order;
import com.example.processing_service.service.OrderProcessingService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderProcessingService service;

    @KafkaListener(topics = "orders-topic", groupId = "order-group")
    public void consume(String message) throws Exception {

        Order order = objectMapper.readValue(message, Order.class);

        System.out.println("Received Order: " + order.getProductName());

        service.processOrder(order);
    }
}

