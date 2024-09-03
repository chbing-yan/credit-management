package coding.creditmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 减少额度
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditReduceDto {
    private String cardId;
    private Double amount;
}
