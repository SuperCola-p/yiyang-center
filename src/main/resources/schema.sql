-- =====================================================
-- 东软颐养中心管理系统 - 数据库初始化脚本
-- 数据库: yiyang_center
-- MySQL 8.0+
-- =====================================================

CREATE DATABASE IF NOT EXISTS yiyang_center
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE yiyang_center;

-- ---------------------------------------------------
-- 1. 护理项目表 (nursing_item)
-- ---------------------------------------------------
CREATE TABLE IF NOT EXISTS nursing_item (
    id              BIGINT          NOT NULL AUTO_INCREMENT  COMMENT '主键ID',
    code            VARCHAR(50)     DEFAULT NULL             COMMENT '项目编码',
    name            VARCHAR(100)    DEFAULT NULL             COMMENT '项目名称',
    price           DECIMAL(10,2)   DEFAULT NULL             COMMENT '价格',
    status          VARCHAR(20)     DEFAULT NULL             COMMENT '状态(ENABLED/DISABLED)',
    exec_period     VARCHAR(20)     DEFAULT NULL             COMMENT '执行周期(DAILY/WEEKLY/MONTHLY)',
    exec_quantity   INT             DEFAULT 1               COMMENT '执行次数/数量',
    description     VARCHAR(500)    DEFAULT NULL             COMMENT '项目描述',
    is_deleted      TINYINT(1)      DEFAULT 0               COMMENT '逻辑删除标记(0-未删除,1-已删除)',
    PRIMARY KEY (id),
    UNIQUE KEY uk_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='护理项目表';

-- ---------------------------------------------------
-- 2. 护理等级表 (nursing_level)
-- ---------------------------------------------------
CREATE TABLE IF NOT EXISTS nursing_level (
    id              BIGINT          NOT NULL AUTO_INCREMENT  COMMENT '主键ID',
    level_name      VARCHAR(50)     NOT NULL                 COMMENT '等级名称',
    status          VARCHAR(20)     DEFAULT NULL             COMMENT '状态(ENABLED/DISABLED)',
    deleted         TINYINT(1)      DEFAULT 0               COMMENT '逻辑删除标记',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='护理等级表';

-- ---------------------------------------------------
-- 3. 护理等级-项目关联表 (nursing_level_item)
-- ---------------------------------------------------
CREATE TABLE IF NOT EXISTS nursing_level_item (
    id              BIGINT          NOT NULL AUTO_INCREMENT  COMMENT '主键ID',
    level_id        BIGINT          NOT NULL                 COMMENT '护理等级ID',
    item_id         BIGINT          NOT NULL                 COMMENT '护理项目ID',
    PRIMARY KEY (id),
    KEY idx_level_id (level_id),
    KEY idx_item_id (item_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='护理等级-项目关联表';

-- ---------------------------------------------------
-- 4. 床位表 (bed)
-- ---------------------------------------------------
CREATE TABLE IF NOT EXISTS bed (
    id              BIGINT          NOT NULL AUTO_INCREMENT  COMMENT '主键ID',
    building        VARCHAR(10)     DEFAULT NULL             COMMENT '楼栋',
    room_no         INT             DEFAULT NULL             COMMENT '房间号',
    bed_no          VARCHAR(10)     DEFAULT NULL             COMMENT '床位号',
    bed_status      INT             DEFAULT 1               COMMENT '床位状态(1-空闲,2-已入住,3-维修)',
    remarks         VARCHAR(255)    DEFAULT NULL             COMMENT '备注',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='床位表';

-- ---------------------------------------------------
-- 5. 床位入住记录表 (bed_details)
-- ---------------------------------------------------
CREATE TABLE IF NOT EXISTS bed_details (
    id              INT             NOT NULL AUTO_INCREMENT  COMMENT '主键ID',
    customer_id     INT             DEFAULT NULL             COMMENT '老人ID(client.id)',
    bed_id          INT             DEFAULT NULL             COMMENT '床位ID',
    start_date      DATETIME        DEFAULT NULL             COMMENT '入住开始时间',
    end_date        DATETIME        DEFAULT NULL             COMMENT '入住结束时间',
    bed_details     VARCHAR(255)    DEFAULT NULL             COMMENT '入住详情',
    is_deleted      TINYINT(1)      DEFAULT 0               COMMENT '逻辑删除标记',
    PRIMARY KEY (id),
    KEY idx_customer_id (customer_id),
    KEY idx_bed_id (bed_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='床位入住记录表';

-- ---------------------------------------------------
-- 6. 老人信息表 (client)
-- ---------------------------------------------------
CREATE TABLE IF NOT EXISTS client (
    id                      INT             NOT NULL AUTO_INCREMENT  COMMENT '主键ID',
    name                    VARCHAR(50)     DEFAULT NULL             COMMENT '姓名',
    age                     INT             DEFAULT NULL             COMMENT '年龄',
    gender                  VARCHAR(10)     DEFAULT NULL             COMMENT '性别(男/女)',
    blood_type              VARCHAR(10)     DEFAULT NULL             COMMENT '血型',
    phone                   VARCHAR(20)     DEFAULT NULL             COMMENT '联系电话',
    family_contact          VARCHAR(100)    DEFAULT NULL             COMMENT '家属联系方式',
    id_card                 VARCHAR(18)     DEFAULT NULL             COMMENT '身份证号',
    building_no             VARCHAR(10)     DEFAULT NULL             COMMENT '楼栋号',
    room_no                 VARCHAR(10)     DEFAULT NULL             COMMENT '房间号',
    bed_no                  VARCHAR(10)     DEFAULT NULL             COMMENT '床位号',
    birthday                DATE            DEFAULT NULL             COMMENT '出生日期',
    check_in_date           DATE            DEFAULT NULL             COMMENT '入住日期',
    contract_expire_date    DATE            DEFAULT NULL             COMMENT '合同到期日期',
    nursing_level           VARCHAR(50)     DEFAULT NULL             COMMENT '护理等级',
    nurse                   VARCHAR(50)     DEFAULT NULL             COMMENT '护工',
    health_status           VARCHAR(100)    DEFAULT NULL             COMMENT '健康状况',
    type                    VARCHAR(20)     DEFAULT NULL             COMMENT '类型(自理/半自理/全护理)',
    deleted                 TINYINT(1)      NOT NULL DEFAULT 0      COMMENT '逻辑删除标记',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='老人信息表';

-- ---------------------------------------------------
-- 7. 老人护理服务购买记录表 (client_nursing_setting)
-- ---------------------------------------------------
CREATE TABLE IF NOT EXISTS client_nursing_setting (
    id                  BIGINT          NOT NULL AUTO_INCREMENT  COMMENT '主键ID',
    client_id           BIGINT          DEFAULT NULL             COMMENT '老人ID',
    nursing_item_id     BIGINT          DEFAULT NULL             COMMENT '护理项目ID',
    nursing_level_id    BIGINT          DEFAULT NULL             COMMENT '护理等级ID',
    purchase_date       DATE            DEFAULT NULL             COMMENT '购买日期',
    total_quantity      INT             DEFAULT NULL             COMMENT '总次数',
    remaining_quantity  INT             DEFAULT NULL             COMMENT '剩余次数',
    service_due_date    DATE            DEFAULT NULL             COMMENT '服务到期日期',
    service_status      VARCHAR(20)     DEFAULT NULL             COMMENT '服务状态(ACTIVE/EXPIRED/SUSPENDED)',
    is_deleted          TINYINT(1)      DEFAULT 0               COMMENT '逻辑删除标记',
    PRIMARY KEY (id),
    KEY idx_client_id (client_id),
    KEY idx_nursing_item_id (nursing_item_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='老人护理服务购买记录表';

-- ---------------------------------------------------
-- 8. 护理执行记录表 (nursing_record)
-- ---------------------------------------------------
CREATE TABLE IF NOT EXISTS nursing_record (
    id                  BIGINT          NOT NULL AUTO_INCREMENT  COMMENT '主键ID',
    client_id           BIGINT          DEFAULT NULL             COMMENT '老人ID',
    nursing_item_id     BIGINT          DEFAULT NULL             COMMENT '护理项目ID',
    health_assistant_id BIGINT          DEFAULT NULL             COMMENT '护理员ID',
    nursing_time        DATETIME        DEFAULT NULL             COMMENT '护理执行时间',
    remarks             VARCHAR(500)    DEFAULT NULL             COMMENT '备注',
    exec_quantity       INT             DEFAULT 1               COMMENT '执行次数',
    is_deleted          TINYINT(1)      DEFAULT 0               COMMENT '逻辑删除标记',
    PRIMARY KEY (id),
    KEY idx_client_id (client_id),
    KEY idx_nursing_item_id (nursing_item_id),
    KEY idx_nursing_time (nursing_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='护理执行记录表';

-- ---------------------------------------------------
-- 9. 退住申请表 (check_out_application)
-- ---------------------------------------------------
CREATE TABLE IF NOT EXISTS check_out_application (
    id              VARCHAR(50)     NOT NULL                 COMMENT '申请编号(CO-开头)',
    client_id       INT             DEFAULT NULL             COMMENT '老人ID',
    client_name     VARCHAR(50)     DEFAULT NULL             COMMENT '老人姓名',
    type            VARCHAR(20)     DEFAULT NULL             COMMENT '退住类型(主动退住/违规劝退/转院)',
    reason          VARCHAR(255)    DEFAULT NULL             COMMENT '退住原因',
    time            DATETIME        DEFAULT NULL             COMMENT '申请时间',
    statue          VARCHAR(20)     DEFAULT '已提交'         COMMENT '状态(已提交/已通过/已拒绝)',
    auditor         VARCHAR(50)     DEFAULT NULL             COMMENT '审核人',
    audit_time      DATETIME        DEFAULT NULL             COMMENT '审核时间',
    deleted         TINYINT(1)      DEFAULT 0               COMMENT '逻辑删除标记',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='退住申请表';

-- ---------------------------------------------------
-- 10. 请假表 (leave_application)
-- ---------------------------------------------------
CREATE TABLE IF NOT EXISTS leave_application (
    id                  VARCHAR(50)     NOT NULL                 COMMENT '申请编号(LV-开头)',
    client_id           INT             DEFAULT NULL             COMMENT '老人ID',
    client_name         VARCHAR(50)     DEFAULT NULL             COMMENT '老人姓名',
    reason              VARCHAR(255)    DEFAULT NULL             COMMENT '请假原因',
    leave_time          DATETIME        DEFAULT NULL             COMMENT '请假时间',
    predicted_end       DATETIME        DEFAULT NULL             COMMENT '预计结束时间',
    actual_return_time  DATETIME        DEFAULT NULL             COMMENT '实际归院时间',
    statue              VARCHAR(20)     DEFAULT '已提交'         COMMENT '状态(已提交/已通过/已拒绝)',
    auditor             VARCHAR(50)     DEFAULT NULL             COMMENT '审核人',
    audit_time          DATETIME        DEFAULT NULL             COMMENT '审核时间',
    deleted             TINYINT(1)      DEFAULT 0               COMMENT '逻辑删除标记',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='请假表';

-- ---------------------------------------------------
-- 11. 操作员表 (operator)
-- ---------------------------------------------------
CREATE TABLE IF NOT EXISTS operator (
    login_code      VARCHAR(50)     NOT NULL                 COMMENT '登录账号(主键)',
    password        VARCHAR(100)    NOT NULL                 COMMENT '密码',
    real_name       VARCHAR(50)     NOT NULL                 COMMENT '真实姓名',
    operator_type   VARCHAR(20)     NOT NULL DEFAULT 'ADMIN' COMMENT '操作员类型(ADMIN/STAFF)',
    deleted         TINYINT(1)      NOT NULL DEFAULT 0      COMMENT '逻辑删除标记',
    PRIMARY KEY (login_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作员表';

-- ---------------------------------------------------
-- 初始数据
-- ---------------------------------------------------

-- 插入默认管理员账号 (密码 123456, BCrypt 加密)
-- 注意：此哈希由 Spring Security 的 BCryptPasswordEncoder 生成
INSERT INTO operator (login_code, password, real_name, operator_type, deleted)
VALUES ('admin', '$2a$10$cWMMibslCwCOKXVN3QbqkO1f8zN79U7aa/26DR2HeGflM0BEk1hjW', '系统管理员', 'ADMIN', 0)
ON DUPLICATE KEY UPDATE real_name = VALUES(real_name);
