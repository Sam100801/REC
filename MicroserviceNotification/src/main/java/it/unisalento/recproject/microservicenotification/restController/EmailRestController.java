package it.unisalento.recproject.microservicenotification.restController;

import it.unisalento.recproject.microservicenotification.security.AuthorizationAPI;
import it.unisalento.recproject.microservicenotification.security.JwtUtilities;
import it.unisalento.recproject.microservicenotification.service.bridge.EmailService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/email")
public class EmailRestController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtUtilities jwtUtilities;


    @RequestMapping(value="/", method = RequestMethod.POST)
    public String sendMail(@RequestParam(value = "file", required = false) MultipartFile[] file, String to, String[] cc, String subject, String body, HttpServletRequest request) {
        AuthorizationAPI authorizationAPI = new AuthorizationAPI();
        authorizationAPI.checkAuthorizationRole(request, jwtUtilities);

        return emailService.sendMail(file, to, cc, subject, body);
    }

    @RequestMapping(value="/opt", method = RequestMethod.POST)
    public void sendOptMail(@RequestParam String email, String opt) throws MessagingException {
        emailService.sendOtpEmail(email,opt);
    }
}
