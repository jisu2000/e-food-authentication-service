package com.subhadeep.e_food_authentication_service.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
        super("Resource Not found");
    }

    public ResourceNotFoundException(String msg) {
        super(msg);
    }

    public ResourceNotFoundException(String entityName, String fieldName, String value) {
        super(entityName + " not found with " + fieldName + " : " + value);
    }

}
