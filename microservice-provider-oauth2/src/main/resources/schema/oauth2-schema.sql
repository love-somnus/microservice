
CREATE TABLE oauth2_registered_client
(
    id                            varchar(100)                        NOT NULL,
    client_id                     varchar(100)                        NOT NULL,
    client_id_issued_at           timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    client_secret                 varchar(200)                        NULL,
    client_secret_expires_at      timestamp                           NULL,
    client_name                   varchar(200)                        NOT NULL,
    client_authentication_methods varchar(1000)                       NOT NULL,
    authorization_grant_types     varchar(1000)                       NOT NULL,
    redirect_uris                 varchar(1000)                       NULL,
    scopes                        varchar(1000)                       NOT NULL,
    client_settings               varchar(2000)                       NOT NULL,
    token_settings                varchar(2000)                       NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE oauth2_authorization
(
    id                            varchar(100)  NOT NULL,
    registered_client_id          varchar(100)  NOT NULL,
    principal_name                varchar(200)  NOT NULL,
    authorization_grant_type      varchar(100)  NOT NULL,
    attributes                    varchar(4000) NULL,
    state                         varchar(500)  NULL,
    authorization_code_value      blob          NULL,
    `authorization_code_issued_at`  timestamp     NULL,
    authorization_code_expires_at timestamp     NULL,
    authorization_code_metadata   varchar(2000) NULL,
    access_token_value            blob          NULL,
    access_token_issued_at        timestamp     NULL,
    access_token_expires_at       timestamp     NULL,
    access_token_metadata         varchar(2000) NULL,
    access_token_type             varchar(100)  NULL,
    access_token_scopes           varchar(1000) NULL,
    oidc_id_token_value           blob          NULL,
    oidc_id_token_issued_at       timestamp     NULL,
    oidc_id_token_expires_at      timestamp     NULL,
    oidc_id_token_metadata        varchar(2000) NULL,
    refresh_token_value           blob          NULL,
    refresh_token_issued_at       timestamp     NULL,
    refresh_token_expires_at      timestamp     NULL,
    refresh_token_metadata        varchar(2000) NULL,
    PRIMARY KEY (id)
);

CREATE TABLE oauth2_authorization_consent
(
    registered_client_id varchar(100)  NOT NULL,
    principal_name       varchar(200)  NOT NULL,
    authorities          varchar(1000) NOT NULL,
    PRIMARY KEY (registered_client_id, principal_name)
);

/*
SQLyog Ultimate v12.08 (64 bit)
MySQL - 5.7.23-log
*********************************************************************
*/
/*!40101 SET NAMES utf8 */;

CREATE TABLE `oauth2_client_details` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `client_id` varchar(255) DEFAULT NULL COMMENT '客户端ID',
    `client_secret` longtext COMMENT '客户端密钥',
    `resource_ids` varchar(255) DEFAULT NULL COMMENT '资源ID',
    `scopes` longtext COMMENT '作用域',
    `client_authentication_methods` longtext COMMENT '授权方法',
    `authorization_grant_types` longtext COMMENT '授权模式（A,B,C）',
    `redirect_uris` longtext COMMENT '回调地址',
    `authorities` varchar(255) DEFAULT NULL COMMENT '权限',
    `access_token_validity` bigint(20) DEFAULT NULL COMMENT '请求令牌有效时间',
    `refresh_token_validity` bigint(20) DEFAULT NULL COMMENT '刷新令牌有效时间',
    `additional_information` longtext COMMENT '扩展信息',
    `auto_approve` varchar(32) DEFAULT NULL COMMENT '是否自动放行',
    `creator` varchar(255) DEFAULT NULL COMMENT '创建人',
    `creator_id` int(11) DEFAULT NULL COMMENT '创建人ID',
    `created_time` datetime NOT NULL COMMENT '创建时间',
    `last_operator` varchar(255) DEFAULT NULL COMMENT '最近操作人',
    `last_operator_id` int(11) DEFAULT NULL COMMENT '最后操作人ID',
    `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='客户端信息表'
insert into `oauth2_client_details` (`id`, `client_id`, `client_secret`, `resource_ids`, `scopes`, `client_authentication_methods`, `authorization_grant_types`, `redirect_uris`, `authorities`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `autoapprove`, `creator`, `creator_id`, `created_time`, `last_operator`, `last_operator_id`, `update_time`) values('1','client','{bcrypt}$2a$10$al2no469Z7P4SnPOeDkExux3mfNgpHd5VuAmxwT4ZXjeFOAc8Hwxy',NULL,'openid,message.read,message.write','client_secret_post,client_secret_basic','refresh_token,client_credentials,password,authorization_code,sms','https://www.baidu.com',NULL,'12','12',NULL,'true','admin','0','2022-06-15 11:51:51',NULL,NULL,'2022-06-15 16:26:20');
insert into `oauth2_client_details` (`id`, `client_id`, `client_secret`, `resource_ids`, `scopes`, `client_authentication_methods`, `authorization_grant_types`, `redirect_uris`, `authorities`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `autoapprove`, `creator`, `creator_id`, `created_time`, `last_operator`, `last_operator_id`, `update_time`) values('2','sms','{bcrypt}$2a$10$al2no469Z7P4SnPOeDkExux3mfNgpHd5VuAmxwT4ZXjeFOAc8Hwxy',NULL,'openid,message.read,message.write','client_secret_post,client_secret_basic','refresh_token,client_credentials,password,authorization_code,sms','https://www.baidu.com',NULL,'12','12',NULL,'true','admin','0','2022-06-15 18:49:15',NULL,NULL,'2022-06-15 18:51:05');
