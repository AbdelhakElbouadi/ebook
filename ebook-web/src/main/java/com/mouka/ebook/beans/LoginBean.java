package com.mouka.ebook.beans;

import com.mouka.ebook.beans.util.JsfUtils;
import com.mouka.ebook.entity.Person;
import com.mouka.ebook.entity.constraint.LoggedIn;
import com.mouka.ebook.service.UserService;
import java.io.Serializable;
import java.security.Principal;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Abdelhak
 */
@Named
@SessionScoped
public class LoginBean implements Serializable{
    
    @EJB
    private UserService userService;
    
    @Inject
    @LoggedIn
    private Person actualUser;
   
    private String email;
    private String password;
    private String recoverEmail;
    private boolean rememberMe;
    private boolean admin = false;
    private boolean recoverRequestSent = false;
    
    public LoginBean() {
    }
    
    public String login(){
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        String result = "";
        
        if((actualUser = userService.verifyUserByEmail(email)) != null){
            if(userService.authenticateUser(email, password) == null){//We have already the person entity.
                JsfUtils.addFailureMessage("The Login Info are not correct.Sorry! Try again");
                result = "/login.xhtml";
                return result;
            }
            try {
                request.login(email, password);
                List<String> roles = userService.getUserRole(actualUser);
                if(!roles.isEmpty() && roles.contains("Customer")){//Admin User If Role Is Null Then It Will Catch Here
                     setAdmin(false);
                    result = "/index.xhtml?faces-redirect=true";
                }else if(!roles.isEmpty() && roles.contains("Admin")){
                   //go as admin
                    setAdmin(true);
                    result = "/admin/index.xhtml?faces-redirect=true";
                }else{
                    //Nothing
                }               
            } catch (ServletException ex) {
                Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, ex);
            }  
        }
        
        return result;
    }
    
    public String logout(){
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        try {
            this.actualUser = null;
            request.logout();
            request.getSession(false).invalidate();
        } catch (ServletException ex) {
            Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return "/index.xhtml?faces-redirect=true";
    }
    
    public boolean isLoggedIn(String email){
        boolean loggedIn = false;
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        Principal user = externalContext.getUserPrincipal();
        if(user != null && user.getName().equals(email)){
            loggedIn = true;
        }
        
        return loggedIn;
    }
    
    public boolean isUserLoggedIn(){
        boolean userLoggedIn = false;
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        Principal user = externalContext.getUserPrincipal();
        if(user != null)
            userLoggedIn = true;
        
        return userLoggedIn;
    }
    
    public boolean isAdmin(){
            
        return admin;
    }
    
    @Produces
    @LoggedIn
    public Person getLoggedUser(){
        return actualUser;
    }
    
    public String recoverPassword(){
        String customerEmail= "", customerPassword = "";
        
        FacesContext context = FacesContext.getCurrentInstance();
        String username =  context.getExternalContext().getInitParameter("smtpAccountEmail");
        String password = context.getExternalContext().getInitParameter("smtpAccountPassword");
        
        Person person = userService.findByEmail(recoverEmail);
        if(person !=  null){
            customerPassword = person.getPassword();
            Properties props = new Properties();
            
            //SSL Connection
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class",
                    "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");
            
            
            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    }
            );
            
            try {
                
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(username));
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(recoverEmail));
                message.setSubject("Ebook Help");
                message.setText("Your Password is : " + customerPassword);
                
                Transport.send(message);
                
                System.out.println("Done");
                
            } catch (MessagingException e) {
                //e.printStackTrace();
                Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, e.getMessage());
            }    
            
            setRecoverRequestSent(true);
        }else{
            JsfUtils.addFailureMessage("The Email You Provide Is Not Correct");
            setRecoverRequestSent(false);
        }
        
        return null;
    }
    
    /**
     * @return the userService
     */
    public UserService getUserService() {
        return userService;
    }
    
    /**
     * @param userService the userService to set
     */
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    
    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the recoverEmail
     */
    public String getRecoverEmail() {
        return recoverEmail;
    }

    /**
     * @param recoverEmail the recoverEmail to set
     */
    public void setRecoverEmail(String recoverEmail) {
        this.recoverEmail = recoverEmail;
    }
    
    /**
     * @return the rememberMe
     */
    public boolean isRememberMe() {
        return rememberMe;
    }
    
    /**
     * @param rememberMe the rememberMe to set
     */
    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    /**
     * @param admin the admin to set
     */
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    /**
     * @return the recoverRequestSent
     */
    public boolean isRecoverRequestSent() {
        return recoverRequestSent;
    }

    /**
     * @param recoverRequestSent the recoverRequestSent to set
     */
    public void setRecoverRequestSent(boolean recoverRequestSent) {
        this.recoverRequestSent = recoverRequestSent;
    }
    
}