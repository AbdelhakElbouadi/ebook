package com.mouka.ebook.service;

import com.mouka.ebook.entity.Product;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;

/**
 *
 * @author Abdelhak
 */
@Stateless
public class ProductService extends ServiceFacade<Product>{

    @PersistenceContext(unitName = "EbookPU")
    private EntityManager entityManager;
    
    public ProductService(){
        super(Product.class);
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }
    
    public List<Product> makeGeneralSearch(String query){
        
        FullTextEntityManager fullTextEntityManager =
                org.hibernate.search.jpa.Search.getFullTextEntityManager(entityManager);
        try {
            fullTextEntityManager.createIndexer().startAndWait();
        } catch (InterruptedException ex) {
            Logger.getLogger(BookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        QueryBuilder qb = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder().forEntity(Product.class).get();
        org.apache.lucene.search.Query q = qb
                .keyword()
                .onFields("name", "title","description", "authors.lname","authors.fname","publisher.name")
                .matching(query)
                .createQuery();
        Query jpaQuery = fullTextEntityManager.createFullTextQuery(q, Product.class);
        List<Product> products = jpaQuery.getResultList();
        
        return products;
    }

}
