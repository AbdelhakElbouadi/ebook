package com.mouka.ebook.beans.util;

/**
 *
 * @author Abdelhak
 */
public enum CheckoutPaymentError {
    UNAUTHORIZED(300, "Sorry, there is an error actually we are going to fix it."),
    BAD_REQ_PARAMETER_MISSING(400, "Please Fill All The Necessary Data Before Submitting."),
    BAD_REQ_DATA_MISSING(401, "Please Fill All The Necessary Data Before Submitting."), 
    REQUEST_FAILED_RETRY(200, "Sorry, There is a workload Try Again."),
    REQUEST_FAILED(500, "Sorry, there is an error actually we are going to fix it.");
    
    private int code;
    private String message;
    
    CheckoutPaymentError(int code, String message){
        this.code = code;
        this.message = message;                
    }

    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    public CheckoutPaymentError getValueFromCode(int code){
        switch(code){
            case 300: 
                return CheckoutPaymentError.UNAUTHORIZED;
            case 400: 
                return CheckoutPaymentError.BAD_REQ_PARAMETER_MISSING;
            case 401:
                return CheckoutPaymentError.BAD_REQ_DATA_MISSING;
            case 200:
                return CheckoutPaymentError.REQUEST_FAILED_RETRY;
            case 500:
                return CheckoutPaymentError.REQUEST_FAILED;
            default :
                return CheckoutPaymentError.REQUEST_FAILED_RETRY;
        }
    }
}
