package coding.creditmanagement.entity;

import java.util.Date;

public class CardCredit {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column card_credit.card_id
     *
     * @mbg.generated Mon Sep 02 18:01:27 CST 2024
     */
    private String cardId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column card_credit.user_id
     *
     * @mbg.generated Mon Sep 02 18:01:27 CST 2024
     */
    private String userId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column card_credit.card_credit
     *
     * @mbg.generated Mon Sep 02 18:01:27 CST 2024
     */
    private Double cardCredit;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column card_credit.card_type
     *
     * @mbg.generated Mon Sep 02 18:01:27 CST 2024
     */
    private String cardType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column card_credit.card_status
     *
     * @mbg.generated Mon Sep 02 18:01:27 CST 2024
     */
    private String cardStatus;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column card_credit.create_time
     *
     * @mbg.generated Mon Sep 02 18:01:27 CST 2024
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column card_credit.update_time
     *
     * @mbg.generated Mon Sep 02 18:01:27 CST 2024
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column card_credit.card_id
     *
     * @return the value of card_credit.card_id
     *
     * @mbg.generated Mon Sep 02 18:01:27 CST 2024
     */
    public String getCardId() {
        return cardId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column card_credit.card_id
     *
     * @param cardId the value for card_credit.card_id
     *
     * @mbg.generated Mon Sep 02 18:01:27 CST 2024
     */
    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column card_credit.user_id
     *
     * @return the value of card_credit.user_id
     *
     * @mbg.generated Mon Sep 02 18:01:27 CST 2024
     */
    public String getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column card_credit.user_id
     *
     * @param userId the value for card_credit.user_id
     *
     * @mbg.generated Mon Sep 02 18:01:27 CST 2024
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column card_credit.card_credit
     *
     * @return the value of card_credit.card_credit
     *
     * @mbg.generated Mon Sep 02 18:01:27 CST 2024
     */
    public Double getCardCredit() {
        return cardCredit;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column card_credit.card_credit
     *
     * @param cardCredit the value for card_credit.card_credit
     *
     * @mbg.generated Mon Sep 02 18:01:27 CST 2024
     */
    public void setCardCredit(Double cardCredit) {
        this.cardCredit = cardCredit;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column card_credit.card_type
     *
     * @return the value of card_credit.card_type
     *
     * @mbg.generated Mon Sep 02 18:01:27 CST 2024
     */
    public String getCardType() {
        return cardType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column card_credit.card_type
     *
     * @param cardType the value for card_credit.card_type
     *
     * @mbg.generated Mon Sep 02 18:01:27 CST 2024
     */
    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column card_credit.card_status
     *
     * @return the value of card_credit.card_status
     *
     * @mbg.generated Mon Sep 02 18:01:27 CST 2024
     */
    public String getCardStatus() {
        return cardStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column card_credit.card_status
     *
     * @param cardStatus the value for card_credit.card_status
     *
     * @mbg.generated Mon Sep 02 18:01:27 CST 2024
     */
    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column card_credit.create_time
     *
     * @return the value of card_credit.create_time
     *
     * @mbg.generated Mon Sep 02 18:01:27 CST 2024
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column card_credit.create_time
     *
     * @param createTime the value for card_credit.create_time
     *
     * @mbg.generated Mon Sep 02 18:01:27 CST 2024
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column card_credit.update_time
     *
     * @return the value of card_credit.update_time
     *
     * @mbg.generated Mon Sep 02 18:01:27 CST 2024
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column card_credit.update_time
     *
     * @param updateTime the value for card_credit.update_time
     *
     * @mbg.generated Mon Sep 02 18:01:27 CST 2024
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}