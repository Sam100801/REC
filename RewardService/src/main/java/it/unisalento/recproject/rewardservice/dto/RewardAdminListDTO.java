package it.unisalento.recproject.rewardservice.dto;

import java.util.ArrayList;

public class RewardAdminListDTO {
    private ArrayList<RewardAdminDTO> rewards;

    public RewardAdminListDTO() {
        rewards = new ArrayList<>();
    }
    public void setRewards(ArrayList<RewardAdminDTO> rewards) {
        this.rewards = rewards;
    }

    public ArrayList<RewardAdminDTO> getRewards() {
        return rewards;
    }
}
