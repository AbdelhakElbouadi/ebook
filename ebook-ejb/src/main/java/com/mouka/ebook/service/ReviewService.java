package com.mouka.ebook.service;

import com.mouka.ebook.entity.Comment;
import com.mouka.ebook.entity.Comment_;
import com.mouka.ebook.entity.Customer;
import com.mouka.ebook.entity.Product;
import com.mouka.ebook.entity.Rate;
import com.mouka.ebook.entity.Rate_;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Abdelhak
 */
@Stateless
public class ReviewService extends ServiceFacade<Comment>{
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public ReviewService() {
        super(Comment.class);
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }
    
    public boolean addCustomerRate(Rate rate){
        boolean result = false;
        
        entityManager.persist(rate);
        
        return result;
    }
    
    public boolean addCustomerComment(Comment comment){
        boolean result = false;
        
        entityManager.persist(comment);
        
        return result;
    }
    
    public List<Comment> getAllCommentOnProduct(Long id){
        List<Comment> comments = null;
        Product prod = entityManager.find(Product.class, id);
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Comment> criteriaQuery = builder.createQuery(Comment.class);
        Root<Comment> root = criteriaQuery.from(Comment.class);
        criteriaQuery.select(root);
        criteriaQuery.where(builder.equal(root.get(Comment_.reviewedProduct), prod));
        comments = entityManager.createQuery(criteriaQuery).getResultList();
        if(comments.isEmpty()){
            //comments = null;
            //Avoid NullPointer
        }
        
        return comments;
    }
    
    public int getCustomerRateOnProduct(Long customerId,Long productId){
        int rate = -1;
        
        Customer cus = entityManager.find(Customer.class, customerId);
        Product prod = entityManager.find(Product.class, productId);
        if(cus!= null && prod!= null){
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Rate> criteriaQuery = builder.createQuery(Rate.class);
            Root<Rate> root = criteriaQuery.from(Rate.class);
            criteriaQuery.select(root);
            criteriaQuery.where(builder
                    .and(
                            builder.equal(root.get(Rate_.reviewer), cus),
                            builder.equal(root.get(Rate_.reviewedProduct), prod)
                    ));
            List<Rate> rates = entityManager.createQuery(criteriaQuery).getResultList();
            if(!rates.isEmpty()){
                rate = rates.get(0).getRate();
            }
        }
        
        return rate;
    }
    
}
