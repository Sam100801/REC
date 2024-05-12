package it.unisalento.recproject.adminservice.service.pdf;

import java.util.Objects;
import java.util.Optional;

public class RewardDTO {
    private Optional<String> pname;
    private String ImageUrl;
    private int quantity;
    private double costo;

    public RewardDTO(String imageUrl, String nome, int quantita, double costo) {
        this.pname = Optional.ofNullable(nome);
        this.ImageUrl = imageUrl;
        this.quantity = quantita;
        this.costo = costo;
    }


    public void setPname(Optional<String> pname) {
        this.pname = pname;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public Optional<String> getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = Optional.ofNullable(pname);
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RewardDTO)) return false;
        RewardDTO product = (RewardDTO) o;
        return Objects.equals(pname, product.pname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pname);
    }

    @Override
    public String toString() {
        return "Reward{" +
                "pname=" + pname +
                ", ImageUrl='" + ImageUrl + '\'' +
                ", quantity=" + quantity +
                ", costo=" + costo +
                '}';
    }
}
