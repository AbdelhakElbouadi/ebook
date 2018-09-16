/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.mouka.ebook.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author Abdelhak
 */
@Entity
public class BookReportData  implements Serializable{
    
    @Id
    private Long id;
    private Long quantity;
    private Double totalAmount;
    
    public BookReportData() {
    }

    public BookReportData(Long id, Long quantity, Double totalAmount) {
        this.id = id;
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
        return Math.ceil(totalAmount);
    }

    /**
     * @param totalAmount the totalAmount to set
     */
    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BookReportData other = (BookReportData) obj;

        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    } 
    
}
