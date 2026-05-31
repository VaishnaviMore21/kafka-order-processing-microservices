//package com.example.processing_service.service;
//
//
//import com.example.processing_service.entity.Order;
//import com.example.processing_service.repository.OrderRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class OrderProcessingService {
//
//    @Autowired
//    private OrderRepository repository;
//
//    public void processOrder(Order order) throws InterruptedException {
//
//        order.setStatus("PROCESSING");
//        repository.save(order);
//
//        Thread.sleep(2000); // simulate work
//
//        order.setStatus("COMPLETED");
//        repository.save(order);
//    }
//}
package com.example.processing_service.service;

import com.example.processing_service.entity.Order;
import com.example.processing_service.entity.OrderStatus;
import com.example.processing_service.repository.OrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderProcessingService {

    @Autowired
    private OrderRepository repository;

    public void processOrder(Order order) {

        try {

            // Step 1: PROCESSING
            order.setStatus(OrderStatus.PROCESSING);
            repository.save(order);

            // Step 2: Failure condition (for DLQ testing)
            if (order.getPrice() > 100000) {
                throw new RuntimeException("High value order failed");
            }

            // Step 3: COMPLETED
            order.setStatus(OrderStatus.COMPLETED);
            repository.save(order);

        } catch (Exception e) {

            // Step 4: FAILED
            order.setStatus(OrderStatus.FAILED);
            repository.save(order);

            // 🔥 IMPORTANT → triggers DLQ
            throw e;
        }
    }
}