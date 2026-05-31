//package com.example.processing_service.entity;
//
//
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.Id;
//import jakarta.persistence.Table;
//import lombok.Data;
//import jakarta.persistence.EnumType;
//import jakarta.persistence.Enumerated;
//@Entity
//@Table(name = "orders")
//@Data
//public class Order {
//
//    @Id
//    private String orderId;
//
//    private String productName;
//    private double price;
//    private String status;
////@Enumerated(EnumType.STRING)
////private OrderStatus status;
//}

package com.example.processing_service.entity;

import jakarta.persistence.*;

@Entity
@Table(name="orders")
public class Order{

    @Id
    private String orderId;

    private String productName;
    private double price;

    @Enumerated(EnumType.STRING)   // 🔥 IMPORTANT
    private OrderStatus status;

    // Getters and Setters

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}