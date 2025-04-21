package com.subhadeep.e_food_authentication_service.exceptions;

public class InvalidRequestException extends RuntimeException{
    
    public InvalidRequestException(String msg){
        super(msg);
    }

    public InvalidRequestException(){
        super("Invalid Request");
    }
}
