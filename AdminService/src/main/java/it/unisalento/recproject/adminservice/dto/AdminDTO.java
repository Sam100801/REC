package it.unisalento.recproject.adminservice.dto;

public class AdminDTO {

    private String id;
    private String nome;
    private String cognome;
    private String email;
    private float incasso;
    private float saldo;

    public AdminDTO() {
    }

    public AdminDTO(String id, String nome, String cognome, String username, String email) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
    }

    public float getSaldo() {
        return saldo;
    }

    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }

    public float getIncasso() {
        return incasso;
    }

    public void setIncasso(float incasso) {
        this.incasso = incasso;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

