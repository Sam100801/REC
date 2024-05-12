package it.unisalento.recproject.orderservice.dto;

public class RewardDTO {
    private String id;
    private String nome;
    private String descrizione;
    private String imageUrl;
    private int quantita;
    private double costo;

    public RewardDTO() {
    }

    public RewardDTO(String id, String nome, String descrizione, String imageUrl, int quantita, double costo) {
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
        this.imageUrl = imageUrl;
        this.quantita = quantita;
        this.costo = costo;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
}

