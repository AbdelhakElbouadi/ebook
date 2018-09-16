package com.mouka.ebook.beans;

import com.mouka.ebook.entity.Administrator;
import com.mouka.ebook.entity.Customer;
import com.mouka.ebook.service.AdministratorService;
import java.io.Serializable;
import java.util.List;
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
public class UserBean implements Serializable{
    
    @Inject
    @New(value = Customer.class)
    private Customer customer;
    
    @EJB
    private AdministratorService adminService;
    
    
    public List<Administrator> getAllAdministrator(){
        List<Administrator> ads = null;
        
        ads = adminService.findAll();
        
        return ads;
    }
    
}
