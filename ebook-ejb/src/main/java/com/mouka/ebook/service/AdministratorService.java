package com.mouka.ebook.service;

import com.mouka.ebook.entity.Administrator;
import com.mouka.ebook.entity.Administrator_;
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
public class AdministratorService extends ServiceFacade<Administrator>{
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public AdministratorService() {
        super(Administrator.class);
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }
    
    public Administrator authenticateAdmin(String email, String pwd){
        Administrator manager = null;
        manager = findByEmail(email);
        if(manager != null){
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Administrator> query = builder.createQuery(Administrator.class);
            Root<Administrator> root = query.from(Administrator.class);
            query.select(root);
            query.where(builder.and(
                    builder.equal(root.get(Administrator_.password), pwd),
                    builder.equal(root.get(Administrator_.email), email)
            ));
            
            List<Administrator> listOfManagers = entityManager.createQuery(query).getResultList();
            if(!listOfManagers.isEmpty()){
                manager = listOfManagers.get(0);
            }else{
                manager = null;
            }
        }
        
        return manager;
    }
    
    public Administrator findByEmail(String email){
        Administrator manager  = null;
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Administrator> query = builder.createQuery(Administrator.class);
        Root<Administrator> root = query.from(Administrator.class);
        query.select(root);
        query.where(builder.equal(root.get(Administrator_.email), email));
        List<Administrator> listOfManagers = entityManager.createQuery(query).getResultList();
        if(!listOfManagers.isEmpty()){
            manager = listOfManagers.get(0);
        }
        
        return manager;
    }
    
}
