package com.mouka.ebook.beans.validator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;

/**
 *
 * @author Abdelhak
 */
@Named
public class RangeDateValidator implements Validator{

    @Override
    public void validate(FacesContext context, UIComponent component, Object value)
            throws ValidatorException {
    }
    
}
