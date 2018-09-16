package com.mouka.ebook.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 *
 * @author Abdelhak
 */
@Entity
@DiscriminatorValue(value = "Admin")
public class Administrator extends Person{
    
    public Administrator() {
    }
    
    public Administrator(String fname, String lname, String email, String password){
        super(fname, lname, email, password);
    }

}
