package com.mouka.ebook.service;

import com.mouka.ebook.entity.Image;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Abdelhak
 */
@Stateless
public class ImageService {
    
    @PersistenceContext
    private EntityManager em;
    
    public Image findImageById(Long id){
        Image result = null;
        
        result = em.find(Image.class, id);
        
        return result;
    }
}
