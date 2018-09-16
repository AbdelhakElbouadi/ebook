package com.mouka.ebook.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

/**
 *
 * @author Abdelhak
 */
@Entity
public class Customer extends Person{
    
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "customer_bankcards",
            joinColumns = @JoinColumn(name = "customer_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "bankcard_id", referencedColumnName = "id"))
    private List<BankCard> bankCards;
    
    @OneToMany(fetch = FetchType.LAZY, 
            cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<CustomerOrder> customerOrders;
        
    public Customer() {
        bankCards = new ArrayList<>();
        customerOrders = new ArrayList<>();
    }

    /**
     * @return the bankCards
     */
    public List<BankCard> getBankCards() {
        return bankCards;
    }

    /**
     * @param bankCards the bankCards to set
     */
    public void setBankCards(List<BankCard> bankCards) {
        this.bankCards = bankCards;
    }

    /**
     * @return the customerOrders
     */
    public List<CustomerOrder> getCustomerOrders() {
        return customerOrders;
    }
    
    public void addOrder(CustomerOrder order){
        this.getCustomerOrders().add(order);
        order.setCustomer(this);
    }
    
    public void removeOrder(CustomerOrder order){
        this.getCustomerOrders().remove(order);
        order.setCustomer(null);
    }

}