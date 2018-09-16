/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mouka.ebook.entity;

import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author nasser
 */
@Entity
@Table(name = "rate")
public class Rate implements Identifiable{

//    @TableGenerator(name = "rate_gen",
//            table = "id_gen",
//            pkColumnName = "GEN_KEY",
//            valueColumnName = "GEN_VALUE",
//            pkColumnValue = "lastgenrate_id",
//            allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name = "date_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    
    //The rating normally is between one and five.
    @Size(min = 0, max = 5)
    private Integer rate;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product reviewedProduct;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer reviewer;

    public Rate() {
    }
    
    @Override
    public Long getId() {
        return id;
    }
    
    public void setId(Long id){
        this.id = id;
    }

    /**
     * @return the dateCreated
     */
    public Date getDateCreated() {
        return dateCreated;
    }

    /**
     * @param dateCreated the dateCreated to set
     */
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * @return the rate
     */
    public Integer getRate() {
        return rate;
    }

    /**
     * @param rate the rate to set
     */
    public void setRate(Integer rate) {
        this.rate = rate;
    }

    /**
     * @return the reviewedProduct
     */
    public Product getReviewedProduct() {
        return reviewedProduct;
    }

    /**
     * @param reviewedProduct the reviewedProduct to set
     */
    public void setReviewedProduct(Product reviewedProduct) {
        this.reviewedProduct = reviewedProduct;
    }

    /**
     * @return the reviewer
     */
    public Customer getReviewer() {
        return reviewer;
    }

    /**
     * @param reviewer the reviewer to set
     */
    public void setReviewer(Customer reviewer) {
        this.reviewer = reviewer;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.id);
        hash = 17 * hash + Objects.hashCode(this.dateCreated);
        hash = 17 * hash + Objects.hashCode(this.rate);
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
        final Rate other = (Rate) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.dateCreated, other.dateCreated)) {
            return false;
        }
        if (!Objects.equals(this.rate, other.rate)) {
            return false;
        }
        return true;
    }
       
}
