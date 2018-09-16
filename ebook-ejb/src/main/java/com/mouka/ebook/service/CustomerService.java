package com.mouka.ebook.service;

import com.mouka.ebook.entity.Customer;
import com.mouka.ebook.entity.Customer_;
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
public class CustomerService extends ServiceFacade<Customer>{
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public CustomerService() {
        super(Customer.class);
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }
    
    public Customer findByEmail(String email){
        Customer customer  = null;
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Customer> query = builder.createQuery(Customer.class);
        Root<Customer> root = query.from(Customer.class);
        query.select(root);
        query.where(builder.equal(root.get(Customer_.email), email));
        List<Customer> listCustomers = entityManager.createQuery(query).getResultList();
        if(!listCustomers.isEmpty()){
            customer = listCustomers.get(0);
        }
        
        return customer;
    }
    
    public Customer authenticateCustomer(String email, String pwd){
        Customer customer = null;
        customer = findByEmail(email);
        if(customer != null){
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Customer> query = builder.createQuery(Customer.class);
            Root<Customer> root = query.from(Customer.class);
            query.select(root);
            query.where(builder.and(
                    builder.equal(root.get(Customer_.password), pwd),
                    builder.equal(root.get(Customer_.email), email)
            ));
            
            List<Customer> listCustomers = entityManager.createQuery(query).getResultList();
            if(!listCustomers.isEmpty()){
                customer = listCustomers.get(0);
            }else{
                customer = null;
            }
        }
        
        return customer;
    }
    
}