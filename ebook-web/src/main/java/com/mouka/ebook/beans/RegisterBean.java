package com.mouka.ebook.beans;

import com.mouka.ebook.beans.util.JsfUtils;
import com.mouka.ebook.entity.Administrator;
import com.mouka.ebook.entity.Customer;
import com.mouka.ebook.entity.Groups;
import com.mouka.ebook.entity.Person;

import com.mouka.ebook.exceptions.PreExistingEntityException;
import com.mouka.ebook.service.AdministratorService;
import com.mouka.ebook.service.CustomerService;
import com.mouka.ebook.service.GroupService;
import com.mouka.ebook.service.UserService;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.New;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Abdelhak
 */
@Named
@RequestScoped
public class RegisterBean implements Serializable{
    
    @EJB
    private UserService userService;
    
    @EJB
    private CustomerService customerService;
    
    @EJB
    private  AdministratorService managerService;
    
    @EJB
    private GroupService groupService;
    
    @Inject
    @New(value = Customer.class)
    private Customer user;
    
    @Inject
    @New(value = Administrator.class)
    private Administrator manager;
    
    public RegisterBean() {
    }
    
    
    public String registerUser(){
        
        try {
            if(user.getGroups().isEmpty()){
                Groups defaultGroup = getGroupService().findByName("Customer");
                if(defaultGroup != null)
                    user.addGroup(defaultGroup);
            }
            customerService.create(getUser());
        } catch (PreExistingEntityException ex) {
            Logger.getLogger(RegisterBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return "/index";
    }
    
    public String registerUserAsAdmin(){
        //This should be the manager not the customer
        try {
            if(manager.getGroups().isEmpty()){
                Groups defaultGroup = getGroupService().findByName("Admin");
                if(defaultGroup != null)
                    manager.addGroup(defaultGroup);
            }
            managerService.create(getManager());
        } catch (PreExistingEntityException ex) {
            Logger.getLogger(RegisterBean.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtils.addFailureMessage("There Is Already An Account Having This Address Email " + manager.getEmail());
            return null;
        }
        
        return "/admin/index";
    }
    
    public void verifyDuplicateAdminEmail(){
        Person p = userService.verifyUserByEmail(manager.getEmail());
        if(p != null){
            JsfUtils.addFailureMessage("This Email Exists Already Please Use Another Email.");
        }
    }
    
    
    public void verifyDuplicateCustomerEmail(){
        Person p = userService.verifyUserByEmail(user.getEmail());
        if(p != null){
            JsfUtils.addFailureMessage("This Email Exists Already Please Use Another Email.");
        }
    }
    
    /**
     * @return the customerService
     */
    public CustomerService getCustomerService() {
        return customerService;
    }
    
    /**
     * @param customerService the customerService to set
     */
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }
    
    /**
     * @return the managerService
     */
    public AdministratorService getManagerService() {
        return managerService;
    }
    
    /**
     * @param managerService the managerService to set
     */
    public void setManagerService(AdministratorService managerService) {
        this.managerService = managerService;
    }
    
    /**
     * @return the user
     */
    public Customer getUser() {
        return user;
    }
    
    /**
     * @param user the user to set
     */
    public void setUser(Customer user) {
        this.user = user;
    }
    
    /**
     * @return the manager
     */
    public Administrator getManager() {
        return manager;
    }
    
    /**
     * @param manager the manager to set
     */
    public void setManager(Administrator manager) {
        this.manager = manager;
    }
    
    /**
     * @return the groupService
     */
    public GroupService getGroupService() {
        return groupService;
    }
    
    /**
     * @param groupService the groupService to set
     */
    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
    }
    
}
