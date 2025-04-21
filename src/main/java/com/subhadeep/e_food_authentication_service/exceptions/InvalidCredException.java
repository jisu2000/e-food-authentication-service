package com.subhadeep.e_food_authentication_service.exceptions;

public class InvalidCredException extends RuntimeException{

    public InvalidCredException(){
        super("Invalid Credentials");
    }
    
    public InvalidCredException(String msg){
        super(msg);
    }
}
