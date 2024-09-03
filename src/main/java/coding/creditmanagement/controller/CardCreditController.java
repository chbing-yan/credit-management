package coding.creditmanagement.controller;

import coding.creditmanagement.dto.CreditInfoDto;
import coding.creditmanagement.dto.CreditInitDto;
import coding.creditmanagement.dto.CreditReduceDto;
import coding.creditmanagement.dto.CreditAddDto;
import coding.creditmanagement.response.MyResponse;
import coding.creditmanagement.service.CardCreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/credits")
public class CardCreditController {

    @Autowired
    private CardCreditService cardCreditService;

    /**
     * 初始化额度
     * @param creditInitDto
     * @return
     */
    @PostMapping("/initialize")
    public MyResponse<CreditInfoDto> initializeCredit(@RequestBody CreditInitDto creditInitDto) {
        CreditInfoDto creditInfoDto =cardCreditService.initializeCredit(creditInitDto);
        return MyResponse.success(creditInfoDto);
    }

    /**
     * 减少额度
     * @param creditReduceDto
     * @return
     */
    @PostMapping("/reduce")
    public MyResponse<CreditInfoDto> reduce(@RequestBody CreditReduceDto creditReduceDto) {
        CreditInfoDto creditInfoDto = cardCreditService.reduce(creditReduceDto);
        return MyResponse.success(creditInfoDto);
    }

    /**
     * 增加额度
     * @param creditAddDto
     * @return
     */
    @PostMapping("/add")
    public MyResponse<CreditInfoDto> add(@RequestBody CreditAddDto creditAddDto)  {
        CreditInfoDto creditInfoDto = cardCreditService.add(creditAddDto);
        return MyResponse.success(creditInfoDto);
    }

    /**
     * 查询额度
     * @param cardId
     * @return
     */
    @PostMapping("/get")
    public MyResponse<CreditInfoDto> getCardCredit(@RequestBody String cardId) {
        CreditInfoDto creditInfoDto =  cardCreditService.getCardCredit(cardId);
        return MyResponse.success(creditInfoDto);
    }
}

