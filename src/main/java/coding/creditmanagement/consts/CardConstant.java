package coding.creditmanagement.consts;

public class CardConstant {
    public static Double DEFAULT_AMOUNT=100000.0;//初始化额度10w
    public static Double MAX_AMOUNT=99999999.99;//数据库支持的最大金额
    public static Double DELTA=0.0001;//考虑到精度丢失问题，用于double类型等于比较
    public static Integer SUCCESS_CODE=200;//操作成功后返回的状态码
    public static String TEST_QUERY_CARD_ID="fe5c2481-691f-11ef-a07e-525400fcde48";
    //用于额度查询测试，定时任务随机操作时，跳过该数据。
    public static Double TEST_QUERY_CARD_CREDIT=100000.00;

}
