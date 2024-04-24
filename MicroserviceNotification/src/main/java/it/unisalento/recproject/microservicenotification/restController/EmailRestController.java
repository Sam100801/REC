package it.unisalento.recproject.microservicenotification.restController;

import it.unisalento.recproject.microservicenotification.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/email")
public class EmailRestController {

    @Autowired
    private EmailService emailService;

    @RequestMapping(value="/", method = RequestMethod.POST)
    public String sendMail(@RequestParam(value = "file", required = false) MultipartFile[] file, String to, String[] cc, String subject, String body) {
        return emailService.sendMail(file, to, cc, subject, body);
    }
}
