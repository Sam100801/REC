package it.unisalento.recproject.microservicenotification.service.bridge;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@Component
public class JavaMailEmailSender implements EmailSender {
    private final String fromEmail;
    private final JavaMailSender javaMailSender;

    public JavaMailEmailSender(@Value("${spring.mail.username}")String fromEmail, JavaMailSender javaMailSender) {
        this.fromEmail = fromEmail;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendMail(MultipartFile[] files, String to, String[] cc, String subject, String body) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setCc(cc);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(body);

            for (MultipartFile file : files) {
                mimeMessageHelper.addAttachment(file.getOriginalFilename(), new ByteArrayResource(file.getBytes()));
            }

            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendOtpEmail(String email, String otp) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Verify OTP");
        mimeMessageHelper.setText("""
        <div>
          <a href="http://localhost:8080/api/registrazione/verify-account?email=%s&otp=%s" target="_blank">click link to verify</a>
        </div>
        """.formatted(email, otp), true);

        javaMailSender.send(mimeMessage);
    }
}
