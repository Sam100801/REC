package it.unisalento.recproject.abitazioneservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class AbitazioneNotFoundException extends Exception{
    private String idAbitazione;

    public AbitazioneNotFoundException(String idAbitazione) {
        super("Abitazione non trovata con ID: " + idAbitazione);
        this.idAbitazione = idAbitazione;
    }
    public AbitazioneNotFoundException() {
    }

    public String getIdAbitazione() {
        return idAbitazione;
    }
}
