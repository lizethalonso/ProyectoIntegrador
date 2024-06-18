package Gomez_Alonso.ClinicaOdontologica.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptions {
    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<String>tratamientoResourceNotFoundException(ResourceNotFoundException rnfex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mensaje: " + rnfex.getMessage());
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<String> tratamientoBadRequestException(BadRequestException brex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mensaje: " + brex.getMessage());
    }

    @ExceptionHandler({NoContentException.class})
    public ResponseEntity<String> tratamientoNoContentException(NoContentException ncex) {
        return ResponseEntity.status((HttpStatus.NO_CONTENT)).body("Mensaje: " + ncex.getMessage());
    }

}
