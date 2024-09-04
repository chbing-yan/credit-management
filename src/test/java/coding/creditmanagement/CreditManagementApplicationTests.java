package coding.creditmanagement;

import coding.creditmanagement.controller.CardCreditController;
import coding.creditmanagement.dto.CreditAddDto;
import coding.creditmanagement.dto.CreditInfoDto;
import coding.creditmanagement.dto.CreditInitDto;
import coding.creditmanagement.dto.CreditReduceDto;
import coding.creditmanagement.entity.CardCredit;
import coding.creditmanagement.enums.CardType;
import coding.creditmanagement.mapper.CardCreditMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static coding.creditmanagement.consts.CardConstant.*;

@SpringBootTest(classes = CreditManagementApplication.class)
@RunWith(SpringRunner.class)
@Slf4j
public class CreditManagementApplicationTests {

    @Autowired
    private CardCreditController creditController;
    @Autowired
    private CardCreditMapper cardCreditMapper;
    //选择一条card_id,用于增减额度测试
    private static String TEST_OP_CARD_ID="fc367f48-691f-11ef-a07e-525400fcde48";

    @Test
    public void testInitializeCredit() {
        //构造数据
        CreditInitDto creditInitDto=new CreditInitDto(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                DEFAULT_AMOUNT,
                CardType.USD
        );
        //调用方法
        CreditInfoDto creditInfoDto = creditController
                .initializeCredit(creditInitDto)
                .getData();

        //初始化后从数据库查询数据
        CardCredit cardCredit=cardCreditMapper.selectByPrimaryKey(creditInfoDto.getCardId());
        //校验数据不为空
        Assert.assertNotNull(cardCredit);
        //期望值与数据库是否一致
        Assert.assertEquals(creditInitDto.getCardId(),cardCredit.getCardId());
        Assert.assertEquals(creditInitDto.getCardCredit(),cardCredit.getCardCredit(),DELTA);

        //期望值与返回值是否一致
        Assert.assertEquals(creditInitDto.getCardId(),creditInfoDto.getCardId());
        Assert.assertEquals(creditInitDto.getCardCredit(),creditInfoDto.getCardCredit(),DELTA);
        log.info(String.format("对卡片%s执行初始化,期望金额为%.2f，数据库查询为%.2f，返回值为%.2f，均一致",
                creditInitDto.getCardId(),
                creditInitDto.getCardCredit(),
                cardCredit.getCardCredit(),
                creditInfoDto.getCardCredit()));
    }

    /**
     * 减少额度
     */
    @Test
    public void testReduce() {
        String cardId=TEST_OP_CARD_ID;
        Double oriCredit = cardCreditMapper.selectByPrimaryKey(cardId).getCardCredit();
        CreditReduceDto reduceDto=new CreditReduceDto();
        reduceDto.setCardId(cardId);
        reduceDto.setAmount(100.00);
        CreditInfoDto creditInfoDto = creditController.reduce(reduceDto).getData();
        Double newCredit=cardCreditMapper.selectByPrimaryKey(cardId).getCardCredit();

        //期望值与返回值相比
        Assert.assertEquals(oriCredit-reduceDto.getAmount(),
                creditInfoDto.getCardCredit(),
                DELTA);

        //期望值与数据库相比
        Assert.assertEquals(oriCredit-reduceDto.getAmount(),
                newCredit,
                DELTA);
        log.info(String.format(
                "对卡片%s执行减少额度操作,减少后的期望金额为%.2f，数据库查询为%.2f，返回值为%.2f，均一致",
                cardId,
                oriCredit-reduceDto.getAmount(),
                newCredit,
                creditInfoDto.getCardCredit()
        ));
    }

    /**
     * 增加额度
     */

    @Test
    public void testAdd() {
        String cardId=TEST_OP_CARD_ID;
        Double oriCredit = cardCreditMapper.selectByPrimaryKey(cardId).getCardCredit();
        CreditAddDto addDto=new CreditAddDto();
        addDto.setCardId(cardId);
        addDto.setAmount(100.00);
        CreditInfoDto creditInfoDto = creditController.add(addDto).getData();
        Double newCredit=cardCreditMapper.selectByPrimaryKey(cardId).getCardCredit();

        //期望值与返回值相比
        Assert.assertEquals(oriCredit+addDto.getAmount(),
                creditInfoDto.getCardCredit(),
                DELTA);

        //期望值与数据库相比
        Assert.assertEquals(oriCredit+addDto.getAmount(),
                newCredit,
                DELTA);
        log.info(String.format(
                "对卡片%s执行增加额度操作,增加后的期望金额为%.2f，数据库查询为%.2f，返回值为%.2f，均一致",
                cardId,
                oriCredit+addDto.getAmount(),
                newCredit,
                creditInfoDto.getCardCredit()
        ));
    }

    @Test
    public void testGetCredit() {
        //准备一条数据，执行查询操作，校验是否查询成功
        String cardId=TEST_QUERY_CARD_ID;
        Double expectedCredit=TEST_QUERY_CARD_CREDIT;
        //测试controller层与mapper层查询结果是否一致，且均不为空
        CreditInfoDto creditInfoDto = creditController.getCardCredit(cardId).getData();
        Assert.assertEquals(expectedCredit,creditInfoDto.getCardCredit(),DELTA);
        log.info(String.format(
                "对卡片 %s 执行查询操作，预期结果为%.2f, 返回结果为%.2f，结果一致",
                cardId,
                TEST_QUERY_CARD_CREDIT,
                creditInfoDto.getCardCredit()
        ));
    }
}

