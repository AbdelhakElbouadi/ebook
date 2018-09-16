package com.mouka.ebook.beans;

import com.mouka.ebook.beans.util.JsfUtils;
import com.mouka.ebook.entity.Publisher;
import com.mouka.ebook.exceptions.NonExistingEntityException;
import com.mouka.ebook.exceptions.PreExistingEntityException;
import com.mouka.ebook.service.PublisherService;
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
public class PublisherBean implements Serializable{
    
    @EJB
    private PublisherService publisherService;
    
    @Inject
    @New(Publisher.class)
    private Publisher publisher;
    
    private boolean renderEditPublisherForm = false;
    private boolean renderDeletePublisherForm = false;

    public PublisherBean() {
    }
        
    public String addPublisher(){
        try {
            
            if(getPublisher().getName() != null && getPublisher().getPublisherUrl() != null){
                publisherService.create(getPublisher());
                JsfUtils.addSuccessMessage("Publisher With Name " + getPublisher().getName() + " created successfully.");
            }else{
                JsfUtils.addFailureMessage("Publisher Not Created Please Make Sure Info Are Correct.");
            }
        } catch (PreExistingEntityException ex) {
            Logger.getLogger(BookBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return "/admin/publisher/View";
    }
    
    public String editPublisher(){
        try {
            
            if(getPublisher().getName() != null && getPublisher().getPublisherUrl() != null){
                publisherService.edit(getPublisher());
                JsfUtils.addSuccessMessage("Publisher With Name " + getPublisher().getName() + " updated successfully.");
            }else{
                JsfUtils.addFailureMessage("Publisher Not Updated Please Make Sure Info Are Correct.");
            }
        } catch (NonExistingEntityException ex) {
            Logger.getLogger(PublisherBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        setRenderEditPublisherForm(false);
        
        return null;
    }
    
    public String deletePublisher(){ 
        try {
            
            if(getPublisher().getName() != null && getPublisher().getPublisherUrl() != null){
                publisherService.remove(getPublisher().getId());
                JsfUtils.addSuccessMessage("Publisher With Name " + getPublisher().getName() + " removed successfully.");
            }else{
                JsfUtils.addFailureMessage("Publisher Not Removed Please Make Sure Info Are Correct.");
            }
        } catch (NonExistingEntityException ex) {
            Logger.getLogger(PublisherBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        setRenderDeletePublisherForm(false);
        
        return null;
    }
    
    public String cancelPublisher(){
        FacesContext context = FacesContext.getCurrentInstance();
        Iterator<FacesMessage> it = context.getMessages();
        while(it.hasNext()){
            it.next();
            it.remove();
        }
        
        setRenderDeletePublisherForm(false);
        setRenderEditPublisherForm(false);
        
        return null;
    }
        
    public List<Publisher> getAllPublisher(){
        List<Publisher> all = null;
        all = publisherService.findAll();
        
        return all;
    }

    /**
     * @return the publisher
     */
    public Publisher getPublisher() {
        return publisher;
    }

    /**
     * @param publisher the publisher to set
     */
    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    /**
     * @return the renderEditPublisherForm
     */
    public boolean isRenderEditPublisherForm() {
        return renderEditPublisherForm;
    }

    /**
     * @param renderEditPublisherForm the renderEditPublisherForm to set
     */
    public void setRenderEditPublisherForm(boolean renderEditPublisherForm) {
        this.renderEditPublisherForm = renderEditPublisherForm;
    }
    
    public String renderEditPublisherForm(Long pubId){
        
        publisher = publisherService.findById(pubId);
        setRenderEditPublisherForm(true);
        
        return null;
    }

    /**
     * @return the renderDeletePublisherForm
     */
    public boolean isRenderDeletePublisherForm() {
        return renderDeletePublisherForm;
    }

    /**
     * @param renderDeletePublisherForm the renderDeletePublisherForm to set
     */
    public void setRenderDeletePublisherForm(boolean renderDeletePublisherForm) {
        this.renderDeletePublisherForm = renderDeletePublisherForm;
    }
    
    public String renderDeletePublisherForm(Long pubId){
        
        publisher = publisherService.findById(pubId);
        setRenderDeletePublisherForm(true);
        
        return null;
    }
    
}
