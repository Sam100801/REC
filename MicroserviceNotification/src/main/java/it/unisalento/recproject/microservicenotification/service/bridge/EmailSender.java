package it.unisalento.recproject.microservicenotification.service.bridge;

import jakarta.mail.MessagingException;
import org.springframework.web.multipart.MultipartFile;

public interface EmailSender {
    void sendMail(MultipartFile[] files, String to, String[] cc, String subject, String body);

    void sendOtpEmail(String email, String otp) throws MessagingException;
}
