package it.unisalento.recproject.recproject.domain;

import it.unisalento.recproject.recproject.dto.CapacitaCalcoloDTO;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



@Document("abitazione")
public class Abitazione {

    @Id
    private String id;
    private CapacitaCalcoloDTO caratteristiche;
    private float ore;
    private float kwh;
    private String energiaGreen;
    private String proprietario;
    private float energiaVenduta;

    public float getEnergiaVenduta() {
        return energiaVenduta;
    }

    public void setEnergiaVenduta(float energiaVenduta) {
        this.energiaVenduta = energiaVenduta;
    }

    public Abitazione() {
    }

    public Abitazione(String  id, CapacitaCalcoloDTO caratteristiche, float ore, float kwh, String energiaGreen, String proprietario, float energia_venduta) {
        this.id = id;
        this.caratteristiche = caratteristiche;
        this.ore = ore;
        this.kwh = kwh;
        this.energiaGreen = energiaGreen;
        this.proprietario = proprietario;
        this.energiaVenduta = energia_venduta;
    }

    public CapacitaCalcoloDTO getCaratteristiche() {
        return caratteristiche;
    }

    public void setCaratteristiche(CapacitaCalcoloDTO caratteristiche) {
        this.caratteristiche = caratteristiche;
    }

    public float getOre() {
        return ore;
    }

    public void setOre(float ore) {
        this.ore = ore;
    }

    public float getKwh() {
        return kwh;
    }

    public void setKwh(float kwh) {
        this.kwh = kwh;
    }

    public String getEnergiaGreen() {
        return energiaGreen;
    }

    public void setEnergiaGreen(String energiaGreen) {
        this.energiaGreen = energiaGreen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProprietario() {
        return proprietario;
    }

    public void setProprietario(String proprietario) {
        this.proprietario = proprietario;
    }

}
