package coding.creditmanagement.dto;

import coding.creditmanagement.enums.CardType;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 卡片额度初始化参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditInitDto {

    @NotEmpty(message = "卡片id不能为空")
    private String cardId;

    @NotEmpty(message = "用户id不能为空")
    private String userId;

    @Min(0)
    @Digits(integer=8, fraction=2, message = "最大金额为99999999.99")
    private Double cardCredit;

    private CardType cardType;
}
