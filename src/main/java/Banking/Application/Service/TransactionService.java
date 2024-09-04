package Banking.Application.Service;

import Banking.Application.Dto.TransactionDto;
import org.springframework.stereotype.Service;

@Service
public interface TransactionService{

    void saveTransaction(TransactionDto transactionDto);
}
