package it.unisalento.recproject.orderservice.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {
    private String id;
    private String userEmail;
    private int quantita;
    private double costo;
    private List<RewardDTO> rewards;
    private LocalDateTime localDateTime;

    public OrderDTO() {
    }

    public OrderDTO(String id, String userEmail, int quantita, double costo, List<RewardDTO> rewards) {
        this.id = id;
        this.userEmail = userEmail;
        this.quantita = quantita;
        this.costo = costo;
        this.rewards = rewards;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public List<RewardDTO> getRewards() {
        return rewards;
    }

    public void setRewards(List<RewardDTO> rewards) {
        this.rewards = rewards;
    }
}

