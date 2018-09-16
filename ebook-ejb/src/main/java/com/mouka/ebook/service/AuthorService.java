package com.mouka.ebook.service;

import com.mouka.ebook.entity.Author;
import com.mouka.ebook.entity.Author_;
import com.mouka.ebook.entity.Category;
import com.mouka.ebook.entity.Category_;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

/**
 *
 * @author Abdelhak
 */
@Stateless
public class AuthorService extends ServiceFacade<Author>{

    @PersistenceContext
    private EntityManager entityManager;

    public AuthorService() {
        super(Author.class);
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }
    
    
    public List<Category> getAuthorCategory(Long authorId){
        List<Category> categories = null;
        
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Category> cq = cb.createQuery(Category.class);
        Root<Category> root = cq.from(Category.class);
        Join<Category, Author> join = root.join(Category_.authors);
        cq.select(root);
        cq.where(cb.equal(join.get(Author_.id), authorId));
        
        TypedQuery<Category> typedQuery = entityManager.createQuery(cq);
        categories = typedQuery.getResultList();
        
        return categories;
    }
    
    public Author verifyAuthorByEmail(String email){
        Author author = null;
        
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Author> cq = cb.createQuery(Author.class);
        Root<Author> root = cq.from(Author.class);
        
        cq.select(root);
        cq.where(cb.equal(root.get(Author_.email), email));
        TypedQuery<Author> query = entityManager.createQuery(cq);
        author = query.getSingleResult();
        
        return author;
    } 
    
}
