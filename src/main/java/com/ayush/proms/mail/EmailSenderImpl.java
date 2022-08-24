package com.ayush.proms.mail;

import com.ayush.proms.pojos.UserPOJO;
import com.ayush.proms.repo.EmailCredentialRepo;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableAsync
public class EmailSenderImpl implements EmailSender {

    private final JavaMailSender javaMailSender;

    public EmailSenderImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    @Override
    public void sendLoginCredential(Email email) {
        SimpleMailMessage message=
                new SimpleMailMessage();

        message.setFrom("promis.activation@gmail.com");
        message.setTo(email.getTo());
        message.setSubject(email.getSubject());
        message.setText(email.getBody());

        javaMailSender.send(message);

    }


    @Async
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
