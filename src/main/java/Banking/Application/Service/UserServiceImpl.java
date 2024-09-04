package Banking.Application.Service;

import Banking.Application.Config.JwtTokenProvider;
import Banking.Application.Dto.*;
import Banking.Application.Entity.Role;
import Banking.Application.Entity.User;
import Banking.Application.Repository.UserRepository;
import Banking.Application.Utils.AccountUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import Banking.Application.Dto.UserRequest;

import java.math.BigDecimal;
import java.math.BigInteger;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
	private final UserRepository userRepository;
	@Autowired
	EmailService emailService;
	@Autowired
	TransactionService transactionService;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	JwtTokenProvider jwtTokenProvider;

	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public BankResponse createAccount(UserRequest userRequest) {
		// Check if the email already exists
        if (!userRepository.existsByEmail(userRequest.getEmail()))
		{
            User user = User.builder()
                    .firstName(userRequest.getFirstName())
                    .lastName(userRequest.getLastName())
                    .otherName(userRequest.getOtherName())
                    .email(userRequest.getEmail())
                    .password(passwordEncoder.encode(userRequest.getPassword()))
                    .gender(userRequest.getGender())
                    .address(userRequest.getAddress())
                    .nationality(userRequest.getNationality())
                    .idNumber(userRequest.getIdNumber())
                    .phoneNumber(userRequest.getPhoneNumber())
                    .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                    .accountNumber(AccountUtils.GenerateAccountNumber())
                    .accountBalance(BigDecimal.ZERO)
					.role(Role.valueOf("ROLE_USER"))
                    .status("ACTIVE")
                    .build();
            User savedUser = userRepository.save(user);
            EmailDetails emailDetails = EmailDetails.builder()
                    .recipient(savedUser.getEmail())
                    .subject("ACCOUNT CREATION")
                    .messageBody("Congratulations!! Your Account has been created successfully \n Account Name\n" +
                            "Account Name: " + savedUser.getFirstName() + " " + savedUser.getLastName() + "" + savedUser.getOtherName()
                            +  " \n Account Number: " + savedUser.getAccountNumber())
                    .build();
            emailService.sendEmailAlert(emailDetails);
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_CREATED__CODE)
                    .responseMessage(AccountUtils.ACCOUNT_CREATED_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountName(savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName())
                            .accountNumber(savedUser.getAccountNumber())
                            .accountBalance(savedUser.getAccountBalance())
                            .build())
                    .build();
        }
		else {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

    }
	public BankResponse login (LoginDto loginDto)
	{
		Authentication authentication=null;

		authentication= authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword())
		);
		EmailDetails loginAlert= EmailDetails.builder()
				.recipient(loginDto.getEmail())
				.subject("You are Logged in!!")
				.messageBody("You are logged into you account. If you did not initiate this request, Contact your Bank ")
				.build();
        emailService.sendEmailAlert(loginAlert);
		return  BankResponse.builder()
				.responseCode("Login success!!")
				.responseMessage(jwtTokenProvider.generateToken(authentication))
				.build();
    }

	@Override
	public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {
		boolean isAccountNumberExist= userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
		if(!isAccountNumberExist)
		{
			return BankResponse.builder()
					.responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
					.responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
					.accountInfo(null)
					.build();
		}
		User foundUser= userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
		return BankResponse.builder()
				.responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
				.responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
				.accountInfo(AccountInfo.builder()
						.accountBalance(foundUser.getAccountBalance())
						.accountName(foundUser.getFirstName()+ " " + foundUser.getLastName()+" "+foundUser.getOtherName())
						.build())
				.build();
	}

	@Override
	public String nameEnquiry(EnquiryRequest enquiryRequest) {
		boolean isAccountNumberExist = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
		if (!isAccountNumberExist) {
			return AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE;

		}
		User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountName());
		return foundUser.getFirstName() + "" + foundUser.getLastName() + "" + foundUser.getOtherName();
	}

	@Override
	public BankResponse creditAccount(CreditDebitRequest creditDebitRequest) {
		boolean isAccountNumberExist= userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());
		if(!isAccountNumberExist)
		{
			return BankResponse.builder()
					.responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
					.responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
					.accountInfo(null)
					.build();
		}
		User userToCredit= userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());
		userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(creditDebitRequest.getAmount()));
		userRepository.save(userToCredit);
     TransactionDto transactionDto=TransactionDto.builder()
			 .transactionAccountNumber(userToCredit.getAccountNumber())
			 .transactionType("CREDIT")
			 .amount(creditDebitRequest.getAmount())
			 .build();
 transactionService.saveTransaction(transactionDto);
        return BankResponse.builder()
				.responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS_CODE)
				.responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
				.accountInfo(AccountInfo.builder()
						.accountName(userToCredit.getFirstName() +""+  userToCredit.getLastName() +""+  userToCredit.getOtherName())
						.accountNumber(userToCredit.getAccountNumber())
						.accountBalance(userToCredit.getAccountBalance())
						.build())
				.build();
    }

	@Override
	public BankResponse debitAccount(CreditDebitRequest creditDebitRequest) {
		boolean isAccountNumberExist= userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());
		if(!isAccountNumberExist)
		{
			return BankResponse.builder()
					.responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
					.responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
					.accountInfo(null)
					.build();
		}
		User userToDebit= userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());
		BigInteger availableBalance= userToDebit.getAccountBalance().toBigInteger();
		BigInteger debitAmount= creditDebitRequest.getAmount().toBigInteger();
		if(availableBalance.intValue() <debitAmount.intValue())
		{
			return BankResponse.builder()
					.responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
					.responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
					.accountInfo(null)
					.build();
		}
 else {
	 userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(creditDebitRequest.getAmount()));
	 userRepository.save(userToDebit);
			TransactionDto transactionDto=TransactionDto.builder()
					.transactionAccountNumber(userToDebit.getAccountNumber())
					.transactionType("DEBIT")
					.amount(creditDebitRequest.getAmount())
					.build();
			transactionService.saveTransaction(transactionDto);
	 return BankResponse.builder()
			 .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS_CODE)
			 .responseMessage(AccountUtils.ACCOUNT_DEBITED_SUCCESS_MESSAGE)
			 .accountInfo(AccountInfo.builder()
					 .accountName(userToDebit.getFirstName()+""+userToDebit.getOtherName()+""+userToDebit.getLastName())
					 .accountNumber(creditDebitRequest.getAccountNumber())
					 .accountBalance(userToDebit.getAccountBalance())
					 .build())

			 .build();
		}
	}

	@Override
	public BankResponse transfer(TransferRequest transferRequest) {
	boolean isDestinationAccountExists=userRepository.existsByAccountNumber(transferRequest.getDestinationAccountNumber());
		if(!isDestinationAccountExists)
		{
			return BankResponse.builder()
					.responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
					.responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
					.accountInfo(null)
					.build();
		}
		User sourceAccountUser=userRepository.findByAccountNumber(transferRequest.getSourceAccountNumber());
		if (transferRequest.getAmount().compareTo(sourceAccountUser.getAccountBalance())>0){
			return  BankResponse.builder()
					.responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
					.responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
					.accountInfo(null)
					.build();

		}
		sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(transferRequest.getAmount()));
		userRepository.save(sourceAccountUser);
		EmailDetails debitAlert= EmailDetails.builder()
				.recipient(sourceAccountUser.getEmail())
				.subject("Debit Alert")
				.messageBody("The sum of"+ transferRequest.getAmount()+"has been deducted from your account, Your new account balance is"
						+sourceAccountUser.getAccountBalance() )
				.build();
		emailService.sendEmailAlert(debitAlert);

		User destinationAccountUser=userRepository.findByAccountNumber(transferRequest.getDestinationAccountNumber());
		destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(transferRequest.getAmount()));
		userRepository.save(destinationAccountUser);
		EmailDetails creditAlert= EmailDetails.builder()
				.recipient(sourceAccountUser.getEmail())
				.subject("Credit Alert")
				.messageBody("The sum of"+ transferRequest.getAmount()+"has been sent to your account from" + sourceAccountUser.getFirstName()
						+""+sourceAccountUser.getLastName()+""+sourceAccountUser.getOtherName() +"Your new Account account balance is"
						+destinationAccountUser.getAccountBalance() )
				.build();
		emailService.sendEmailAlert(creditAlert);
		TransactionDto transactionDto=TransactionDto.builder()
				.transactionAccountNumber(destinationAccountUser.getAccountNumber())
				.transactionType("CREDIT")
				.amount(transferRequest.getAmount())
				.build();
		transactionService.saveTransaction(transactionDto);
        return BankResponse.builder()
				.responseCode(AccountUtils.TRANSFER_SUCCESS_CODE)
				.responseMessage(AccountUtils.TRANSFER_SUCCESS_MESSAGE)
				.accountInfo(null)
				.build();
    }


}
