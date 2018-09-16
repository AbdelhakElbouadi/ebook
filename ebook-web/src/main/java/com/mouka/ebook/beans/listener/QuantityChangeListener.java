package com.mouka.ebook.beans.listener;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;

/**
 *
 * @author Abdelhak
 */
public class QuantityChangeListener implements ValueChangeListener{

    @Override
    public void processValueChange(ValueChangeEvent event) 
            throws AbortProcessingException {
         System.out.println("<<<<<<<<<<<<<<<<The Value has being changed from " + event.getOldValue()  
                    + " To " + event.getNewValue());
        if(event.getNewValue() != null){
            System.out.println("<<<<<<<<<<<<<<<<The Value has being changed from " + event.getOldValue()  
                    + " To " + event.getNewValue());
        }
    }
    
}
