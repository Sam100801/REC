package it.unisalento.recproject.userservice.repositories;

import it.unisalento.recproject.userservice.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
