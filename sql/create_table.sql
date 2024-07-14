-- 创建库
create database if not exists api;

-- 切换库
use api;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userProfile  varchar(512)                           null comment '用户简介',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除'
) comment '用户' collate = utf8mb4_unicode_ci;

-- 接口信息表
create table if not exists interface_info
(
    id             bigint                             not null auto_increment primary key comment 'id',
    name           varchar(256)                       not null comment '名称',
    description    text                               null comment '描述',
    url            varchar(512)                       not null comment '接口地址',
    requestParams  text                               not null comment '请求参数',
    requestHeader  text                               null comment '请求头',
    responseHeader text                               null comment '响应头',
    status         int      default 0                 not null comment '接口状态：0-关闭，1-开启',
    method         varchar(256)                       not null comment '请求类型',
    userId         bigint                             not null comment '创建人',
    createTime     datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete       tinyint  default 0                 not null comment '是否删除'
) comment '接口信息';

create table if not exists api_key
(
    id         bigint                             not null auto_increment primary key comment 'id',
    accessKey  varchar(512)                       not null comment 'accessKey',
    secretKey  varchar(512)                       not null comment 'secretKey',
    userId     bigint                             not null comment '创建人',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isAbandon  tinyint  default 0                 not null comment '是否弃用'
) comment 'API签名密钥表';