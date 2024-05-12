package it.unisalento.recproject.abitazioneservice.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class AbitazioneResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AbitazioneNotFoundException.class)
    protected ResponseEntity<Object> handleAbitazioneNotFoundException(
            AbitazioneNotFoundException ex, WebRequest request) {
        String errorMessage = "Abitazione non trovata con ID: " + ex.getIdAbitazione();
        return handleExceptionInternal(ex, errorMessage, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(EnergiaInsufficienteException.class)
    protected ResponseEntity<Object> handleEnergiaInsufficienteException(
            EnergiaInsufficienteException ex, WebRequest request) {
        String errorMessage = "L'energia richiesta è maggiore di quella disponibile " ;
        return handleExceptionInternal(ex, errorMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(VenditeNotFoudExceptions.class)
    protected ResponseEntity<Object> handleVenditeNotFoundException(
            VenditeNotFoudExceptions ex, WebRequest request) {
        String errorMessage = "L'utente " + ex.email + " non ha effettuato nessun acquisto in questo mese" ;
        return handleExceptionInternal(ex, errorMessage, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}
