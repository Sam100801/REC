package it.unisalento.recproject.recproject.repositories;

import it.unisalento.recproject.recproject.domain.Reward;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RewardRepository extends MongoRepository<Reward, String>{

}
