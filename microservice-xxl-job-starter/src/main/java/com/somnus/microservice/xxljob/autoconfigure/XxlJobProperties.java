package com.somnus.microservice.xxljob.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author kevin.liu
 * @title: XxlJobProperties
 * @projectName neshpub
 * @description: TODO
 * @date 2022/8/26 16:08
 */
@Data
@ConfigurationProperties(prefix = "xxl.job")
public class XxlJobProperties {

    /**
     *  执行器通讯TOKEN [选填]：非空时启用；
     */
    private String accessToken;

    /**
     * 是否启用分布式定时任务
     */
    private boolean enable = false;

    private Admin admin;

    private Executor executor;

    @Data
    public static class Admin{
        /**
         * 调度中心部署根地址 [选填]：如调度中心集群部署存在多个地址则用逗号分隔。执行器将会使用该地址进行"执行器心跳注册"和"任务结果回调"；为空则关闭自动注册；
         */
        private String addresses;
        /**
         * 登陆用户名
         */
        private String username = "admin";
        /**
         * 登陆密码
         */
        private String password = "123456";
        /**
         * 超时时间
         */
        private Integer connectionTimeOut = 5000;
    }

    @Data
    public static class Executor{
        /**
         * 执行器注册 [选填]：优先使用该配置作为注册地址，为空时使用内嵌服务 ”IP:PORT“ 作为注册地址。从而更灵活的支持容器类型执行器动态IP和动态映射端口问题。
         */
        private String address;
        /**
         * 执行器AppName [选填]：执行器心跳注册分组依据；为空则关闭自动注册
         */
        private String appname;
        /**
         * 执行器IP [选填]：默认为空表示自动获取IP，多网卡时可手动设置指定IP，该IP不会绑定Host仅作为通讯实用；地址信息用于 "执行器注册" 和 "调度中心请求并触发任务"；
         */
        private String ip;
        /**
         * 执行器运行日志文件存储磁盘路径 [选填] ：需要对该路径拥有读写权限；为空则使用默认路径；
         */
        private String logpath;
        /**
         * 执行器日志文件保存天数 [选填] ： 过期日志自动清理, 限制值大于等于3时生效; 否则, 如-1, 关闭自动清理功能；
         */
        private Integer logretentiondays;
        /**
         * 执行器端口号 [选填]：小于等于0则自动获取；默认端口为9999，单机部署多个执行器时，注意要配置不同执行器端口；
         */
        private Integer port;
    }
}
