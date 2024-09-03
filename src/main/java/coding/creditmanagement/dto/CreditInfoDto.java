package coding.creditmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 额度信息
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreditInfoDto {
    private String cardId;
    private Double cardCredit;


}
