package coding.creditmanagement.dto;

import coding.creditmanagement.enums.CardType;
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
    private String cardId;
    private String userId;
    private Double cardCredit;
    private CardType cardType;
}
