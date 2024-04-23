package it.unisalento.recproject.recproject.di;

import org.springframework.stereotype.Component;

@Component
public class WindTurbine implements IRenewableSource{
    @Override
    public void initialize() {
        System.out.println("Ciao sono una turbina eolica");
    }
}
