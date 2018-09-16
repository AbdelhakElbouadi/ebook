package com.mouka.ebook.beans.util;

import com.mouka.ebook.entity.Book;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.text.WordUtils;

/**
 *
 * @author Abdelhak
 */
@Named
@SessionScoped
public class JsfUtils implements Serializable{
    
    private static FacesContext context = FacesContext.getCurrentInstance();
    
    public static void addSuccessMessage(String message){
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,message, message));
    }
    
    public static void addFailureMessage(String message){
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
    }
    
    public String capitalize(String title){
        String newTitle = "";
        newTitle  = WordUtils.capitalize(title);
        
        return newTitle;
    }
    
    public String getBookFrontImage(Book book){
        String path = "";
        if(book.getImage() != null){
            path = "/image/" + book.getImage().getId();
        }else {
            path = "/resources/default/images/bookico.jpg";
        }
        
        return path;
    }
    
}
