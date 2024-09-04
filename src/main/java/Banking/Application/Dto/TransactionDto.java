package Banking.Application.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDto
{
 private String transactionType;
 private String transactionAccountNumber;
 private BigDecimal amount;
 private String transactionStatus;
}
