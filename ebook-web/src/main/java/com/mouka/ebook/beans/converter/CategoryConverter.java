package com.mouka.ebook.beans.converter;

import com.mouka.ebook.entity.Category;
import com.mouka.ebook.service.CategoryService;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Named;

/**
 *
 * @author Abdelhak
 */
@Named
@RequestScoped
public class CategoryConverter implements Converter{

    @EJB
    private CategoryService categoryService;
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, 
            String value) {
        if(value == null || value.isEmpty())
            return null;
        
        return categoryService.findById(Long.parseLong(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, 
            Object value) {
        if(value == null)
            return "";
        if(value instanceof Category){
            Category c = (Category) value;
            return String.valueOf(c.getId());
        }else{
            throw new ConverterException(new FacesMessage(value + " is not a Book Category"));
        }
    }
    
}
