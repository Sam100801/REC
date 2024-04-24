package it.unisalento.recproject.rewardservice.repositories;

import it.unisalento.recproject.rewardservice.domain.Reward;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RewardRepository extends MongoRepository<Reward, String> {
}
