package Banking.Application.Service;

import Banking.Application.Dto.EmailDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class EmailServiceImpl extends EmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String SenderEmail;

    @Override
    public void SendEmailAlert(EmailDetails emailDetails) {
 try{
     SimpleMailMessage simpleMailMessage= new SimpleMailMessage();
     simpleMailMessage.setFrom(SenderEmail);
     simpleMailMessage.setTo(emailDetails.getRecipient());
     simpleMailMessage.setSubject(emailDetails.getSubject());
     simpleMailMessage.setText(emailDetails.getMessageBody());
     javaMailSender.send(simpleMailMessage);
 } catch (MailException e) {
     throw new RuntimeException(e);
 }
        System.out.println("Mail sent successfully");
    }
}
