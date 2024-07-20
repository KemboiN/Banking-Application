package Banking.Application.Service;

import Banking.Application.Dto.*;
import Banking.Application.Entity.User;
import Banking.Application.Repository.UserRepository;
import Banking.Application.Utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public BankResponse createAccount(UserRequest userRequest) {
		// Check if the email already exists
        if (!userRepository.existsByEmail(userRequest.getEmail()))
		{// Build the User entity
            User user = User.builder()
                    .firstName(userRequest.getFirstName())
                    .lastName(userRequest.getLastName())
                    .otherName(userRequest.getOtherName())
                    .email(userRequest.getEmail())
                    .password(userRequest.getPassword())
                    .gender(userRequest.getGender())
                    .address(userRequest.getAddress())
                    .nationality(userRequest.getNationality())
                    .idNumber(userRequest.getIdNumber())
                    .phoneNumber(userRequest.getPhoneNumber())
                    .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                    .accountNumber(AccountUtils.GenerateAccountNumber())
                    .accountBalance(BigDecimal.ZERO)
                    .status("ACTIVE")
                    .build();

            // Save the user to the database
            User savedUser = userRepository.save(user);
            EmailDetails emailDetails = EmailDetails.builder()
                    .recipient(savedUser.getEmail())
                    .subject("ACCOUNT CREATION")
                    .messageBody("Congratulations!! Your Account has been created successfully \n Account Name\n" +
                            "Account Name:" + savedUser.getFirstName() + "" + savedUser.getLastName() + "" + savedUser.getOtherName()
                            + "\n Account Number:" + savedUser.getAccountNumber())
                    .build();
			
            emailService.SendEmailAlert(emailDetails);

            // Build the BankResponse
            return BankResponse.builder()

                    .responseCode(AccountUtils.ACCOUNT_CREATED__CODE)
                    .responseMessage(AccountUtils.ACCOUNT_CREATED_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountName(savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName())
                            .accountNumber(savedUser.getAccountNumber())
                            .accountBalance(savedUser.getAccountBalance())
                            .build())
                    .build();
        } else {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

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
						.accountName(foundUser.getFirstName()+"" + foundUser.getLastName()+""+foundUser.getOtherName())
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
        return BankResponse.builder()
				.responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS_CODE)
				.responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
				.accountInfo(AccountInfo.builder()
						.accountName(userToCredit.getFirstName()+""+ userToCredit.getLastName()+""+userToCredit.getOtherName())
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
		if (transferRequest.getAmount().compareTo(sourceAccountUser.getAccountBalance())<0){
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
		emailService.SendEmailAlert(debitAlert);
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
        return BankResponse.builder()
				.responseCode(AccountUtils.TRANSFER_SUCCESS_CODE)
				.responseMessage(AccountUtils.TRANSFER_SUCCESS_MESSAGE)
				.accountInfo(null)
				.build();
    }
}
