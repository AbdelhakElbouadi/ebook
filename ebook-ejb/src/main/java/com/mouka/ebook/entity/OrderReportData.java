package com.mouka.ebook.entity;

import com.mouka.ebook.entity.util.OrderStatus;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author Abdelhak
 */
@Entity
public class OrderReportData implements Serializable{
    
    @Id
    private Long id;
    private OrderStatus orderStatus;
    private Long quantity;
    private Double totalAmount;

    public OrderReportData() {
    }

    public OrderReportData(Long id, OrderStatus orderStatus, Long quantity, Double totalAmount) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the orderStatus
     */
    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    /**
     * @param orderStatus the orderStatus to set
     */
    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    /**
     * @return the quantity
     */
    public Long getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the totalAmount
     */
    public Double getTotalAmount() {
        return totalAmount;
    }

    /**
     * @param totalAmount the totalAmount to set
     */
    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
    
}
