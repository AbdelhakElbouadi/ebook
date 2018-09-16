package com.mouka.ebook.beans.validator;

import com.mouka.ebook.beans.util.JsfUtils;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.apache.commons.validator.routines.UrlValidator;

/**
 *
 * @author Abdelhak
 */
@FacesValidator(value = "PublisherUrlValidator")
public class PublisherUrlValidator implements Validator{

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) 
            throws ValidatorException {
        String url = String.valueOf(value);
        UrlValidator validator = new UrlValidator();
        FacesMessage message = new FacesMessage();
        if(!(url.startsWith("http://") || url.startsWith("https://") 
                || url.startsWith("http://www") || url.startsWith("https://www"))){
            StringBuffer sb = new StringBuffer();
            sb.append("http://");
            sb.append(url);
            url = sb.toString();
        }
        
        if(!validator.isValid(url)){
            JsfUtils.addFailureMessage(value + " is not a valid url");
            throw new ValidatorException(new FacesMessage(value + " is not a valid url"));
        }
    }

}
