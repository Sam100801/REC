package it.unisalento.recproject.abitazioneservice.service;


import it.unisalento.recproject.abitazioneservice.domain.Abitazione;
import it.unisalento.recproject.abitazioneservice.domain.VenditaEnergia;
import it.unisalento.recproject.abitazioneservice.dto.AbitazioneDTO;
import it.unisalento.recproject.abitazioneservice.dto.EnergiaDTO;
import it.unisalento.recproject.abitazioneservice.dto.VenditaEnergiaDTO;
import it.unisalento.recproject.abitazioneservice.dto.VenditaEnergiaListDTO;
import it.unisalento.recproject.abitazioneservice.exceptions.AbitazioneNotFoundException;
import it.unisalento.recproject.abitazioneservice.exceptions.EnergiaInsufficienteException;
import it.unisalento.recproject.abitazioneservice.repositories.AbitazioneRepository;
import it.unisalento.recproject.abitazioneservice.repositories.VenditaEnergiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AbitazioneService {

    @Autowired
    AbitazioneRepository abitazioneRepository;

    @Autowired
    VenditaEnergiaRepository venditaEnergiaRepository;

    public VenditaEnergiaDTO scambioEnergia(EnergiaDTO energiaDTO) throws AbitazioneNotFoundException, EnergiaInsufficienteException {
        Optional<Abitazione> abitazioneVenditore = abitazioneRepository.findById(energiaDTO.getIdAbitazioneVenditore());
        Optional<Abitazione> abitazioneConsumatore = abitazioneRepository.findById(energiaDTO.getIdAbitazioneConsumatore());

        if (abitazioneVenditore.isEmpty() ) {
            throw new AbitazioneNotFoundException(energiaDTO.getIdAbitazioneVenditore());
        }
        else if (abitazioneConsumatore.isEmpty()){
            throw new AbitazioneNotFoundException(energiaDTO.getIdAbitazioneConsumatore());
        }

        if (abitazioneVenditore.get().getKw() >= energiaDTO.getKw() && abitazioneVenditore.get().getOre() >= energiaDTO.getOre()){
            // Scalare energia da venditore:
            abitazioneVenditore.get().setKw((abitazioneVenditore.get().getKw() - energiaDTO.getKw()));
            abitazioneVenditore.get().setOre((abitazioneVenditore.get().getOre() - energiaDTO.getOre()));
            // Incrementare il valore dell'attributo energiaVenduta per tenere traccia dell'energia totale venduta da parte dell'abitazione
            abitazioneVenditore.get().setEnergiaVenduta((abitazioneVenditore.get().getEnergiaVenduta() + (energiaDTO.getKw()) * energiaDTO.getOre()));
        }
        else
            throw new EnergiaInsufficienteException();

        // Incrementare energia consumatore:
        abitazioneConsumatore.get().setKw((abitazioneConsumatore.get().getKw() + energiaDTO.getKw()));
        abitazioneConsumatore.get().setOre((abitazioneConsumatore.get().getOre() + energiaDTO.getOre()));

        // Salvo le modifiche nel database:
        abitazioneRepository.save(abitazioneConsumatore.get());
        abitazioneRepository.save(abitazioneVenditore.get());

        return venditaEnergia(abitazioneVenditore, abitazioneConsumatore, energiaDTO);
    }

    public VenditaEnergiaDTO venditaEnergia(Optional<Abitazione> abitazioneVenditore, Optional<Abitazione> abitazioneConsumatore, EnergiaDTO energiaDTO){

        VenditaEnergiaDTO venditaEnergiaDTO = new VenditaEnergiaDTO();

        // Venditore di energia:
        venditaEnergiaDTO.setViaVenditore(abitazioneVenditore.get().getViaENumeroCivico());
        venditaEnergiaDTO.setEmailVenditore(abitazioneVenditore.get().getProprietario());

        // Compratore di energia:
        venditaEnergiaDTO.setViaAcquirente(abitazioneConsumatore.get().getViaENumeroCivico());
        venditaEnergiaDTO.setEmailAcquirente(abitazioneConsumatore.get().getProprietario());

        venditaEnergiaDTO.setDataAcquisto();

        venditaEnergiaDTO.setEnergiaAcquistata((energiaDTO.getKw() * energiaDTO.getOre()));
        // Energia acquistata * 0,3 poichè supponiamo che un kwh costi 0,30$
        venditaEnergiaDTO.setCostoEnergiaAcquistata((float) (energiaDTO.getKw() * energiaDTO.getOre() * 0.3));

        // Andiamo a salvare nel database:
        VenditaEnergia venditaEnergia = new VenditaEnergia();

        venditaEnergia.setViaVenditore(venditaEnergiaDTO.getViaVenditore());
        venditaEnergia.setEmailVenditore(venditaEnergiaDTO.getEmailVenditore());
        venditaEnergia.setViaAcquirente(venditaEnergiaDTO.getViaAcquirente());
        venditaEnergia.setEmailAcquirente(venditaEnergiaDTO.getEmailAcquirente());
        venditaEnergia.setDataAcquisto(venditaEnergiaDTO.getDataAcquisto());
        venditaEnergia.setEnergiaAcquistata(venditaEnergiaDTO.getEnergiaAcquistata());
        venditaEnergia.setCostoEnergiaAcquistata(venditaEnergiaDTO.getCostoEnergiaAcquistata());

        venditaEnergiaRepository.save(venditaEnergia);

        assegnaCrediti(venditaEnergiaDTO.getEnergiaAcquistata(), venditaEnergiaDTO.getEmailVenditore());

        return venditaEnergiaDTO;
    }

    public void assegnaCrediti(float energiaAcquistata, String email){

        // Suppponiamo che ogni kwh equivalga a mezzo credito
        float crediti = (float) (energiaAcquistata * 0.50);

        String url = "http://host.docker.internal:8088/api/users/addCrediti/"+email+"?crediti="+crediti;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Void> response1 = restTemplate.exchange(url, HttpMethod.PUT, null, Void.class);

    }

}
