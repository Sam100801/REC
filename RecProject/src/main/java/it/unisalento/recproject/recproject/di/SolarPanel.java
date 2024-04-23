package it.unisalento.recproject.recproject.di;

import org.springframework.stereotype.Component;

@Component //classi devono essere dei component
public class SolarPanel implements IRenewableSource{
    @Override
    public void initialize() {
        System.out.println("Ciao sono un pannelo solare");
    }
}
