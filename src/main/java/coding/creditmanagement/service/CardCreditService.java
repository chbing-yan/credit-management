package coding.creditmanagement.service;

import coding.creditmanagement.consts.CardConstant;
import coding.creditmanagement.dto.CreditAddDto;
import coding.creditmanagement.dto.CreditInfoDto;
import coding.creditmanagement.dto.CreditInitDto;
import coding.creditmanagement.dto.CreditReduceDto;
import coding.creditmanagement.entity.CardCredit;
import coding.creditmanagement.enums.CardStatus;
import coding.creditmanagement.mapper.CardCreditMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.Date;
@Slf4j
@Service
public class CardCreditService {
    @Autowired
    private CardCreditMapper cardCreditMapper;


    /**
     * 初始化额度
     * @param creditInitDto
     * @return
     */
    public CreditInfoDto initializeCredit(CreditInitDto creditInitDto) {
        log.info(String.format("对卡片%s进行额度初始化",creditInitDto.getCardId()));
        Date current= Date.from(Instant.now());
        CardCredit cardCredit = new CardCredit();
        cardCredit.setCardId(creditInitDto.getCardId());
        cardCredit.setUserId(creditInitDto.getUserId());
        cardCredit.setCardCredit(creditInitDto.getCardCredit());
        cardCredit.setCardType(creditInitDto.getCardType().toString());
        cardCredit.setCardStatus(CardStatus.ACTIVE.toString());

        cardCredit.setCreateTime(current);
        cardCredit.setUpdateTime(current);
        cardCreditMapper.insert(cardCredit);
        return new CreditInfoDto(cardCredit.getCardId(),
                cardCredit.getCardCredit());
    }

    /**
     * 减少额度
     * @param creditReduceDto
     * @return
     */
    @Transactional
    public CreditInfoDto reduce (CreditReduceDto creditReduceDto) {
        CardCredit cardCredit = cardCreditMapper.selectForUpdate(creditReduceDto.getCardId());

        Assert.notNull(cardCredit,"尚未初始化额度");
        //确保卡片状态为active
        Assert.isTrue(CardStatus.ACTIVE.name().equals(cardCredit.getCardStatus()),"卡片状态异常，无法操作");

        if(creditReduceDto.getAmount()>cardCredit.getCardCredit()){
            throw new RuntimeException(String.format("减少的额度大于总额度，当前总额度为%.2f",cardCredit.getCardCredit()));
        }
        cardCredit.setCardCredit(cardCredit.getCardCredit()- creditReduceDto.getAmount());
        cardCredit.setUpdateTime(Date.from(Instant.now()));
        cardCreditMapper.updateByPrimaryKey(cardCredit);
        return new CreditInfoDto(cardCredit.getCardId(),
                cardCredit.getCardCredit());

    }

    /**
     * 增加额度
     * @param creditAddDto
     * @return
     */

    @Transactional
    public CreditInfoDto add(CreditAddDto creditAddDto) {
        CardCredit cardCredit = cardCreditMapper.selectForUpdate(creditAddDto.getCardId());

        Assert.notNull(cardCredit,"尚未初始化额度");
        //确保卡片状态为active
        Assert.isTrue(CardStatus.ACTIVE.name().equals(cardCredit.getCardStatus()),"卡片状态异常，无法操作");

        double newCredit=cardCredit.getCardCredit()+creditAddDto.getAmount();
        if(newCredit> CardConstant.MAX_AMOUNT){
            throw new RuntimeException(
                    String.format("您申请的额度超过限制，最多还可申请%.2f",
                    CardConstant.MAX_AMOUNT-cardCredit.getCardCredit()));
        }
        //更新额度
        cardCredit.setCardCredit(newCredit);
        cardCredit.setUpdateTime(Date.from(Instant.now()));
        cardCreditMapper.updateByPrimaryKey(cardCredit);
        return new CreditInfoDto(cardCredit.getCardId(), cardCredit.getCardCredit());
    }

    public CreditInfoDto getCardCredit(String cardId) {
        CardCredit cardCredit = cardCreditMapper.selectByPrimaryKey(cardId);
        if(cardCredit==null){
            return null;
        }
        return new CreditInfoDto(cardCredit.getCardId(),
                cardCredit.getCardCredit());
    }

}
