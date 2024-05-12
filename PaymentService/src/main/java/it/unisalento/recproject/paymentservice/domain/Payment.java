package it.unisalento.recproject.paymentservice.domain;


import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public class Payment {
    
    @Id
    private String idPayment;
    private String emailReceivePayment;
    private String emailSendPayment;
    private LocalDateTime datePayment;

    public String getIdPayment() {
        return idPayment;
    }

    public void setIdPayment(String idPayment) {
        this.idPayment = idPayment;
    }

    public String getEmailReceivePayment() {
        return emailReceivePayment;
    }

    public void setEmailReceivePayment(String emailReceivePayment) {
        this.emailReceivePayment = emailReceivePayment;
    }

    public String getEmailSendPayment() {
        return emailSendPayment;
    }

    public void setEmailSendPayment(String emailSendPayment) {
        this.emailSendPayment = emailSendPayment;
    }

    public LocalDateTime getDatePayment() {
        return datePayment;
    }

    public void setDatePayment(LocalDateTime datePayment) {
        this.datePayment = datePayment;
    }
}
