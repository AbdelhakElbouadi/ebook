package com.mouka.ebook.beans;

import com.mouka.ebook.entity.BankCard;
import java.io.Serializable;
import java.util.Date;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.New;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Abdelhak
 */
@Named
@SessionScoped
public class BankCardBean implements Serializable{
    
    private Date expDate;
    
    @Inject
    @New(BankCard.class)
    private BankCard bankCard;

    public BankCardBean() {
    }


    /**
     * @return the expDate
     */
    public Date getExpDate() {
        return expDate;
    }

    /**
     * @param expDate the expDate to set
     */
    public void setExpDate(Date expDate) {
        this.expDate = expDate;
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