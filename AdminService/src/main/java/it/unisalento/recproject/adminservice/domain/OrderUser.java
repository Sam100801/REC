package it.unisalento.recproject.adminservice.domain;

import it.unisalento.recproject.adminservice.dto.RewardDTO;

import java.time.LocalDateTime;
import java.util.List;


public class OrderUser {
    private String id;
    private String userEmail;
    private double costoCrediti;
    private List<RewardDTO> rewards;
    private LocalDateTime localDateTime;

    public OrderUser() {
    }

    public OrderUser(String id, String userEmail, double costo, List<RewardDTO> rewards) {
        this.id = id;
        this.userEmail = userEmail;
        this.costoCrediti = costo;
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

    public double getCostoCrediti() {
        return costoCrediti;
    }

    public void setCostoCrediti(double costoCrediti) {
        this.costoCrediti = costoCrediti;
    }

    public List<RewardDTO> getRewards() {
        return rewards;
    }

    public void setRewards(List<RewardDTO> rewards) {
        this.rewards = rewards;
    }
}
