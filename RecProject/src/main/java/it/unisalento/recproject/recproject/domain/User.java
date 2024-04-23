package it.unisalento.recproject.recproject.domain;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
/*
Ricordiamo che DTO è la classe che si interfaccia all'utente dunque contiene solo una parte di dati
solo quelli che si vogliono far uscire.
Le classi di domain invece sono diverse, e andranno a contenere tutti i dati.
 */


@Document("user")      // Per dire che ci stiamo collegando a MongoDB.
public class User {

    @Id
    private String id;
    private String nome;
    private String cognome;
    private String email;
    private String username;
    private String password;
    private float saldo;
    private float crediti;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public float getCrediti() {
        return crediti;
    }

    public void setCrediti(float crediti) {
        this.crediti = crediti;
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

    public float getSaldo() {
        return saldo;
    }

    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + "..." + '\'' +
                '}';
    }
}
