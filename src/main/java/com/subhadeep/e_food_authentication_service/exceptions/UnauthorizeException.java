package com.subhadeep.e_food_authentication_service.exceptions;

public class UnauthorizeException extends RuntimeException{
    
    public UnauthorizeException(){
        super("Request has been blocked due to Unauthenticed source");
    }

    public UnauthorizeException(String msg){
        super(msg);
    }

}