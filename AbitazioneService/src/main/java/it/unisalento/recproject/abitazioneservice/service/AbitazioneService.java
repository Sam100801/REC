package it.unisalento.recproject.abitazioneservice.service;


import it.unisalento.recproject.abitazioneservice.domain.Abitazione;
import it.unisalento.recproject.abitazioneservice.domain.VenditaEnergia;
import it.unisalento.recproject.abitazioneservice.dto.AbitazioneDTO;
import it.unisalento.recproject.abitazioneservice.dto.EnergiaDTO;
import it.unisalento.recproject.abitazioneservice.dto.VenditaEnergiaDTO;
import it.unisalento.recproject.abitazioneservice.repositories.AbitazioneRepository;
import it.unisalento.recproject.abitazioneservice.repositories.VenditaEnergiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class AbitazioneService {

    @Autowired
    AbitazioneRepository abitazioneRepository;

    @Autowired
    VenditaEnergiaRepository venditaEnergiaRepository;

    public VenditaEnergiaDTO scambioEnergia(AbitazioneDTO abitazioneDTOVenditore, AbitazioneDTO abitazioneDTOConsumatore, EnergiaDTO energiaDTO){
        Optional<Abitazione> abitazioneVenditore = abitazioneRepository.findById(abitazioneDTOVenditore.getId());
        Optional<Abitazione> abitazioneConsumatore = abitazioneRepository.findById(abitazioneDTOConsumatore.getId());

        if (abitazioneVenditore.isEmpty() ) {
            throw new RuntimeException("Abitazione non trovata con ID: " + abitazioneDTOVenditore.getId());
        }
        else if (abitazioneConsumatore.isEmpty()){
            throw new RuntimeException("Abitazione non trovata con ID: " + abitazioneDTOVenditore.getId());
        }

        if (abitazioneVenditore.get().getKw() >= energiaDTO.getKw() && abitazioneVenditore.get().getOre() >= energiaDTO.getOre()){
            // Scalare energia da venditore:
            abitazioneVenditore.get().setKw((abitazioneVenditore.get().getKw() - energiaDTO.getKw()));
            abitazioneVenditore.get().setOre((abitazioneVenditore.get().getOre() - energiaDTO.getOre()));
            // Incrementare il valore dell'attributo energiaVenduta per tenere traccia dell'energia totale venduta da parte dell'abitazione
            abitazioneVenditore.get().setEnergiaVenduta((abitazioneVenditore.get().getEnergiaVenduta() + (energiaDTO.getKw()) * energiaDTO.getOre()));
        }
        else
            throw new RuntimeException("L'energia richiesta è superiore rispetto a quella che l'abitazione ha");

        // Incrementare energia consumatore:
        abitazioneConsumatore.get().setKw((abitazioneConsumatore.get().getKw() + energiaDTO.getKw()));
        abitazioneConsumatore.get().setOre((abitazioneConsumatore.get().getOre() + energiaDTO.getOre()));

        // Salvo le modifiche nel database:
        abitazioneRepository.save(abitazioneConsumatore.get());
        abitazioneRepository.save(abitazioneVenditore.get());

        return venditaEnergia(abitazioneDTOVenditore,abitazioneDTOConsumatore,energiaDTO);
    }

    public VenditaEnergiaDTO venditaEnergia(AbitazioneDTO abitazioneDTOVenditore, AbitazioneDTO abitazioneDTOConsumatore, EnergiaDTO energiaDTO){

        VenditaEnergiaDTO venditaEnergiaDTO = new VenditaEnergiaDTO();

        // Venditore di energia:
        venditaEnergiaDTO.setViaVenditore(abitazioneDTOVenditore.getViaENumeroCivico());
        venditaEnergiaDTO.setEmailVenditore(abitazioneDTOVenditore.getProprietario());

        // Compratore di energia:
        venditaEnergiaDTO.setViaAcquirente(abitazioneDTOConsumatore.getViaENumeroCivico());
        venditaEnergiaDTO.setEmailAcquirente(abitazioneDTOConsumatore.getProprietario());

        venditaEnergiaDTO.setDataAcquisto();

        venditaEnergiaDTO.setEnergiaAcquistata(energiaDTO.getKw() * energiaDTO.getOre());
        // Energia acquistata * 0,3 poichè supponiamo che un kwh costi 0,30$
        venditaEnergiaDTO.setCostoEnergiaAcquistata((float) (energiaDTO.getKw() * energiaDTO.getOre() * 0.3));

        // Andiamo a salvare nel database:
        VenditaEnergia venditaEnergia = new VenditaEnergia();

        venditaEnergia.setViaVenditore(venditaEnergiaDTO.getViaVenditore());
        venditaEnergia.setEmailVenditore(venditaEnergiaDTO.getEmailVenditore());
        venditaEnergia.setViaAcquirente(venditaEnergiaDTO.getViaAcquirente());
        venditaEnergia.setEmailAcquirente(venditaEnergiaDTO.getEmailAcquirente());
        venditaEnergia.setDataAcquisto(venditaEnergiaDTO.getDataAcquisto());
        venditaEnergia.setEnergiaAcquistata(venditaEnergia.getEnergiaAcquistata());
        venditaEnergia.setCostoEnergiaAcquistata(venditaEnergia.getCostoEnergiaAcquistata());

        venditaEnergiaRepository.save(venditaEnergia);

        return venditaEnergiaDTO;
    }

}
