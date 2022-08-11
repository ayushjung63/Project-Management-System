package com.ayush.proms.mail;

import com.ayush.proms.pojos.UserPOJO;
import com.ayush.proms.repo.EmailCredentialRepo;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailSenderImpl implements EmailSender {

    private final JavaMailSender javaMailSender;

    public EmailSenderImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendLoginCredential(Email email) {
        SimpleMailMessage message=
                new SimpleMailMessage();

        message.setFrom("promis.activation@gmail.com");
        message.setTo(email.getTo());
        message.setSubject("Login Credential");
        message.setText(email.getBody());

        javaMailSender.send(message);

    }


    @Override
    public void sendSupervisorAssignedMail(Email email) {
        SimpleMailMessage message=
                new SimpleMailMessage();

        message.setFrom("promis.activation@gmail.com");
        message.setTo(email.getTo());
        message.setSubject("Assigned to Project");
        message.setText(email.getBody());

        javaMailSender.send(message);
    }
}
