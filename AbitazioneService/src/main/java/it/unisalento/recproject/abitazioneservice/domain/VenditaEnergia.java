package it.unisalento.recproject.abitazioneservice.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("vendita_energia")
public class VenditaEnergia {

    @Id
    private String id;
    private String viaVenditore;
    private String emailVenditore;
    private String viaAcquirente;
    private String emailAcquirente;
    private LocalDateTime dataAcquisto;
    private float energiaAcquistata;
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

    public void setDataAcquisto(LocalDateTime dataAcquisto) {
        this.dataAcquisto = dataAcquisto;
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
