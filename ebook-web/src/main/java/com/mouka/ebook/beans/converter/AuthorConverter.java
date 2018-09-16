package com.mouka.ebook.beans.converter;

import com.mouka.ebook.entity.Author;
import com.mouka.ebook.service.AuthorService;
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
public class AuthorConverter implements Converter{

    @EJB
    private AuthorService authorService;
            
    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
            String value) {
        if(value == null || value.isEmpty())
            return null;
        
        return authorService.findById(Long.parseLong(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if(value == null)
            return "";
        
        if(value instanceof Author){
            Author author = (Author) value;
            return String.valueOf(author.getId());
        }else{
            throw new ConverterException(new FacesMessage(value + " is not an author object"));
        }

    }
    
}
