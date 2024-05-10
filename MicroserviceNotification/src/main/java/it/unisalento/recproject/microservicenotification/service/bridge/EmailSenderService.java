package it.unisalento.recproject.microservicenotification.service.bridge;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EmailSenderService implements EmailService {

    @Autowired
    private EmailSender emailSender;


    @Override
    public String sendMail(MultipartFile[] files, String to, String[] cc, String subject, String body) {
        emailSender.sendMail(files, to, cc, subject, body);
        return "Mail sent successfully";
    }

    @Override
    public void sendOtpEmail(String email, String otp) throws MessagingException {
        emailSender.sendOtpEmail(email,otp);
    }
}
