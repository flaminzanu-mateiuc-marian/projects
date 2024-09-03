package com.freightbroker.notifications_service.controller;

import com.freightbroker.notifications_service.entity.MailDTO;
import com.freightbroker.notifications_service.service.MailService;
import com.freightbroker.notifications_service.service.MailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/freightbroker")
public class MailController {


    private MailServiceImpl mailService;

    @Autowired
    public void setMailService(MailServiceImpl mailService) {
        this.mailService = mailService;
    }

    @PostMapping("/sendMail")
    public void sendMail(@RequestBody MailDTO mail) {
        mailService.sendMail(mail.getRecipient(),mail.getSubject(), mail.getContent());
    }

}

