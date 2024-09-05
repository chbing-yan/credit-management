CREATE DATABASE IF NOT EXISTS credit_management;
-- 选择数据库
USE credit_management;
-- 创建 额度表
CREATE TABLE IF NOT EXISTS card_credit (
    card_id CHAR(36) NOT NULL PRIMARY KEY COMMENT '卡号，唯一标识信用卡',
    user_id CHAR(36) NOT NULL COMMENT ' 客户ID，用于后续关联到客户表,目前暂不使用',               --

    card_credit DOUBLE(10,2) NOT NULL COMMENT '卡片额度',

    card_type ENUM('USD','RMB') NOT NULL COMMENT '卡片金额类型（如USD:美元、RMB:人民币）',
    card_status ENUM('ACTIVE', 'CLOSED', 'SUSPENDED') NOT NULL COMMENT '卡片状态',

    create_time DATETIME NOT NULL COMMENT '建卡时间',
    update_time DATETIME NOT NULL COMMENT '更新时间'

);