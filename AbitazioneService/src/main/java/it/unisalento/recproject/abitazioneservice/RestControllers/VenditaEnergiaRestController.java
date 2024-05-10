package it.unisalento.recproject.abitazioneservice.RestControllers;

import it.unisalento.recproject.abitazioneservice.dto.AbitazioneDTO;
import it.unisalento.recproject.abitazioneservice.dto.EnergiaDTO;
import it.unisalento.recproject.abitazioneservice.dto.VenditaEnergiaDTO;
import it.unisalento.recproject.abitazioneservice.repositories.VenditaEnergiaRepository;
import it.unisalento.recproject.abitazioneservice.service.AbitazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vendita_energia")
public class VenditaEnergiaRestController {

    @Autowired
    VenditaEnergiaRepository venditaEnergiaRepository;

    @Autowired
    AbitazioneService abitazioneService;

    @RequestMapping(value = "/", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public VenditaEnergiaDTO acquisto(@RequestBody AbitazioneDTO abitazioneDTOVenditore, @RequestBody AbitazioneDTO abitazioneDTOConsumatore, @RequestBody EnergiaDTO energiaDTO) {

        return abitazioneService.scambioEnergia(abitazioneDTOVenditore, abitazioneDTOConsumatore, energiaDTO);
    }

}


