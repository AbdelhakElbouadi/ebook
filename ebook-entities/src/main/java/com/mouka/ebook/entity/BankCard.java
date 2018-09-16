package com.mouka.ebook.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mmeh4
 */
@Entity
@Table(name = "bank_card")
@XmlRootElement
public class BankCard implements Identifiable{

//    @TableGenerator(name = "card_gen",
//            table = "id_gen",
//            pkColumnName = "GEN_KEY",
//            valueColumnName = "GEN_VALUE",
//            pkColumnValue = "lastgencard_id",
//            allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name = "card_number")
    private String cardNumber;
    
    @Column(name = "cvv")
    private String cardVerificationValue;
    
    @Column(name = "exp_date")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date expDate;
    
//    @OneToOne(fetch = FetchType.EAGER, 
//            cascade = {CascadeType.MERGE, CascadeType.REFRESH})
//    @JoinColumn(name = "bank_account_id",
//            referencedColumnName = "id")
//    private BankAccount bankAccount;
    
    @ManyToMany(fetch = FetchType.LAZY, 
            cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            mappedBy = "bankCards")
    private List<Customer> cardUsers;

    public BankCard() {
        cardUsers = new ArrayList<>();
    }

    public BankCard(String cardNumber, String cardVerificationValue) {
        cardUsers = new ArrayList<>();
        this.cardNumber = cardNumber;
        this.cardVerificationValue = cardVerificationValue;
    }  
    
    
    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    /**
     * @return the cardNumber
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * @param cardNumber the cardNumber to set
     */
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    /**
     * @return the cardVerificationValue
     */
    public String getCardVerificationValue() {
        return cardVerificationValue;
    }

    /**
     * @param cardVerificationValue the cardVerificationValue to set
     */
    public void setCardVerificationValue(String cardVerificationValue) {
        this.cardVerificationValue = cardVerificationValue;
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

//    /**
//     * @return the bankAccount
//     */
//    public BankAccount getBankAccount() {
//        return bankAccount;
//    }
//
//    /**
//     * @param bankAccount the bankAccount to set
//     */
//    public void setBankAccount(BankAccount bankAccount) {
//        this.bankAccount = bankAccount;
//    }

    /**
     * @return the cardUsers
     */
    public List<Customer> getCardUsers() {
        return cardUsers;
    }

    /**
     * @param cardUsers the cardUsers to set
     */
    public void setCardUsers(List<Customer> cardUsers) {
        this.cardUsers = cardUsers;
    }
     
}