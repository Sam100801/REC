package it.unisalento.recproject.abitazioneservice.dto;

public class AbitazioneDTO {

    private String id;
    private CapacitaCalcoloDTO caratteristiche;
    private float ore;
    private float kwh;
    private String energiaGreen;
    private String proprietario;
    private float energiaVenduta;

    public AbitazioneDTO() {
    }

    public AbitazioneDTO(String  id, CapacitaCalcoloDTO caratteristiche, float ore, float kwh, String energiaGreen, String proprietario, float energia_venduta) {
        this.id = id;
        this.caratteristiche = caratteristiche;
        this.ore = ore;
        this.kwh = kwh;
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

