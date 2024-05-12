package it.unisalento.recproject.abitazioneservice.dto;

import it.unisalento.recproject.abitazioneservice.domain.VenditaEnergia;

import java.util.ArrayList;

public class VenditaEnergiaListDTO {
    private ArrayList<VenditaEnergiaDTO> list;

    public ArrayList<VenditaEnergiaDTO> getList() {
        return list;
    }

    public void setList(ArrayList<VenditaEnergiaDTO> list) {
        this.list = list;
    }
}
