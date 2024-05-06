package it.unisalento.recproject.rewardservice.repositories;

import it.unisalento.recproject.rewardservice.domain.RewardAdmin;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RewardRepository extends MongoRepository<RewardAdmin, String> {
}
