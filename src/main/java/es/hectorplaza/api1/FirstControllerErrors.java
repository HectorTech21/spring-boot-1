package es.hectorplaza.api1;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice(assignableTypes = FirstController.class)
class FirstControllerErrors {

    @ExceptionHandler(ResponseStatusException.class)
    ResponseEntity<String> responderError(ResponseStatusException error) {
        return ResponseEntity.status(error.getStatusCode())
                .contentType(MediaType.parseMediaType("text/plain;charset=UTF-8"))
                .body(error.getReason());
    }
}
