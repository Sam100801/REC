package it.unisalento.recproject.rewardservice.dto;

import java.util.ArrayList;

public class RewardUserListDTO {
    private ArrayList<RewardUserDTO> rewards;

    public RewardUserListDTO() {
        rewards = new ArrayList<>();
    }
    public void setRewards(ArrayList<RewardUserDTO> rewards) {
        this.rewards = rewards;
    }

    public ArrayList<RewardUserDTO> getRewards() {
        return rewards;
    }
}
