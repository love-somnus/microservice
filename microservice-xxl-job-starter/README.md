
分布式定时任务组件starter

## 使用方法

1. 引入pom依赖

```xml
<dependency>
    <groupId>com.somnus</groupId>
    <artifactId>microservice-xxl-job-starter</artifactId>
    <version>${project.parent.version}</version>
</dependency>
```

2. 添加配置

```yaml
xxl:
  job:
    enable: true
    accessToken: default_token
    admin: #如调度中心集群部署存在多个地址则用逗号分隔。执行器将会使用该地址进行"执行器心跳注册"和"任务结果回调"；为空则关闭自动注册；
      addresses: http://192.168.95.13/xxl-job-admin
      username: admin
      password: 123456
    executor:
      address: ''
      appname: cloud-flower
      ip: ''
      logpath: /data/applogs/xxl-job/jobhandler
      logretentiondays: 7
      port: 9001
```

3. 开启注解

```java
package com.somnus.microservice;

import com.somnus.microservice.xxljob.annotation.EnableXxlJob;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @packageName com.somnus.microservice.provider.uac
 * @description: 用户服务中心启动类
 * @date 2021/10/25 13:43
 */
@EnableXxlJob
public class UacProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(UacProviderApplication.class, args);
    }

}

```
