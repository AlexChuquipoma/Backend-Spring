package com.portfolio.backend.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para respuestas de error estandarizadas
 * 
 * Se usa en el GlobalExceptionHandler para retornar errores
 * con un formato consistente en toda la API.
 * 
 * Ejemplo de respuesta:
 * {
 * "status": 400,
 * "message": "Validation failed",
 * "timestamp": "2024-02-08T01:24:00",
 * "errors": [
 * "El título debe tener entre 3 y 150 caracteres",
 * "La biografía no puede superar 500 caracteres"
 * ]
 * }
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    /**
     * Código de estado HTTP (400, 404, 403, 500, etc.)
     */
    private int status;

    /**
     * Mensaje principal del error
     */
    private String message;

    /**
     * Timestamp del error en formato ISO 8601
     * Ejemplo: "2024-02-08T01:24:00"
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    /**
     * Lista de errores específicos (útil para validaciones)
     * Puede ser null si solo hay un error general
     */
    private List<String> errors;

    /**
     * Constructor simplificado para errores sin lista de detalles
     */
    public ErrorResponse(int status, String message, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
        this.errors = null;
    }
}
