package it.unisalento.recproject.adminservice.dto;

public class AbitazioneDTO {

    private String id;
    private CapacitaCalcoloDTO caratteristiche;
    private float ore;
    private float kw;
    private String energiaGreen;
    private String proprietario;
    private float energiaVenduta;
    private String viaENumeroCivico;

    public AbitazioneDTO() {
    }

    public AbitazioneDTO(String  id, CapacitaCalcoloDTO caratteristiche, float ore, float kw, String energiaGreen, String proprietario, float energia_venduta) {
        this.id = id;
        this.caratteristiche = caratteristiche;
        this.ore = ore;
        this.kw = kw;
        this.energiaGreen = energiaGreen;
        this.proprietario = proprietario;
        this.energiaVenduta = energia_venduta;
    }

    public float getEnergiaVenduta() {
        return energiaVenduta;
    }

    public void setEnergiaVenduta(float energiaVenduta) {
        this.energiaVenduta = energiaVenduta;
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
