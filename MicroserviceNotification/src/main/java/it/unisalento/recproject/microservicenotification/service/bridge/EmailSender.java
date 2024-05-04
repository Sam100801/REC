package it.unisalento.recproject.microservicenotification.service.bridge;

import org.springframework.web.multipart.MultipartFile;

public interface EmailSender {
    void sendMail(MultipartFile[] files, String to, String[] cc, String subject, String body);
}
