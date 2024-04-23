package it.unisalento.recproject.recproject.repositories;

import it.unisalento.recproject.recproject.domain.Abitazione;
import it.unisalento.recproject.recproject.domain.Reward;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface AbitazioneRepository extends MongoRepository<Abitazione, String> {
    @Query("{'caratteristiche.cpu': ?0}")
    List<Abitazione> findByCaratteristicheCpu(float cpu);
    @Query("{'caratteristiche.ram': ?0}")
    List<Abitazione> findByCaratteristicheRam(int ram);
    @Query("{'caratteristiche.sistemaOperativo': ?0}")
    List<Abitazione> findByCaratteristicheSistemaOperativo(String sistemaOperativo);
}
