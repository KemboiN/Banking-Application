package Banking.Application.Exception;

public class AccountExistException extends  RuntimeException{
    public AccountExistException(String message)  {
        super(message);
    }
}
