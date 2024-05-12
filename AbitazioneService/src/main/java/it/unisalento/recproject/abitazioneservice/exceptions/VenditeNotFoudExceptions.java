package it.unisalento.recproject.abitazioneservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class VenditeNotFoudExceptions extends Exception{
    String email;
    public VenditeNotFoudExceptions(String email) {
        super("Abitazione non trovata con ID: " + email);
        this.email = email;
    }
}
