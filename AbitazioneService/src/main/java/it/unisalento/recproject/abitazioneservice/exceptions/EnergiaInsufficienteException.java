package it.unisalento.recproject.abitazioneservice.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EnergiaInsufficienteException extends RuntimeException {

}
