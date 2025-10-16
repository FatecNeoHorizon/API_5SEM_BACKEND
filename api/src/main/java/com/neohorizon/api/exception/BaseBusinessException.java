package com.neohorizon.api.exception;

/**
 * Classe base para exceções de negócio customizadas.
 * Extende RuntimeException para não ser uma checked exception.
 */
public abstract class BaseBusinessException extends RuntimeException {
    
    private final String errorCode;
    
    protected BaseBusinessException(String message) {
        super(message);
        this.errorCode = getClass().getSimpleName();
    }
    
    protected BaseBusinessException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = getClass().getSimpleName();
    }
    
    protected BaseBusinessException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
    
    protected BaseBusinessException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
}