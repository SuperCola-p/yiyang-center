-- ============================================================
-- 东软颐养中心管理系统 — 数据库初始化脚本
-- 数据库: yiyang_center
-- ============================================================

CREATE DATABASE IF NOT EXISTS yiyang_center
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE yiyang_center;

-- ============================================================
-- 1. 操作员表（基表，含管理员/服务人员）
-- ============================================================
CREATE TABLE IF NOT EXISTS `operator` (
    `login_code`    VARCHAR(50)  NOT NULL COMMENT '登录账号',
    `password`      VARCHAR(100) NOT NULL COMMENT '密码',
    `real_name`     VARCHAR(50)  NOT NULL COMMENT '真实姓名',
    `operator_type` VARCHAR(20)  NOT NULL DEFAULT 'ADMIN' COMMENT '类型: ADMIN-管理员, SERVICE-服务人员',
    `deleted`       TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '逻辑删除标记',
    PRIMARY KEY (`login_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作员表';

-- ============================================================
-- 2. 客户表
-- ============================================================
CREATE TABLE IF NOT EXISTS `client` (
    `id`                   INT          NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`                 VARCHAR(50)  DEFAULT NULL COMMENT '姓名',
    `age`                  INT          DEFAULT NULL COMMENT '年龄',
    `gender`               VARCHAR(10)  DEFAULT NULL COMMENT '性别',
    `blood_type`           VARCHAR(10)  DEFAULT NULL COMMENT '血型',
    `phone`                VARCHAR(20)  DEFAULT NULL COMMENT '联系电话',
    `family_contact`       VARCHAR(100) DEFAULT NULL COMMENT '家属联系方式',
    `id_card`              VARCHAR(18)  DEFAULT NULL COMMENT '身份证号',
    `building_no`          VARCHAR(10)  DEFAULT NULL COMMENT '楼号',
    `room_no`              VARCHAR(10)  DEFAULT NULL COMMENT '房间号',
    `bed_no`               VARCHAR(10)  DEFAULT NULL COMMENT '床位号',
    `birthday`             DATE         DEFAULT NULL COMMENT '出生日期',
    `check_in_date`        DATE         DEFAULT NULL COMMENT '入住日期',
    `contract_expire_date` DATE         DEFAULT NULL COMMENT '合同到期日期',
    `nursing_level`        VARCHAR(50)  DEFAULT NULL COMMENT '护理级别',
    `nurse`                VARCHAR(50)  DEFAULT NULL COMMENT '负责护工',
    `health_status`        VARCHAR(100) DEFAULT NULL COMMENT '健康状况',
    `type`                 VARCHAR(20)  DEFAULT NULL COMMENT '类型: 自理老人/护理老人',
    `deleted`              TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '逻辑删除标记',
    PRIMARY KEY (`id`),
    KEY `idx_name` (`name`),
    KEY `idx_type` (`type`),
    KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户表';

-- ============================================================
-- 3. 床位表
-- ============================================================
CREATE TABLE IF NOT EXISTS `bed` (
    `id`         BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `building`   VARCHAR(10)  DEFAULT NULL COMMENT '楼号',
    `room_no`    INT          DEFAULT NULL COMMENT '房间编号',
    `bed_no`     VARCHAR(10)  DEFAULT NULL COMMENT '床位编号',
    `bed_status` INT          NOT NULL DEFAULT 1 COMMENT '状态: 1=空闲, 2=有人, 3=外出',
    `remarks`    VARCHAR(255) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    KEY `idx_building_room` (`building`, `room_no`),
    KEY `idx_status` (`bed_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='床位表';

-- ============================================================
-- 4. 床位详情表
-- ============================================================
CREATE TABLE IF NOT EXISTS `bed_details` (
    `id`          INT          NOT NULL AUTO_INCREMENT COMMENT '主键',
    `customer_id` INT          DEFAULT NULL COMMENT '顾客ID',
    `bed_id`      INT          DEFAULT NULL COMMENT '床位ID',
    `start_date`  DATETIME     DEFAULT NULL COMMENT '床位起始日期',
    `end_date`    DATETIME     DEFAULT NULL COMMENT '床位结束日期',
    `bed_details` VARCHAR(255) DEFAULT NULL COMMENT '床位详情信息',
    `is_deleted`  TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0=显示, 1=隐藏',
    PRIMARY KEY (`id`),
    KEY `idx_customer` (`customer_id`),
    KEY `idx_bed` (`bed_id`),
    KEY `idx_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='床位详情表';

-- ============================================================
-- 5. 护理项目表
-- ============================================================
CREATE TABLE IF NOT EXISTS `nursing_item` (
    `id`          BIGINT         NOT NULL AUTO_INCREMENT COMMENT '主键',
    `code`        VARCHAR(50)    DEFAULT NULL COMMENT '项目编号',
    `name`        VARCHAR(100)   DEFAULT NULL COMMENT '项目名称',
    `price`       DECIMAL(10,2)  DEFAULT NULL COMMENT '价格',
    `status`      VARCHAR(20)    DEFAULT NULL COMMENT '状态: 启用/停用',
    `exec_period` VARCHAR(20)    DEFAULT NULL COMMENT '执行周期: 每日/每周',
    `exec_times`  INT            DEFAULT NULL COMMENT '周期内执行次数',
    `description` VARCHAR(500)   DEFAULT NULL COMMENT '描述',
    `is_deleted`  TINYINT(1)     NOT NULL DEFAULT 0 COMMENT '逻辑删除标记',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_code` (`code`),
    KEY `idx_status` (`status`),
    KEY `idx_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='护理项目表';

-- ============================================================
-- 6. 护理级别表
-- ============================================================
CREATE TABLE IF NOT EXISTS `nursing_level` (
    `id`         BIGINT      NOT NULL AUTO_INCREMENT COMMENT '主键',
    `level_name` VARCHAR(50) NOT NULL COMMENT '级别名称',
    `status`     VARCHAR(20) DEFAULT NULL COMMENT '状态: 启用/停用',
    `deleted`    TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '逻辑删除标记',
    PRIMARY KEY (`id`),
    KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='护理级别表';

-- ============================================================
-- 7. 护理级别-项目关联表
-- ============================================================
CREATE TABLE IF NOT EXISTS `nursing_level_item` (
    `id`       BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `level_id` BIGINT NOT NULL COMMENT '护理级别ID',
    `item_id`  BIGINT NOT NULL COMMENT '护理项目ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_level_item` (`level_id`, `item_id`),
    KEY `idx_item` (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='护理级别-项目关联表';

-- ============================================================
-- 8. 客户护理设置表
-- ============================================================
CREATE TABLE IF NOT EXISTS `client_nursing_setting` (
    `id`                 BIGINT      NOT NULL AUTO_INCREMENT COMMENT '主键',
    `client_id`          BIGINT      DEFAULT NULL COMMENT '客户ID',
    `nursing_item_id`    BIGINT      DEFAULT NULL COMMENT '护理项目ID',
    `nursing_level_id`   BIGINT      DEFAULT NULL COMMENT '护理级别ID',
    `purchase_date`      DATE        DEFAULT NULL COMMENT '购买服务日期',
    `total_quantity`     INT         DEFAULT NULL COMMENT '总购买数量',
    `remaining_quantity` INT         DEFAULT NULL COMMENT '剩余数量',
    `service_due_date`   DATE        DEFAULT NULL COMMENT '服务到期日期',
    `service_status`     VARCHAR(20) DEFAULT NULL COMMENT '服务状态: 正常/欠费/到期/将到期/用完',
    `is_deleted`         TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '逻辑删除标记',
    PRIMARY KEY (`id`),
    KEY `idx_client` (`client_id`),
    KEY `idx_item` (`nursing_item_id`),
    KEY `idx_status` (`service_status`),
    KEY `idx_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户护理设置表';

-- ============================================================
-- 9. 护理记录表
-- ============================================================
CREATE TABLE IF NOT EXISTS `nursing_record` (
    `id`                 BIGINT   NOT NULL AUTO_INCREMENT COMMENT '主键',
    `client_id`          BIGINT   DEFAULT NULL COMMENT '客户ID',
    `nursing_item_id`    BIGINT   DEFAULT NULL COMMENT '护理项目ID',
    `health_assistant_id` BIGINT  DEFAULT NULL COMMENT '健康管家ID',
    `nursing_time`       DATETIME DEFAULT NULL COMMENT '护理时间',
    `remarks`            VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `exec_quantity`      INT      NOT NULL DEFAULT 1 COMMENT '执行次数',
    `is_deleted`         TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除标记',
    PRIMARY KEY (`id`),
    KEY `idx_client` (`client_id`),
    KEY `idx_item` (`nursing_item_id`),
    KEY `idx_assistant` (`health_assistant_id`),
    KEY `idx_nursing_time` (`nursing_time`),
    KEY `idx_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='护理记录表';

-- ============================================================
-- 10. 外出申请表
-- ============================================================
CREATE TABLE IF NOT EXISTS `leave_application` (
    `id`                VARCHAR(50)  NOT NULL COMMENT '主键',
    `client_id`         INT          DEFAULT NULL COMMENT '客户ID',
    `client_name`       VARCHAR(50)  DEFAULT NULL COMMENT '客户姓名',
    `reason`            VARCHAR(255) DEFAULT NULL COMMENT '外出原因',
    `leave_time`        DATETIME     DEFAULT NULL COMMENT '外出时间',
    `predicted_end`     DATETIME     DEFAULT NULL COMMENT '预计返回时间',
    `actual_return_time` DATETIME    DEFAULT NULL COMMENT '实际返回时间',
    `statue`            VARCHAR(20)  NOT NULL DEFAULT '已提交' COMMENT '状态: 已提交/已审核/已返回',
    `auditor`           VARCHAR(50)  DEFAULT NULL COMMENT '审核人',
    `audit_time`        DATETIME     DEFAULT NULL COMMENT '审核时间',
    `deleted`           TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '逻辑删除标记',
    PRIMARY KEY (`id`),
    KEY `idx_client` (`client_id`),
    KEY `idx_statue` (`statue`),
    KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='外出申请表';

-- ============================================================
-- 11. 退住申请表
-- ============================================================
CREATE TABLE IF NOT EXISTS `check_out_application` (
    `id`          VARCHAR(50)  NOT NULL COMMENT '主键',
    `client_id`   INT          DEFAULT NULL COMMENT '客户ID',
    `client_name` VARCHAR(50)  DEFAULT NULL COMMENT '客户姓名',
    `type`        VARCHAR(20)  DEFAULT NULL COMMENT '退住类型',
    `reason`      VARCHAR(255) DEFAULT NULL COMMENT '退住原因',
    `time`        DATETIME     DEFAULT NULL COMMENT '申请时间',
    `statue`      VARCHAR(20)  NOT NULL DEFAULT '已提交' COMMENT '状态: 已提交/已审核/已拒绝',
    `auditor`     VARCHAR(50)  DEFAULT NULL COMMENT '审核人',
    `audit_time`  DATETIME     DEFAULT NULL COMMENT '审核时间',
    `deleted`     TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '逻辑删除标记',
    PRIMARY KEY (`id`),
    KEY `idx_client` (`client_id`),
    KEY `idx_statue` (`statue`),
    KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='退住申请表';

-- ============================================================
-- 初始数据
-- ============================================================

-- 管理员账号
INSERT INTO `operator` (`login_code`, `password`, `real_name`, `operator_type`, `deleted`) VALUES
('admin',  'admin',        'NEUSOFTadmin1', 'ADMIN',  0),
('admin1', 'admin1',       'NEUSOFTadmin2', 'ADMIN',  0),
('admin2', 'admin2',       'NEUSOFTadmin3', 'ADMIN',  0);

-- 床位数据（606楼基础床位）
INSERT INTO `bed` (`building`, `room_no`, `bed_no`, `bed_status`, `remarks`) VALUES
('606', 101, 'A01', 1, '双人间'),
('606', 101, 'A02', 1, '双人间'),
('606', 102, 'A01', 1, '双人间'),
('606', 102, 'A02', 1, '双人间'),
('606', 201, 'A01', 1, '单人间'),
('606', 201, 'A02', 1, '单人间'),
('606', 202, 'A01', 1, '单人间'),
('606', 202, 'A02', 1, '单人间'),
('606', 301, 'A01', 1, '豪华间'),
('606', 301, 'A02', 1, '豪华间'),
('606', 302, 'A01', 1, '豪华间'),
('606', 302, 'A02', 1, '豪华间');
