package com.mouka.ebook.service;

import com.mouka.ebook.entity.Publisher;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Abdelhak
 */
@Stateless
public class PublisherService extends ServiceFacade<Publisher>{

    @PersistenceContext(unitName = "EbookPU")
    private EntityManager entityManager;
    
    public PublisherService() {
        super(Publisher.class);
    }
    
    public List<Publisher> getAllPublisher(){
        return findAll();
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }
}
