package es.hectorplaza.api1;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value = "/name", produces = "text/plain;charset=UTF-8")
public class FirstController {

    // Spring comparte el controlador entre peticiones. synchronized protege el nombre guardado.
    private String nameStored;
    private final HttpServletResponse response;

    public FirstController(HttpServletResponse response) {
        this.response = response;
    }

    @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public synchronized void crearRecurso(@RequestBody(required = false) String name) {
        if (name == null || name.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre es obligatorio.");
        }
        if (nameStored != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya hay un nombre guardado.");
        }

        nameStored = name.strip();
        response.setHeader(HttpHeaders.LOCATION, "/name");
    }

    @GetMapping
    public synchronized String obtenerRecurso() {
        if (nameStored == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No hay ningún nombre guardado.");
        }
        return "Hola " + nameStored;
    }

    @PutMapping(consumes = MediaType.TEXT_PLAIN_VALUE)
    public synchronized String actualizarRecurso(@RequestBody(required = false) String name) {
        if (name == null || name.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre es obligatorio.");
        }
        if (nameStored == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No hay ningún nombre guardado.");
        }

        nameStored = name.strip();
        return "Hola " + nameStored;
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public synchronized void borrarRecurso(@RequestParam(name = "name", required = false) String name) {
        if (nameStored == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No hay ningún nombre guardado.");
        }

        nameStored = null;
    }
}
