package com.mouka.ebook.service;

import com.mouka.ebook.entity.Identifiable;
import com.mouka.ebook.exceptions.NonExistingEntityException;
import com.mouka.ebook.exceptions.PreExistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Abdelhak
 */
public abstract class ServiceFacade<T extends Identifiable> {
    
    private Class<T> entityClass;

    public ServiceFacade() {
    }

    public ServiceFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }
    
    protected abstract EntityManager getEntityManager();
    
    public T create(T entity) throws PreExistingEntityException{
        if(entity.getId() != null && findById(entity.getId()) != null){
            throw new PreExistingEntityException("The entity with id " + entity.getId()
             + " exist already.");
        }
        getEntityManager().persist(entity);
        getEntityManager().flush();
        
        return entity;
    }
    
    public T edit(T entity) throws NonExistingEntityException{
        if(entity.getId() == null || findById(entity.getId()) == null){
            throw new NonExistingEntityException("The entity didn't exist in the database");
        }
        T t = getEntityManager().merge(entity);
        
        return t;
    }
    
    public T remove(Long id) throws NonExistingEntityException{
        if(id == null || findById(id) == null){
            throw new NonExistingEntityException("The entity didn't exist in the database");
        }
        T entity = findById(id);
        getEntityManager().remove(entity);
        
        return entity;
    }
    
    public T findById(Long id){
        return getEntityManager().find(entityClass, id);
    }
    
    public List<T> findRange(int range, int start){
        return find(false, range, start);
    }
    
    public List<T> findAll(){
        return find(true, -1, -1);
    }
    
    protected List<T> find(boolean all, int range, int start){
        EntityManager entityManager = getEntityManager();
        List<T> entities =  null;
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
        query.select(root);
        TypedQuery typedQuery = entityManager.createQuery(query);
        if(!all){
            typedQuery.setFirstResult(start).setMaxResults(range);
        }
        
        entities = typedQuery.getResultList();
        
        return entities;
    }
    
}