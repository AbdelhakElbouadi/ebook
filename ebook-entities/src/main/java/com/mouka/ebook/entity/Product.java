package com.mouka.ebook.entity;

import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;

/**
 *
 * @author mmeh4
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Product implements Identifiable{
    
//    @TableGenerator(name = "product_gen",
//            table = "id_gen",
//            pkColumnName = "GEN_KEY",
//            valueColumnName = "GEN_VALUE",
//            pkColumnValue = "lastgenpro_id",
//            allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Field(index = Index.YES,analyze = Analyze.YES,store = Store.NO)
    @Analyzer(definition = "bookAnalyzer")
    private String description;
    
    private double price;
    
    @Column(name="percentage_off")
    private double percentageOff;
    
    @Column(name="onsale")
    private boolean onSale;
    
    @Column(name = "instock")
    private int inStock;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "last_updated")
    @Field(index = Index.YES,analyze = Analyze.YES,store = Store.NO)
    @DateBridge(resolution = Resolution.DAY)
    private Date lastUpdated;
    
    
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
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the percentageOff
     */
    public double getPercentageOff() {
        return percentageOff;
    }

    /**
     * @param percentageOff the percentageOff to set
     */
    public void setPercentageOff(double percentageOff) {
        this.percentageOff = percentageOff;
    }

    /**
     * @return the onSale
     */
    public boolean isOnSale() {
        return onSale;
    }

    /**
     * @param onSale the onSale to set
     */
    public void setOnSale(boolean onSale) {
        this.onSale = onSale;
    }

    /**
     * @return the inStock
     */
    public int getInStock() {
        return inStock;
    }

    /**
     * @param inStock the inStock to set
     */
    public void setInStock(int inStock) {
        this.inStock = inStock;
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

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.id);
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
        final Product other = (Product) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
