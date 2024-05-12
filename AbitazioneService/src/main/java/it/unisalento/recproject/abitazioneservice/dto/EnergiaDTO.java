package it.unisalento.recproject.abitazioneservice.dto;

public class EnergiaDTO {
    private float kw;
    private float ore;
    private String idAbitazioneVenditore;
    private String idAbitazioneConsumatore;

    public float getKw() {
        return kw;
    }

    public void setKw(float kw) {
        this.kw = kw;
    }

    public float getOre() {
        return ore;
    }

    public void setOre(float ore) {
        this.ore = ore;
    }

    public String getIdAbitazioneVenditore() {
        return idAbitazioneVenditore;
    }

    public void setIdAbitazioneVenditore(String idAbitazioneVenditore) {
        this.idAbitazioneVenditore = idAbitazioneVenditore;
    }

    public String getIdAbitazioneConsumatore() {
        return idAbitazioneConsumatore;
    }

    public void setIdAbitazioneConsumatore(String idAbitazioneConsumatore) {
        this.idAbitazioneConsumatore = idAbitazioneConsumatore;
    }
}
