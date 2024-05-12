package it.unisalento.recproject.orderservice.domain;

import it.unisalento.recproject.orderservice.dto.RewardDTO;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "orderAdmin")
public class OrderAdmin {
    @Id
    private String id;
    private String userEmail;
    private double costo;
    private List<RewardDTO> rewards;
    private LocalDateTime localDateTime;

    public OrderAdmin() {
    }

    public OrderAdmin(String id, String userEmail, double costo, List<RewardDTO> rewards) {
        this.id = id;
        this.userEmail = userEmail;
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

