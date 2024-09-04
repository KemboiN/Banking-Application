package Banking.Application.Service;

import Banking.Application.Dto.TransactionDto;
import Banking.Application.Entity.Transaction;
import Banking.Application.Repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionServiceImpl implements TransactionService{
    @Autowired
    TransactionRepository transactionRepository;
    @Override
    public void saveTransaction(TransactionDto transactionDto)
    {
        Transaction transaction= Transaction.builder()
                .transactionType(transactionDto.getTransactionType())
                .transactionAccountNumber(transactionDto.getTransactionAccountNumber())
                .amount(transactionDto.getAmount())
                .transactionStatus("SUCCESS")
                .build();
        transactionRepository.save(transaction);
        System.out.println("Transaction saved successfully");
    }
    }

