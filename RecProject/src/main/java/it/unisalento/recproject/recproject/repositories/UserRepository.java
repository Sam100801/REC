package it.unisalento.recproject.recproject.repositories;

import it.unisalento.recproject.recproject.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String > {
    User findByEmail(String email);
    boolean existsByEmail(String email);

}
