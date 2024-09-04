package coding.creditmanagement.scheduler;

import coding.creditmanagement.consts.CardConstant;
import coding.creditmanagement.dto.CreditAddDto;
import coding.creditmanagement.dto.CreditInfoDto;
import coding.creditmanagement.dto.CreditInitDto;
import coding.creditmanagement.dto.CreditReduceDto;
import coding.creditmanagement.enums.CardType;
import coding.creditmanagement.mapper.CardCreditMapper;
import coding.creditmanagement.response.MyResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.UUID;
import static coding.creditmanagement.consts.CardConstant.TEST_QUERY_CARD_ID;

@Component
@Slf4j
public class CreditOperationScheduler {
    @Autowired
    private CardCreditMapper cardCreditMapper;
    @Autowired
    private RestTemplate restTemplate;

    private static final String USER_OPERATION_URL = "http://localhost:8080/credits/";
    private static final String[] OPERATIONS={"initialize","reduce","add","get"};
    // 设置各随机数的概率,此处假设初始化操作概率很低（对应新用户开卡）
    // 其他3种操作概率相等
    private static double[] probabilities = {0.0001, 0.3333, 0.3333, 0.3333}; // 0.01%, 33.33%, 33.33%, 33.33%
    //各个
    private  static int[] values = {0, 1, 2, 3}; // 对应的随机数
    private ParameterizedTypeReference<MyResponse<CreditInfoDto>> responseType =
            new ParameterizedTypeReference<>() {
            };
    // 定时任务，每10毫秒调用一次
    @Scheduled(fixedRate = 100)
    public void performUserOperations() {
        //随机选择一张信用卡
        String cardId=cardCreditMapper.selectRandomCardId();
        while (TEST_QUERY_CARD_ID.equals(cardId)){
            cardId=cardCreditMapper.selectRandomCardId();
        }
        //按照一定的概率{0.0001, 0.3333, 0.3333, 0.3333}生成{0,1,2,3}四个随机数
        // 代表4种操作
        int operation = generateRandomNumber(probabilities,values);
        String url=String.format("%s%s",USER_OPERATION_URL,OPERATIONS[operation]);
        switch (operation){
            case 0:{
                CreditInitDto creditInitDto=new CreditInitDto(
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString(),
                        CardConstant.DEFAULT_AMOUNT,
                        CardType.USD
                );
//                初始化额度
                MyResponse<CreditInfoDto> response = restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        new HttpEntity<>(creditInitDto),
                        responseType
                ).getBody();

                return;
            }
            case 1:{
                //假设减少额度是【0.01，10000）的随机数
                double randomNumber = 0.01 + (9999.99 * Math.random());
                randomNumber= Math.round(randomNumber * 100.0) / 100.0;
                Double oriCredit = cardCreditMapper.selectByPrimaryKey(cardId).getCardCredit();
                CreditReduceDto creditReduceDto=new CreditReduceDto(cardId,randomNumber);
//                减少额度
                MyResponse<CreditInfoDto> response = restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        new HttpEntity<>(creditReduceDto),
                        responseType
                ).getBody();
                Assert.notNull(response,"减少额度返回异常");
                if(CardConstant.SUCCESS_CODE.equals(response.getCode())){
                    CreditInfoDto creditInfoDto=response.getData();
                    log.info(String.format("对卡片{%s},执行减少额度操作,原始额度为%.2f,减少额度%.2f,当前额度为%.2f",creditInfoDto.getCardId(), oriCredit,creditReduceDto.getAmount(),creditInfoDto.getCardCredit()));
                }
                return;
            }
            case 2:{
                double randomNumber = 0.01 + (9999.99 * Math.random());
                randomNumber= Math.round(randomNumber * 100.0) / 100.0;

                Double oriCredit = cardCreditMapper.selectByPrimaryKey(cardId).getCardCredit();

                CreditAddDto creditAddDto=new CreditAddDto(cardId,randomNumber);
//                增加额度
                MyResponse<CreditInfoDto> response = restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        new HttpEntity<>(creditAddDto),
                        responseType
                ).getBody();

                Assert.notNull(response,"增加额度返回异常");
                if(CardConstant.SUCCESS_CODE.equals(response.getCode())){
                    CreditInfoDto creditInfoDto=response.getData();
                    log.info(String.format("对卡片{%s},执行增加额度操作,原始额度为%.2f,增加额度%.2f,当前额度为%.2f",creditInfoDto.getCardId(), oriCredit,creditAddDto.getAmount(),creditInfoDto.getCardCredit()));

                }
                return;
            }
            case 3:{
                String queryUrl = UriComponentsBuilder.fromUriString(url)
                        .queryParam("cardId", cardId)
                        .toUriString();
//                查询额度
                MyResponse<CreditInfoDto> response = restTemplate.exchange(
                        queryUrl,
                        HttpMethod.POST,
                        null,
                        responseType

                ).getBody();

                if(CardConstant.SUCCESS_CODE.equals(response.getCode())){
                    CreditInfoDto creditInfoDto=response.getData();
                    log.info(String.format("对卡片{%s},执行查询额度操作,当前额度为%.2f",creditInfoDto.getCardId(),creditInfoDto.getCardCredit()));
                }
                else {
                    log.error(String.format("查询额度失败，原因%s" ,response.getMessage()));
                }
                return;
            }
            default:{
                throw new IllegalStateException("目前仅支持4种操作");
            }

        }
    }
    private static int generateRandomNumber(double[] probabilities, int[] values) {
        double randomValue = Math.random(); // 生成 0 到 1 之间的随机数
        double cumulativeProbability = 0.0;
        for (int i = 0; i < probabilities.length; i++) {
            cumulativeProbability += probabilities[i];
            if (randomValue < cumulativeProbability) {
                return values[i];
            }
        }
        throw new IllegalStateException("生成的随机数应该小于最大累计概率（即1）");
    }
}
