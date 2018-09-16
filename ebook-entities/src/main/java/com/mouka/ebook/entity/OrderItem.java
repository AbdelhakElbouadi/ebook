/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mouka.ebook.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 *
 * @author nasser
 */
@Entity
@Table(name = "orderitem")
public class OrderItem implements Identifiable{

//     @TableGenerator(name = "orderdetails_gen",
//            table = "id_gen",
//            pkColumnName = "GEN_KEY",
//            valueColumnName = "GEN_VALUE",
//            pkColumnValue = "lastgenorderdetails_id",
//            allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @NotNull
    @Size(min = 1,message = "quantity must be bigger than zero")
    private int quantity;
    
    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "order_id",
            referencedColumnName = "id")
    private CustomerOrder order;
    
    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "ordered_product_id", 
            referencedColumnName = "id")
    private Product orderedProduct;

    public OrderItem() {
    }

    public OrderItem(Long id) {
        this.id = id;
    }

    public OrderItem(int quantity, CustomerOrder order, Product orderedProduct) {
        this.quantity = quantity;
        this.order = order;
        this.orderedProduct = orderedProduct;
    }
     
    @Override
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
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the order
     */
    public CustomerOrder getOrder() {
        return order;
    }

    /**
     * @param order the order to set
     */
    public void setOrder(CustomerOrder order) {
        this.order = order;
    }

    /**
     * @return the orderedProduct
     */
    public Product getOrderedProduct() {
        return orderedProduct;
    }

    /**
     * @param orderedProduct the orderedProduct to set
     */
    public void setOrderedProduct(Product orderedProduct) {
        this.orderedProduct = orderedProduct;
    }
    
}
