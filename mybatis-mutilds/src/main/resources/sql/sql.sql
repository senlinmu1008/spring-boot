-- 主库
DROP TABLE IF EXISTS base_user;
CREATE TABLE base_user (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    user_name varchar(64) NOT NULL COMMENT '用户名',
    password varchar(16) NOT NULL COMMENT '用户密码',
    name varchar(64) COMMENT '用户姓名',
    birthday varchar(8) COMMENT '用户生日',
    address varchar(64) COMMENT '用户住址',
    mobile_phone varchar(16) COMMENT '手机号码',
    tel_phone varchar(16) COMMENT '固定电话',
    email varchar(16) COMMENT '邮箱',
    sex varchar(4) COMMENT '性别',
    type varchar(4) COMMENT '用户类型',
    status varchar(4) COMMENT '用户状态',
    description varchar(128) COMMENT '用户描述',
    crt_time datetime default NOW() COMMENT '创建时间',
    crt_user varchar(64) COMMENT '创建者用户名',
    crt_name varchar(64) COMMENT '创建者姓名',
    crt_host varchar(16) COMMENT '创建者ip',
    upd_time datetime default NOW() COMMENT '更新时间',
    upd_user varchar(64) COMMENT '更新者用户名',
    upd_name varchar(64) COMMENT '更新者姓名',
    upd_host varchar(16) COMMENT '更新者ip',
    PRIMARY KEY (id)
)COMMENT = '基础用户表';
ALTER TABLE base_user ADD UNIQUE (user_name);

-- 从库
DROP TABLE IF EXISTS auth_client_service;
CREATE TABLE auth_client_service (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    service_id varchar(16) NOT NULL COMMENT '服务id',
    client_id varchar(16) NOT NULL COMMENT '客户端id',
    description varchar(128) COMMENT '描述',
    crt_time datetime default NOW() COMMENT '创建时间',
    crt_user varchar(64) COMMENT '创建者用户名',
    crt_name varchar(64) COMMENT '创建者姓名',
    crt_host varchar(16) COMMENT '创建者ip',
    PRIMARY KEY (id)
)COMMENT = '客户端授权表';