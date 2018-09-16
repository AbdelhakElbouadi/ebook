package com.mouka.ebook.beans;

import com.mouka.ebook.beans.util.JsfUtils;
import com.mouka.ebook.entity.Author;
import com.mouka.ebook.entity.Category;
import com.mouka.ebook.exceptions.NonExistingEntityException;
import com.mouka.ebook.exceptions.PreExistingEntityException;
import com.mouka.ebook.service.AuthorService;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.inject.New;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Abdelhak
 */
@Named
@ViewScoped
public class AuthorBean implements Serializable{
    
    @EJB
    private AuthorService authorService;
    
    @Inject
    @New(value= Author.class)
    private Author author;
    
    private boolean renderEditAuthorForm = false;
    private boolean renderDeleteAuthorForm = false;

    public AuthorBean() {
    }
    
    public String addAuthor(){
         try {
            if(getAuthor() != null){
                authorService.create(getAuthor());
                JsfUtils.addSuccessMessage("Author With Name " + getAuthor().getLname() + " " 
                        + getAuthor().getFname() + " created successfully.");
            }else{
                JsfUtils.addFailureMessage("Author Not Created Please Make Sure Info Are Correct.");
            }
        } catch (PreExistingEntityException ex) {
            Logger.getLogger(BookBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    public String editAuthor(){
        try {
            if(getAuthor() != null){
                authorService.edit(getAuthor());
                JsfUtils.addSuccessMessage("Author With Name " + getAuthor().getLname() + " " 
                        + getAuthor().getFname() + " updated successfully.");
            }else{
                JsfUtils.addFailureMessage("Author Not Updated Please Make Sure Info Are Correct.");
            }
        } catch (NonExistingEntityException ex) {
            Logger.getLogger(PublisherBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        setRenderEditAuthorForm(false);
        
        return null;
    }
    
    public String deleteAuthor(){
        try {
            if(getAuthor() != null){
                authorService.remove(getAuthor().getId());
                JsfUtils.addSuccessMessage("Author With Name " + getAuthor().getLname() + " " 
                        + getAuthor().getFname() + " deleted successfully.");
            }else{
                JsfUtils.addFailureMessage("Author Not Deleted Please Make Sure Info Are Correct.");
            }
        } catch (NonExistingEntityException ex) {
            Logger.getLogger(PublisherBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        setRenderDeleteAuthorForm(false);
        
        return null;
    }
    
    
    public String cancelAuthor(){
        FacesContext context = FacesContext.getCurrentInstance();
        Iterator<FacesMessage> it = context.getMessages();
        while(it.hasNext()){
            it.next();
            it.remove();
        }
        
        setRenderEditAuthorForm(false);
        setRenderDeleteAuthorForm(false);
        
        return null;
    }
    
    public void verifyDuplicateAuthorEmail(){
        Author auth = authorService.verifyAuthorByEmail(author.getEmail());
        if(auth != null){
            JsfUtils.addFailureMessage("This Email Is Already Binded To Another Author.");
        }
    }
    
    public List<Author> getAllAuthor(){
        return authorService.findAll();
    }
    
    public List<Category> getAuthorCategory(Long authorId){
        List<Category> categories = null;
        categories = authorService.getAuthorCategory(authorId);
        
        return categories;
    }

    /**
     * @return the author
     */
    public Author getAuthor() {
        return author;
    }

    /**
     * @param author the author to set
     */
    public void setAuthor(Author author) {
        this.author = author;
    }

    /**
     * @return the renderEditAuthorForm
     */
    public boolean isRenderEditAuthorForm() {
        return renderEditAuthorForm;
    }

    /**
     * @param renderEditAuthorForm the renderEditAuthorForm to set
     */
    public void setRenderEditAuthorForm(boolean renderEditAuthorForm) {
        this.renderEditAuthorForm = renderEditAuthorForm;
    }
    
    public String renderEditAuthorForm(Long authorId){
        
        author = authorService.findById(authorId);
        setRenderEditAuthorForm(true);
        
        return null;
    }

    /**
     * @return the renderDeleteAuthorForm
     */
    public boolean isRenderDeleteAuthorForm() {
        return renderDeleteAuthorForm;
    }

    /**
     * @param renderDeleteAuthorForm the renderDeleteAuthorForm to set
     */
    public void setRenderDeleteAuthorForm(boolean renderDeleteAuthorForm) {
        this.renderDeleteAuthorForm = renderDeleteAuthorForm;
    }
    
    public String renderDeleteAuthorForm(Long authorId){
        
        author = authorService.findById(authorId);
        setRenderDeleteAuthorForm(true);
        
        return null;
    }
    
}
