package com.mouka.ebook.service;

import com.mouka.ebook.entity.Groups;
import com.mouka.ebook.entity.Groups_;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Abdelhak
 */
@Stateless
public class GroupService extends ServiceFacade<Groups>{
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public GroupService() {
        super(Groups.class);
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }
    
    
    public Groups findByName(String name){
        Groups resultedGroup = null;
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Groups> query = builder.createQuery(Groups.class);
        Root<Groups> root = query.from(Groups.class);
        query.select(root);
        query.where(builder.equal(root.get(Groups_.name), name));
        TypedQuery<Groups> typedQuery = entityManager.createQuery(query);
        List<Groups> listOfGroups = typedQuery.getResultList();
        if(listOfGroups.size() > 0){
            resultedGroup = listOfGroups.get(0);
        }
        
        return resultedGroup;
    }
}
