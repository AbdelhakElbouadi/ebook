package com.mouka.ebook.entity;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;

/**
 *
 * @author mmeh4
 */
public class DebitAccount extends BankAccount{
    
    @Column(name = "overdraft_fee")
    private BigDecimal overdraftFee;
    
    public DebitAccount(){    
    }

    public DebitAccount(BigDecimal overdraftFee) {
        this.overdraftFee = overdraftFee;
    }

    public DebitAccount(BigDecimal overdraftFee, BigDecimal intialBalance, BigDecimal interest) {
        super(intialBalance, interest);
        this.overdraftFee = overdraftFee;
    }
    
    /**
     * @return the overdraftFee
     */
    public BigDecimal getOverdraftFee() {
        return overdraftFee;
    }

    /**
     * @param overdraftFee the overdraftFee to set
     */
    public void setOverdraftFee(BigDecimal overdraftFee) {
        this.overdraftFee = overdraftFee;
    }

}