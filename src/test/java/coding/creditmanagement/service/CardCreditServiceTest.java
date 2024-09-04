package coding.creditmanagement.service;

import coding.creditmanagement.CreditManagementApplication;
import coding.creditmanagement.dto.CreditAddDto;
import coding.creditmanagement.dto.CreditInfoDto;
import coding.creditmanagement.mapper.CardCreditMapper;
import coding.creditmanagement.scheduler.CreditOperationScheduler;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static coding.creditmanagement.consts.CardConstant.DELTA;
import static coding.creditmanagement.consts.CardConstant.TEST_OP_CARD_ID;
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CreditManagementApplication.class)
public class CardCreditServiceTest {
    @MockBean
    CreditOperationScheduler creditOperationScheduler;
    @Autowired
    CardCreditService cardCreditService;
    @Autowired
    CardCreditMapper cardCreditMapper;
    @Test
    public void addCreditTest() {
        CreditInfoDto oriInfo=cardCreditService.getCardCredit(TEST_OP_CARD_ID);
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        int times=100;
        double amount=10;
        int successCount=0;
        int failCount=0;
        List<Callable<CreditInfoDto>> tasks = new ArrayList<>();
        for (int i = 0; i < times; i++) {
            tasks.add(() -> cardCreditService.add(new CreditAddDto(TEST_OP_CARD_ID,amount)));
        }
        // 提交任务并等待所有任务完成
        List<Future<CreditInfoDto>> results = null;
        try {
            results = executorService.invokeAll(tasks);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for (Future<CreditInfoDto> result : results) {
            try {
                log.info(result.get().toString());
                successCount++;
            } catch (InterruptedException | ExecutionException e) {
                failCount++;
                e.printStackTrace();
            }

        }
            executorService.shutdown();

        CreditInfoDto newInfo=cardCreditService.getCardCredit(TEST_OP_CARD_ID);
        log.info("数据库原始金额为{}",oriInfo.getCardCredit());
        log.info("共计成功执行{}次，每次增加{}",successCount,amount);
        log.info("共计失败执行{}",failCount);
        log.info("数据库当前金额为{}",newInfo.getCardCredit());
        Assert.assertEquals(times,successCount+failCount);
        Assert.assertEquals(oriInfo.getCardCredit()+amount*successCount,newInfo.getCardCredit(),DELTA);

    }

}
