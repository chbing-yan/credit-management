package coding.creditmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 增加额度
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditAddDto {
    private String cardId;
    private Double amount;
}
