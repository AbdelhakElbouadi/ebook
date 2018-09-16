package com.mouka.ebook.entity;

import javax.persistence.Entity;

/**
 *
 * @author nasser
 */
@Entity
public class Administrator extends Person{
    
    public Administrator() {
    }
    
    public Administrator(String fname, String lname, String email, String password){
        super(fname, lname, email, password);
    }

}
