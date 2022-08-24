//package com.ayush.proms.mail;
//
//import com.ayush.proms.model.EmailCredential;
//import com.ayush.proms.repo.EmailCredentialRepo;
//import org.springframework.context.annotation.Bean;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//import org.springframework.stereotype.Component;
//
//import java.util.Properties;
//
//@Component
//public class EmailConfig {
//
//    private final EmailCredentialRepo emailCredentialRepo;
//
//    public EmailConfig(EmailCredentialRepo emailCredentialRepo) {
//        this.emailCredentialRepo = emailCredentialRepo;
//
//    }
//
//    @Bean
//    private JavaMailSender javaMailSender(){
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        mailSender.setHost("smtp.gmail.com");
//        mailSender.setPort(587);
//
//        EmailCredential emailCredential = emailCredentialRepo.findByUsername("promis.activation@gmail.com");
//
//        mailSender.setUsername(emailCredential.getUsername());
//        mailSender.setPassword(emailCredential.getPassword());
//
//        Properties props = mailSender.getJavaMailProperties();
//        props.put("mail.transport.protocol", "smtp");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.debug", "true");
//
//        return mailSender;
//    }
//}
