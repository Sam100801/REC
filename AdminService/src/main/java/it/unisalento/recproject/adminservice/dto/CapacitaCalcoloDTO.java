package it.unisalento.recproject.adminservice.dto;

public class CapacitaCalcoloDTO {
    private float cpu; //forse deve essere fatta una classe per le caratteristiche del processore
    private int ram;
    private int storage;
    private String sistemaOperativo;

    public CapacitaCalcoloDTO() {
    }

    public CapacitaCalcoloDTO(float cpu, int ram, int storage, String sistemaOperativo) {
        this.cpu = cpu;
        this.ram = ram;
        this.storage = storage;
        this.sistemaOperativo = sistemaOperativo;
    }

    public float getCpu() {
        return cpu;
    }

    public void setCpu(float cpu) {
        this.cpu = cpu;
    }

    public float getRam() {
        return ram;
    }

    public void setRam(int ram) {
        this.ram = ram;
    }

    public float getStorage() {
        return storage;
    }

    public void setStorage(int storage) {
        this.storage = storage;
    }

    public String getSistemaOperativo() {
        return sistemaOperativo;
    }

    public void setSistemaOperativo(String sistemaOperativo) {
        this.sistemaOperativo = sistemaOperativo;
    }

}
