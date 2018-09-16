package com.mouka.ebook.beans.converter;

import com.mouka.ebook.entity.Publisher;
import com.mouka.ebook.service.PublisherService;
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
public class PublisherConverter implements Converter{

    @EJB
    private PublisherService publisherService;
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
            String value) {
        if(value == null || value.isEmpty()){
            return null;
        }
        try{
            return publisherService.findById(Long.valueOf(value));
        }catch(NumberFormatException nfe){
            throw new ConverterException(new FacesMessage(Long.valueOf(value) 
                    + " is not a valid Publisher ID"), nfe);
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component,
            Object value) {
        if(value == null){
            return "";
        }
        
        if(value instanceof Publisher){
            Publisher publisher = (Publisher) value;
            return String.valueOf(publisher.getId());
        }else{
            throw new ConverterException(new FacesMessage(value + " is not a Publisher Object"));
        }
        
    }

}