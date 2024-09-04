package Banking.Application.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreditDebitRequest
{@Schema(name = "Credit/Debit Request Account Number")
    private  String accountNumber;
@Schema(name = "Credit/Debit Request Account Balance")
    private BigDecimal accountBalance;
    @Schema(name = "Credit/Debit Request Amount")

    private BigDecimal amount;

}
