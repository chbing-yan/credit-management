-- 创建一个存储过程来插入 10w 条卡片额度数据
use credit_management;
DROP PROCEDURE IF EXISTS InsertData;
DELIMITER //

CREATE PROCEDURE InsertData()
BEGIN
    DECLARE i INT DEFAULT 0;

    WHILE i < 100000 DO
        INSERT INTO card_credit (card_id,user_id,card_credit,card_type,card_status,create_time,update_time)
        VALUES (UUID(), UUID(), 100000.00,'USD','ACTIVE',NOW(),NOW());
        SET i = i + 1;
    END WHILE;
END //

DELIMITER ;


-- 调用存储过程
CALL InsertData();
