package Banking.Application.Service;

import Banking.Application.Dto.EmailDetails;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String SenderEmail;


    @Override
    public void sendEmailAlert(EmailDetails emailDetails) {
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

    @Override
    public void sendEmailAttachment(EmailDetails emailDetails) {
        MimeMessage mimeMessage =javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;
        try {
            mimeMessageHelper= new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(SenderEmail);
            mimeMessageHelper.setTo(emailDetails.getRecipient());
            mimeMessageHelper.setSubject(emailDetails.getSubject());
            mimeMessageHelper.setText(emailDetails.getMessageBody());

            FileSystemResource file= new FileSystemResource(new File(emailDetails.getAttachment()));
            mimeMessageHelper.addAttachment(file.getFilename(), file);
            javaMailSender.send(mimeMessage);

            log.info(file.getFilename() + "  has successfully been sent to user with email " + emailDetails.getRecipient());

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}



