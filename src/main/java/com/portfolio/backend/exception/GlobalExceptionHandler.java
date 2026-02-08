package com.portfolio.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Manejador global de excepciones para toda la API
 * 
 * @RestControllerAdvice: Intercepta todas las excepciones lanzadas por los
 *                        controllers
 *                        y las convierte en respuestas HTTP estandarizadas.
 * 
 *                        Ventajas:
 *                        - Centraliza el manejo de errores (no repetir código
 *                        en cada controller)
 *                        - Respuestas consistentes en toda la API
 *                        - Mensajes de error claros para el frontend
 *                        - Fácil debugging y logging
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja errores de validación de DTOs
     * 
     * Se activa cuando un DTO con @Valid falla las validaciones
     * (ej: @Size, @NotBlank, @Email, @Pattern, etc.)
     * 
     * Ejemplo:
     * POST /api/profiles con { "jobTitle": "AB" }
     * → 400 Bad Request con mensaje "El título debe tener entre 3 y 150 caracteres"
     * 
     * @param ex Excepción lanzada por Spring cuando falla @Valid
     * @return ResponseEntity con ErrorResponse (400 Bad Request)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {

        // Extraer todos los errores de validación
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            // Formato: "El título debe tener entre 3 y 150 caracteres"
            errors.add(error.getDefaultMessage());
        }

        // Construir respuesta de error
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Error de validación")
                .timestamp(LocalDateTime.now())
                .errors(errors)
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    /**
     * Maneja RuntimeException genéricos lanzados por los servicios
     * 
     * Ejemplos:
     * - "User not found" → 404 Not Found
     * - "Only users with PROGRAMMER role..." → 403 Forbidden
     * - "El email ya está registrado" → 400 Bad Request
     * 
     * @param ex RuntimeException lanzada por el servicio
     * @return ResponseEntity con ErrorResponse (código según el mensaje)
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {

        String message = ex.getMessage();
        HttpStatus status;

        // Determinar el código HTTP según el mensaje de error
        if (message.contains("not found") || message.contains("no encontrado")) {
            status = HttpStatus.NOT_FOUND; // 404
        } else if (message.contains("role") || message.contains("permission") ||
                message.contains("Only users with")) {
            status = HttpStatus.FORBIDDEN; // 403
        } else {
            status = HttpStatus.BAD_REQUEST; // 400
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(status.value())
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity
                .status(status)
                .body(errorResponse);
    }

    /**
     * Maneja cualquier otra excepción no capturada
     * 
     * Este es el "catch-all" para errores inesperados del servidor.
     * 
     * @param ex Cualquier excepción no manejada por los otros handlers
     * @return ResponseEntity con ErrorResponse (500 Internal Server Error)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Error interno del servidor: " + ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }
}
