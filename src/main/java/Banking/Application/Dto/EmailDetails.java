package Banking.Application.Dto;

import lombok.AllArgsConstructor;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EmailDetails {
    private String recipient;
    private String subject;
    private String messageBody;
    private String attachment;
}
