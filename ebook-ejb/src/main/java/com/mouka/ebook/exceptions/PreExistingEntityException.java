package com.mouka.ebook.exceptions;

/**
 *
 * @author Abdelhak
 */
public class PreExistingEntityException extends Exception{

    PreExistingEntityException(){
        
    }
    
    public PreExistingEntityException(String msg){
        super(msg);
    }
}
