package it.unisalento.recproject.abitazioneservice.repositories;

import it.unisalento.recproject.abitazioneservice.domain.Abitazione;
import it.unisalento.recproject.abitazioneservice.domain.VenditaEnergia;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface VenditaEnergiaRepository extends MongoRepository<VenditaEnergia, String>{


}
