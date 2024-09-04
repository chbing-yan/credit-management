package coding.creditmanagement.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
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
    @NotEmpty(message = "卡片id不能为空")
    private String cardId;
    @Min(0)
    @Digits(integer=8, fraction=2, message = "最大金额为99999999.99")
    private Double amount;
}
