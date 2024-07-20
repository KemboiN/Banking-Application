package Banking.Application.Utils;

import java.time.Year;

public class AccountUtils {
    public static final String ACCOUNT_EXIST_CODE="001";
    public static final String ACCOUNT_EXIST_MESSAGE="This User Already has an Account";
    public static final String ACCOUNT_CREATED__CODE="002";
    public static final String ACCOUNT_CREATED_MESSAGE="ACCOUNT CREATED SUCCESSFULLY";
    public static final String ACCOUNT_NOT_EXIST_CODE ="003";
    public static final String ACCOUNT_NOT_EXIST_MESSAGE ="USER WITH PROVIDED ACCOUNT NUMBER DOES NOT EXIST";
    public static final String ACCOUNT_FOUND_CODE ="004";
    public static final String ACCOUNT_FOUND_MESSAGE ="USER WITH ACCOUNT NUMBER FOUND";
    public static final String ACCOUNT_CREDITED_SUCCESS_CODE ="005";
    public static final String ACCOUNT_CREDITED_SUCCESS_MESSAGE ="ACCOUNT CREDITED SUCCESSFULLY";
    public static final String INSUFFICIENT_BALANCE_CODE ="006";
    public static final String INSUFFICIENT_BALANCE_MESSAGE ="AVAILABLE BALANCE IS INSUFFICIENT";
    public static final String ACCOUNT_DEBITED_SUCCESS_CODE ="007";
    public static final String ACCOUNT_DEBITED_SUCCESS_MESSAGE ="ACCOUNT DEBITED SUCCESSFULLY";
    public static final String TRANSFER_SUCCESS_CODE ="008";
    public static final String TRANSFER_SUCCESS_MESSAGE ="TRANSFER SUCCESSFUL ";



    public static String GenerateAccountNumber()
{
    Year currentYear= Year.now();
    int Min=100000;
    int Max=999999;
    int randomNumber = (int) Math.floor(Math.random()*(Max-Min +1) + Min);
    String year=String.valueOf(currentYear);
    String randomNumberStr=String.valueOf(randomNumber);
    StringBuilder accountNumber=new StringBuilder();
    return accountNumber.append(year).append(randomNumberStr).toString();
}
}
