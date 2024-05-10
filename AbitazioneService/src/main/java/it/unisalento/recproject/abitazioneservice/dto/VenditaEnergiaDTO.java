package it.unisalento.recproject.abitazioneservice.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class VenditaEnergiaDTO {
    private String id;
    private String viaVenditore;
    private String emailVenditore;
    private String viaAcquirente;
    private String emailAcquirente;
    private LocalDateTime dataAcquisto;

    // Moltiplicazione tra ore e kw
    private float energiaAcquistata;

    // Supponiamo che ogni kwh costa 0,30 $
    private float costoEnergiaAcquistata;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getViaVenditore() {
        return viaVenditore;
    }

    public void setViaVenditore(String viaVenditore) {
        this.viaVenditore = viaVenditore;
    }

    public String getEmailVenditore() {
        return emailVenditore;
    }

    public void setEmailVenditore(String emailVenditore) {
        this.emailVenditore = emailVenditore;
    }

    public String getViaAcquirente() {
        return viaAcquirente;
    }

    public void setViaAcquirente(String viaAcquirente) {
        this.viaAcquirente = viaAcquirente;
    }

    public String getEmailAcquirente() {
        return emailAcquirente;
    }

    public void setEmailAcquirente(String emailAcquirente) {
        this.emailAcquirente = emailAcquirente;
    }

    public LocalDateTime getDataAcquisto() {
        return dataAcquisto;
    }

    public void setDataAcquisto() {
        this.dataAcquisto = LocalDateTime.from(LocalDate.now());
    }

    public float getEnergiaAcquistata() {
        return energiaAcquistata;
    }

    public void setEnergiaAcquistata(float energiaAcquistata) {
        this.energiaAcquistata = energiaAcquistata;
    }

    public float getCostoEnergiaAcquistata() {
        return costoEnergiaAcquistata;
    }

    public void setCostoEnergiaAcquistata(float costoEnergiaAcquistata) {
        this.costoEnergiaAcquistata = costoEnergiaAcquistata;
    }
}
