package com.mouka.ebook.exceptions;

/**
 *
 * @author Abdelhak
 */
public class NonExistingEntityException extends Exception{

    public NonExistingEntityException() {
    }
    
    public NonExistingEntityException(String msg){
        super(msg);
    }
    
}
