package it.unisalento.recproject.produttoreservice.reporitories;


import it.unisalento.recproject.produttoreservice.domain.Produttore;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProduttoreRepository extends MongoRepository<Produttore, String> {

    Produttore findByEmail(String email);
    Produttore findByUsername(String email);
    boolean existsByEmail(String email);

}
