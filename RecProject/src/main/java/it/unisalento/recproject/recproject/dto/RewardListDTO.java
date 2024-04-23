package it.unisalento.recproject.recproject.dto;

import java.util.ArrayList;


public class RewardListDTO {
    private ArrayList<RewardDTO> rewards;

    public RewardListDTO() {
        rewards = new ArrayList<>();
    }
    public void setRewards(ArrayList<RewardDTO> rewards) {
        this.rewards = rewards;
    }

    public ArrayList<RewardDTO> getRewards() {
        return rewards;
    }
}
