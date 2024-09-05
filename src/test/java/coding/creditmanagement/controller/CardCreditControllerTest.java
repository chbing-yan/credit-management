package coding.creditmanagement.controller;

import coding.creditmanagement.dto.CreditAddDto;
import coding.creditmanagement.dto.CreditInitDto;
import coding.creditmanagement.dto.CreditReduceDto;
import coding.creditmanagement.enums.CardType;
import coding.creditmanagement.service.CardCreditService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static coding.creditmanagement.consts.CardConstant.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CardCreditControllerTest {
    @Autowired
    WebApplicationContext webApplicationContext;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    CardCreditService cardCreditService;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }
    private  final String USER_OPERATION_URL = "/credits/";
    private  final String[] OPERATIONS={"initialize","reduce","add","get"};
    private static String contentType="application/json";
    @Test
    public void testInitializeCredit() throws Exception {
        //正常输入测试
        CreditInitDto creditInitDto=new CreditInitDto(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                DEFAULT_AMOUNT,
                CardType.USD
        );
        doPost(OPERATIONS[0],creditInitDto).andExpect(status().isOk());
        //测试错误输入（金额过大）
        creditInitDto.setCardCredit(MAX_AMOUNT+0.01);
        doPost(OPERATIONS[0],creditInitDto).andExpect(status().is4xxClientError());
        //测试错误输入（金额小于0）
        creditInitDto.setCardCredit(-1.0);
        doPost(OPERATIONS[0],creditInitDto).andExpect(status().is4xxClientError());
        //测试错误输入（卡片id为空）
        creditInitDto.setCardId(null);
        creditInitDto.setCardCredit(DEFAULT_AMOUNT);
        doPost(OPERATIONS[0],creditInitDto).andExpect(status().is4xxClientError());
        //测试错误输入（用于id为空）
        creditInitDto.setCardId(UUID.randomUUID().toString());
        creditInitDto.setUserId(null);
        doPost(OPERATIONS[0],creditInitDto).andExpect(status().is4xxClientError());
    }
    @Test
    public void testReduceCredit() throws Exception {
//        测试正常输入
        String cardId=TEST_OP_CARD_ID;
        CreditReduceDto reduceDto=new CreditReduceDto();
        reduceDto.setCardId(cardId);
        reduceDto.setAmount(100.00);
        doPost(OPERATIONS[1],reduceDto).andExpect(status().is2xxSuccessful());
//        测试cardId输入异常
        reduceDto.setCardId(null);
        doPost(OPERATIONS[1],reduceDto).andExpect(status().is4xxClientError());
        //测试金额过大
        reduceDto.setCardId(TEST_OP_CARD_ID);
        reduceDto.setAmount(MAX_AMOUNT+10);
        doPost(OPERATIONS[1],reduceDto).andExpect(status().is4xxClientError());
        // 测试金额负数
        reduceDto.setAmount(-100.0);
        doPost(OPERATIONS[1],reduceDto).andExpect(status().is4xxClientError());

    }
    @Test
    public void testAddCredit() throws Exception {
//        测试正常输入
        String cardId=TEST_OP_CARD_ID;
        CreditAddDto addDto=new CreditAddDto();
        addDto.setCardId(cardId);
        addDto.setAmount(100.00);
        doPost(OPERATIONS[2],addDto).andExpect(status().is2xxSuccessful());
//        测试cardId输入异常
        addDto.setCardId(null);
        doPost(OPERATIONS[2],addDto).andExpect(status().is4xxClientError());
        //测试金额过大
        addDto.setCardId(TEST_OP_CARD_ID);
        addDto.setAmount(MAX_AMOUNT+10);
        doPost(OPERATIONS[2],addDto).andExpect(status().is4xxClientError());
        // 测试金额负数
        addDto.setAmount(-100.0);
        doPost(OPERATIONS[2],addDto).andExpect(status().is4xxClientError());
    }

    @Test
    public void testGetCredit() throws Exception {
        //测试正常cardid
        doGet(OPERATIONS[3],TEST_QUERY_CARD_ID).andExpect(status().isOk());
        //测试cardId为空
        doGet(OPERATIONS[3],null).andExpect(status().is4xxClientError());

    }

    private ResultActions doGet(String operation,String cardId) throws Exception {
        String url=String.format("%s%s",USER_OPERATION_URL,operation);
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(contentType)
                .queryParam("cardId",cardId));
        return resultActions;
    }
    private ResultActions doPost(String operation,Object content) throws Exception {
        String url=String.format("%s%s",USER_OPERATION_URL,operation);
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(contentType)
                .content(objectMapper.writeValueAsString(content)));
        return resultActions;
    }


}
