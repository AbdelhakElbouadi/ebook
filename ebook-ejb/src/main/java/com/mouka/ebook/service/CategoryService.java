package com.mouka.ebook.service;

import com.mouka.ebook.entity.Category;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Abdelhak
 */
@Stateless
public class CategoryService extends ServiceFacade<Category>{
    
    @PersistenceContext(unitName = "EbookPU")
    private EntityManager em;
    
    public CategoryService(){
        super(Category.class);
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    
    public List<Category> getAllCategory(){
        return findAll();
    }
    
}
