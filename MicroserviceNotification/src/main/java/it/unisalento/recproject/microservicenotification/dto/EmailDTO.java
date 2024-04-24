package it.unisalento.recproject.microservicenotification.dto;

import jakarta.mail.Multipart;
import org.springframework.web.multipart.MultipartFile;

public class EmailDTO {
    private MultipartFile[] file;
    private String destinatario;
    private String[] cc;
    private String subject;
    private String body;

    public MultipartFile[] getFile() {
        return file;
    }

    public void setFile(MultipartFile[] file) {
        this.file = file;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String[] getCc() {
        return cc;
    }

    public void setCc(String[] cc) {
        this.cc = cc;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
