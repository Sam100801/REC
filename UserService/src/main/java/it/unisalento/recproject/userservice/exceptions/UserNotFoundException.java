package it.unisalento.recproject.userservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends Exception{


    private String email;

    public UserNotFoundException(String email) {
        super("Utente con email: " + email + " non trovato");
        this.email = email;
    }

    public UserNotFoundException() {
    }

    public String getEmail() {
        return email;
    }
}
