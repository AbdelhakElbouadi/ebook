package com.mouka.ebook.beans.listener;

import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;

/**
 *
 * @author Abdelhak
 */
public class CategoryListener implements ValueChangeListener{

    @Override
    public void processValueChange(ValueChangeEvent event) 
            throws AbortProcessingException {
        if(event.getNewValue() != null){
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap().put("categoryName", event.getNewValue());
        }
    }
    
}
