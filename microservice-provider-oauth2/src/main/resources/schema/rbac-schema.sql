CREATE TABLE `rbac_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` longtext NOT NULL COMMENT '密码，加密存储',
  `realname` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '真实姓名',
  `status` tinyint(10) NOT NULL DEFAULT '0' COMMENT '状态（0:启用 1:禁用）',
  `creator` varchar(255) DEFAULT NULL COMMENT '创建人',
  `creator_id` int(11) DEFAULT NULL COMMENT '创建人ID',
  `created_time` datetime NOT NULL COMMENT '创建时间',
  `last_operator` varchar(255) DEFAULT NULL COMMENT '最近操作人',
  `last_operator_id` int(11) DEFAULT NULL COMMENT '最后操作人ID',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `username` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户表'