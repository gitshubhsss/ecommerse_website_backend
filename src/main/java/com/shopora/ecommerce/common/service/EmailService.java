package com.shopora.ecommerce.common.service;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {


    private  final JavaMailSender mailSender;

    public void sendPasswordResetEmail(String toEmail,String resetLink){
        SimpleMailMessage message=new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("Password Reset Request");
        message.setText("Click the link below to reset your password.\\n"+resetLink+"\\n\\nThis link expires in 30 minutes. If you did not request this, ignore this email.\"");

        mailSender.send(message);
    }
}
