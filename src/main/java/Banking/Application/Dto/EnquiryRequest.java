package Banking.Application.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnquiryRequest
{
    @Schema(name = "Enquiry Request Account Number")
    private String accountNumber;
    private  String accountName;
    private BigDecimal accountBalance;

}
