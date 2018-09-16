package com.mouka.ebook.entity;

import java.math.BigDecimal;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;

/**
 *
 * @author Abdelhak
 */

public class BankAccount implements Identifiable{

    @TableGenerator(name = "account_gen",
            table = "id_gen",
            pkColumnName = "GEN_KEY",
            valueColumnName = "GEN_VALUE",
            pkColumnValue = "lastgenaccount_id",
            allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "account_gen")
    private Long id;
    
    @Column(name = "available_balance")
    private BigDecimal availableBalance;
    
    @Column(name = "pending_balance")
    private BigDecimal pendingBalance;
    
    @Column(name="interest_rate")
    private BigDecimal monthlyInterestRate;
    
    @OneToOne(fetch = FetchType.EAGER, 
            cascade = {CascadeType.MERGE, CascadeType.REFRESH},
            mappedBy = "bankAccount",
            orphanRemoval = true)
    private BankCard bankCard;
    

    public BankAccount() {
    }
    
    public BankAccount(BigDecimal intialBalance, BigDecimal interest){
        this.availableBalance = intialBalance;
        this.monthlyInterestRate = interest;
    }
    
    @Override
    public Long getId() {
        return id;
    }
    
    public void setId(Long id){
        this.id = id;
    }

    /**
     * @return the availableBalance
     */
    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    /**
     * @param availableBalance the availableBalance to set
     */
    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }

    /**
     * @return the pendingBalance
     */
    public BigDecimal getPendingBalance() {
        return pendingBalance;
    }

    /**
     * @param pendingBalance the pendingBalance to set
     */
    public void setPendingBalance(BigDecimal pendingBalance) {
        this.pendingBalance = pendingBalance;
    }

    /**
     * @return the monthlyInterestRate
     */
    public BigDecimal getMonthlyInterestRate() {
        return monthlyInterestRate;
    }

    /**
     * @param monthlyInterestRate the monthlyInterestRate to set
     */
    public void setMonthlyInterestRate(BigDecimal monthlyInterestRate) {
        this.monthlyInterestRate = monthlyInterestRate;
    }

    /**
     * @return the bankCard
     */
    public BankCard getBankCard() {
        return bankCard;
    }

    /**
     * @param bankCard the bankCard to set
     */
    public void setBankCard(BankCard bankCard) {
        this.bankCard = bankCard;
    }

}