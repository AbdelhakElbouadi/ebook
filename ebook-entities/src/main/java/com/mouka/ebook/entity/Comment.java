/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mouka.ebook.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author nasser
 */
@Entity
@Table(name="comment")
@XmlRootElement
public class Comment implements Identifiable{

//    @TableGenerator(name = "comment_gen",
//            table = "id_gen",
//            pkColumnName = "GEN_KEY",
//            valueColumnName = "GEN_VALUE",
//            pkColumnValue = "lastgencomm_id",
//            allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name = "date_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    
    
    @NotNull
    private String content;
    
    @OneToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product reviewedProduct;
    
    @OneToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer reviewer;
    
    @ManyToMany(cascade = {CascadeType.MERGE,CascadeType.REFRESH})
    @JoinTable(name="comment_replies",
            joinColumns = @JoinColumn(name = "comment_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "reply_id", referencedColumnName = "id"))
    private List<Comment> replyComments = new ArrayList<>();

    public Comment() {
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
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
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

    /**
     * @return the replyComments
     */
    public List<Comment> getReplyComments() {
        return replyComments;
    }

    /**
     * @param replyComments the replyComments to set
     */
    public void setReplyComments(List<Comment> replyComments) {
        this.replyComments = replyComments;
    }
    
    public void addReplyComment(Comment reply){
        this.getReplyComments().add(reply);
    }
    
    public void removeReplyComment(Comment reply){
        this.getReplyComments().remove(reply);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.id);
        hash = 37 * hash + Objects.hashCode(this.dateCreated);
        hash = 37 * hash + Objects.hashCode(this.content);
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
        final Comment other = (Comment) obj;
        if (!Objects.equals(this.content, other.content)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.dateCreated, other.dateCreated)) {
            return false;
        }
        return true;
    }
    
}
