package it.unisalento.recproject.recproject.repositories;

import it.unisalento.recproject.recproject.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String > {
    List<User> findByCognome(String cognome);
    List<User> findByNomeAndCognome(String nome, String cognome);
    User findByEmail(String email);
    User findByUsername(String username);
    boolean existsByEmail(String email);
}
