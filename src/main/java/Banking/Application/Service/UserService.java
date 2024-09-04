package Banking.Application.Service;
import Banking.Application.Dto.*;
import org.springframework.stereotype.Service;

@Service
public interface UserService 
{
 BankResponse createAccount(UserRequest userRequest);
 BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);
 String nameEnquiry (EnquiryRequest enquiryRequest);
 BankResponse creditAccount(CreditDebitRequest creditDebitRequest);
BankResponse debitAccount(CreditDebitRequest creditDebitRequest);
BankResponse transfer(TransferRequest transferRequest);
BankResponse login(LoginDto loginDto);
}