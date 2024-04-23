package it.unisalento.recproject.recproject.repositories;

import it.unisalento.recproject.recproject.domain.Amministratore;
import it.unisalento.recproject.recproject.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AmministratoreRepository extends MongoRepository<Amministratore, String>{
    Amministratore findByUsername(String username);
    Amministratore findByEmail(String email);
    boolean existsByEmail(String email);

}
