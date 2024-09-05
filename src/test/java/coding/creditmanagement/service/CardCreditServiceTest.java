package coding.creditmanagement.service;

import coding.creditmanagement.CreditManagementApplication;
import coding.creditmanagement.dto.CreditAddDto;
import coding.creditmanagement.dto.CreditInfoDto;
import coding.creditmanagement.dto.CreditReduceDto;
import coding.creditmanagement.dto.TaskResDto;
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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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
    public void testAddCredit() {
        log.info("开始增加额度操作并发测试");
        CreditInfoDto oriInfo=cardCreditService.getCardCredit(TEST_OP_CARD_ID);
        int times=100;
        double amount=10;
        int threadNum=10;
        log.info("对单条数据并发执行{}次增加额度操作，每次增加{}",times,amount);
        TaskResDto taskResDto=executeTasks(
                ()-> cardCreditService.add(new CreditAddDto(TEST_OP_CARD_ID,amount)),
                times,
                threadNum
        );
        CreditInfoDto newInfo=cardCreditService.getCardCredit(TEST_OP_CARD_ID);
        printResult(oriInfo,newInfo,taskResDto);
        Assert.assertEquals(times, taskResDto.getSuccessCount() + taskResDto.getFailCount());
        Assert.assertEquals(oriInfo.getCardCredit()+amount* taskResDto.getSuccessCount(),newInfo.getCardCredit(),DELTA);
    }
    @Test
    public void testReduceCredit() {
        log.info("开始减少额度操作并发测试");
        CreditInfoDto oriInfo=cardCreditService.getCardCredit(TEST_OP_CARD_ID);
        int times=100;
        double amount=10;
        int threadNum=10;
        log.info("对单条数据并发执行{}次减少额度操作，每次减少{}",times,amount);
        TaskResDto taskResDto=executeTasks(
                ()-> cardCreditService.reduce(new CreditReduceDto(TEST_OP_CARD_ID,amount)),
                times,
                threadNum
        );
        CreditInfoDto newInfo=cardCreditService.getCardCredit(TEST_OP_CARD_ID);
        printResult(oriInfo,newInfo,taskResDto);
        Assert.assertEquals(times, taskResDto.getSuccessCount() + taskResDto.getFailCount());
        Assert.assertEquals(oriInfo.getCardCredit()-amount* taskResDto.getSuccessCount(),newInfo.getCardCredit(),DELTA);
    }
    private void printResult(CreditInfoDto oriInfo,CreditInfoDto newInfo,TaskResDto taskResDto){
        log.info("数据库原始金额为{}",oriInfo.getCardCredit());
        log.info("共计成功执行{}次",taskResDto.getSuccessCount());
        log.info("共计失败执行{}",taskResDto.getFailCount());
        log.info("数据库当前金额为{}",newInfo.getCardCredit());
    }
    private TaskResDto executeTasks(Callable<CreditInfoDto> creditInfoDtoCallable,int times,int threadNum){
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
        int successCount=0;
        int failCount=0;
        List<Future<CreditInfoDto>> futureList=new ArrayList<>();
        for(int i=0;i<times;i++){
            Future<CreditInfoDto> future=executorService.submit(creditInfoDtoCallable);
            futureList.add(future);
        }
        executorService.shutdown();

        for (int i = 0; i < futureList.size(); i++) {
            Future<CreditInfoDto> future = futureList.get(i);
            try {
                CreditInfoDto creditInfoDto=future.get();
                log.info("第{}个操作执行完毕，当前金额为{}",i,creditInfoDto.getCardCredit());
                successCount++;
            } catch (Exception e) {
                // 处理任务异常
                e.printStackTrace();
                failCount++;
            }
        }
        return new TaskResDto(successCount,failCount);
    }
}
