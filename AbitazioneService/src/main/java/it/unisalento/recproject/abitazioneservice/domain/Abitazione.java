package it.unisalento.recproject.abitazioneservice.domain;


import it.unisalento.recproject.abitazioneservice.dto.CapacitaCalcoloDTO;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("abitazione")
public class Abitazione {

    @Id
    private String id;
    private CapacitaCalcoloDTO caratteristiche;
    private float ore;
    private float kw;
    private String energiaGreen;
    private String proprietario;
    private float energiaVenduta;
    private String viaENumeroCivico;

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
        this.kw = kwh;
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

    public float getKw() {
        return kw;
    }

    public void setKw(float kw) {
        this.kw = kw;
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

    public String getViaENumeroCivico() {
        return viaENumeroCivico;
    }

    public void setViaENumeroCivico(String viaENumeroCivico) {
        this.viaENumeroCivico = viaENumeroCivico;
    }
}
