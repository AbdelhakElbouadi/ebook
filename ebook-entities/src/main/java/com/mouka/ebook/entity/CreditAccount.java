package com.mouka.ebook.entity;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;

/**
 *
 * @author mmeh4
 */
public class CreditAccount extends BankAccount{
    
    @Column(name = "credit_limit")
    private BigDecimal creditLimit;

    public CreditAccount() {
    }

    /**
     * @return the creditLimit
     */
    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    /**
     * @param creditLimit the creditLimit to set
     */
    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }
       
}