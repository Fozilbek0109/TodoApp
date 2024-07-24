package tech.uzpro.todoapp.config;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class SendMailService {


    private final JavaMailSender emailSender;

    public SendMailService(final JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendMail(String email, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("Uzpro");
        message.setTo(email);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
}
