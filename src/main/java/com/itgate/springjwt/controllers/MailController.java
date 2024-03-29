package com.itgate.springjwt.controllers;

import com.itgate.springjwt.models.Mail;
import com.itgate.springjwt.utils.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/email")
@CrossOrigin("*")
public class MailController {
    @Autowired
    private EmailService emailService ;

    @PostMapping
    public String sendMail() {
        System.out.println("spring mail-sendding mail exemple");
        Mail mail=new Mail();
        mail.setFrom("@gmail.com");
        mail.setTo("@gmail.com");
        mail.setSubject("subject simple email with javamailsender");
        mail.setContent("poopop");
        emailService.sendSimpleMessage(mail);
        return "ok";


    }

}
