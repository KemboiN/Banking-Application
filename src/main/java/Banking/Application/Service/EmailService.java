package Banking.Application.Service;

import Banking.Application.Dto.EmailDetails;
import org.springframework.stereotype.Service;

@Service
public interface EmailService
{
    void sendEmailAlert(EmailDetails emailDetails);
    void  sendEmailAttachment(EmailDetails emailDetails);
}
