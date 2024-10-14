package Banking.Application.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountExistException.class)
    public ResponseEntity<ErrorDetails> handleAccountNotFoundException
            (AccountExistException exception, WebRequest request)
    {
        ErrorDetails errorDetails= ErrorDetails.builder()
                .Date(LocalDateTime.now())
                .code("200")
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleAccountNotFoundException
            (AccountNotFoundException exception, WebRequest request)
    {
        ErrorDetails errorDetails= ErrorDetails.builder()
                .Date(LocalDateTime.now())
                .code("300")
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ErrorDetails> handleInsufficientBalanceException
            (InsufficientBalanceException exception, WebRequest request)
    {
        ErrorDetails errorDetails= ErrorDetails.builder()
                .Date(LocalDateTime.now())
                .code("400")
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
}
