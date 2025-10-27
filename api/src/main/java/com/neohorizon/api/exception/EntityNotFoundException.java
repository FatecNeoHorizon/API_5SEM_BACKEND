package com.neohorizon.api.exception;

public class EntityNotFoundException extends RuntimeException {
    
    public EntityNotFoundException(String message) {
        super(message);
    }
    
    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public static EntityNotFoundException forId(String entityName, Object id) {
        return new EntityNotFoundException(String.format("%s com ID %s não encontrado", entityName, id));
    }
    
    public static EntityNotFoundException forField(String entityName, String field, Object value) {
        return new EntityNotFoundException(String.format("%s com %s '%s' não encontrado", entityName, field, value));
    }
}