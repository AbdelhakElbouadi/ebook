package com.mouka.ebook.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author Abdelhak
 */
@Entity
public class StoreReportData implements Serializable {
    
    @Id
    private Long id;
    private String name;
    private int instock;
    private int rechargeAt;
    private int percentageOff;
    private Date lastUpdated;

    public StoreReportData() {
    }

    public StoreReportData(Long id, String name, int instock, int rechargeAt, int percentageOff, Date lastUpdated) {
        this.id = id;
        this.name = name;
        this.instock = instock;
        this.rechargeAt = rechargeAt;
        this.percentageOff = percentageOff;
        this.lastUpdated = lastUpdated;
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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the instock
     */
    public int getInstock() {
        return instock;
    }

    /**
     * @param instock the instock to set
     */
    public void setInstock(int instock) {
        this.instock = instock;
    }

    /**
     * @return the rechargeAt
     */
    public int getRechargeAt() {
        return rechargeAt;
    }

    /**
     * @param rechargeAt the rechargeAt to set
     */
    public void setRechargeAt(int rechargeAt) {
        this.rechargeAt = rechargeAt;
    }

    /**
     * @return the percentageOff
     */
    public int getPercentageOff() {
        return percentageOff;
    }

    /**
     * @param percentageOff the percentageOff to set
     */
    public void setPercentageOff(int percentageOff) {
        this.percentageOff = percentageOff;
    }

    /**
     * @return the lastUpdated
     */
    public Date getLastUpdated() {
        return lastUpdated;
    }

    /**
     * @param lastUpdated the lastUpdated to set
     */
    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    } 
    
}
