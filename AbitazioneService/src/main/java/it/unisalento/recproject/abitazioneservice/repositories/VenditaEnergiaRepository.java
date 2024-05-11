package it.unisalento.recproject.abitazioneservice.repositories;

import it.unisalento.recproject.abitazioneservice.domain.VenditaEnergia;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;


public interface VenditaEnergiaRepository extends MongoRepository<VenditaEnergia, String>{
    List<VenditaEnergia> findByEmailAcquirente(String emailAcquirente);
    List<VenditaEnergia> findByDataAcquistoIsAfterAndAndEmailAcquirente(LocalDateTime dateTime, String emailAcquirente);

}
