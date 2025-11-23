package com.neohorizon.api.config;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.neohorizon.api.dto.ErrorResponseDTO;
import com.neohorizon.api.exception.BusinessException;
import com.neohorizon.api.exception.EntityNotFoundException;
import com.neohorizon.api.exception.ValidationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleEntityNotFoundException(
            EntityNotFoundException ex, WebRequest request) {
        
        ErrorResponseDTO errorResponse = ErrorResponseDTO.of(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(
            ValidationException ex, WebRequest request) {
        
        ErrorResponseDTO errorResponse = ErrorResponseDTO.of(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Error",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDTO> handleBusinessException(
            BusinessException ex, WebRequest request) {
        
        ErrorResponseDTO errorResponse = ErrorResponseDTO.of(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Business Rule Violation",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {
        
        ErrorResponseDTO errorResponse = ErrorResponseDTO.of(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {
        
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        
        ErrorResponseDTO errorResponse = ErrorResponseDTO.of(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                errorMessage,
                request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDTO> handleRuntimeException(
            RuntimeException ex, WebRequest request) {
        
        ErrorResponseDTO errorResponse = ErrorResponseDTO.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Erro interno do servidor",
                request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneralException(
            Exception ex, WebRequest request) {
        
        ErrorResponseDTO errorResponse = ErrorResponseDTO.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Erro inesperado no servidor",
                request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<ErrorResponseDTO> handleHttpMessageNotReadableException(
                HttpMessageNotReadableException ex, WebRequest request) {

        // Mensagem padrão caso a causa real não seja encontrada
        String errorMessage = "Erro ao ler o corpo da requisição. Verifique o formato dos campos.";

        Throwable rootCause = ex.getCause();

        // Loop para tentar encontrar a causa raiz específica (ValidationException ou MismatchedInputException)
        while (rootCause != null) {
                // Se a causa for sua ValidationException
                if (rootCause.getMessage() != null && rootCause.getMessage().contains("Cargo inválido")) {
                errorMessage = rootCause.getMessage();
                break; 
                }
                
                // Se a causa for um erro de enum/tipo de entrada (Jackson MismatchedInputException, que é a causa mais comum)
                if (rootCause instanceof com.fasterxml.jackson.databind.exc.MismatchedInputException ||
                rootCause instanceof com.fasterxml.jackson.core.JsonParseException) {
                
                // Tenta pegar a mensagem mais limpa
                String cleanMessage = rootCause.getMessage().split("problem:")[1].trim();
                // Remove a parte de localização ("at [Source:...") se estiver presente
                errorMessage = cleanMessage.substring(0, cleanMessage.indexOf("\n")).trim();
                break;
                }

                rootCause = rootCause.getCause(); // Desce um nível na cadeia de exceções
        }

        // Se a causa for a ValidationException, ela já contém a mensagem clara.
        if (ex.getCause() instanceof com.neohorizon.api.exception.ValidationException) {
                errorMessage = ex.getCause().getMessage();
        }
        
        // Fallback: se nada for encontrado, tenta usar a mensagem original do DTO, removendo os detalhes técnicos
        if (errorMessage.contains("Cannot construct instance of") || errorMessage.contains("Source: REDACTED")) {
                errorMessage = "Falha de tipo/formato em um campo da requisição.";
                if (ex.getCause() != null) {
                // Tenta pegar a mensagem da causa primária para maior detalhe
                errorMessage = ex.getCause().getMessage().split("problem:")[1].split("\n")[0].trim();
                }
        }


        ErrorResponseDTO errorResponse = ErrorResponseDTO.of(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request - Invalid Format",
                errorMessage, 
                request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

}
