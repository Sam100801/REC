package it.unisalento.recproject.recproject.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import it.unisalento.recproject.recproject.domain.Produttore;

public interface ProduttoreRepository extends MongoRepository<Produttore, String> {

    Produttore findByEmail(String email);
    Produttore findByUsername(String email);
    boolean existsByEmail(String email);

}
