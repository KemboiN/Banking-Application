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
public class TransferRequest
{
    private String sourceAccountNumber;
    private String destinationAccountNumber;
    private BigDecimal amount;
}
