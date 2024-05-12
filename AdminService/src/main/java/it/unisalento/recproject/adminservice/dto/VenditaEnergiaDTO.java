package it.unisalento.recproject.adminservice.dto;

import java.time.LocalDateTime;

public class VenditaEnergiaDTO {
    private String id;
    private String viaVenditore;
    private String emailVenditore;
    private String viaAcquirente;
    private String emailAcquirente;
    private LocalDateTime dataAcquisto;

    public VenditaEnergiaDTO() {
    }

    public VenditaEnergiaDTO(LocalDateTime dataAcquisto, float energiaAcquistata, float costoEnergiaAcquistata) {
        this.dataAcquisto = dataAcquisto;
        this.energiaAcquistata = energiaAcquistata;
        this.costoEnergiaAcquistata = costoEnergiaAcquistata;
    }

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
        this.dataAcquisto = LocalDateTime.now();
    }

    public void setDataAcquisto1(LocalDateTime dateTime) {
        this.dataAcquisto = dateTime;
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
