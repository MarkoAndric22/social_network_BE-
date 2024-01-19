package com.example.network.services;

import com.example.network.entities.EmailObject;

import javax.mail.MessagingException;

public interface EmailService {

    void sendSimpleMessage(EmailObject object) throws Exception;

    void sendMimeMessage (String to, String subject, String body) throws MessagingException;

}
