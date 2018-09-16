package com.mouka.ebook.exceptions;

/**
 *
 * @author Abdelhak
 */
public class NoEnoughFundsException extends Exception{

    public NoEnoughFundsException() {
    }

    public NoEnoughFundsException(String msg){
        super(msg);
    }
}