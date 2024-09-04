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
public class CreditInfoDto extends Object{
    private String cardId;
    private Double cardCredit;
    @Override
    public String toString(){
        return String.format("cardId: %s, cardCredit:%.2f",cardId,cardCredit);
    }


}
