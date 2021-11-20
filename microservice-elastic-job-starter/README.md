
分布式定时任务组件starter

## 使用方法

1. 引入pom依赖

```xml
<dependency>
    <groupId>com.somnus</groupId>
    <artifactId>microservice-elastic-job-starter</artifactId>
    <version>${project.parent.version}</version>
</dependency>
```

2. 添加配置

```yaml
elastic:
  job:
      zk:
        namespace: elaticjob
        serverLists: zk1:2181,zk2:2181,zk3:2181
```

3. 开启注解

```java
package com.somnus.microservice;

import com.somnus.microservice.elasticjob.annotation.EnableElasticJob;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author somnus
 * @packageName com.somnus.microservice.provider.uac
 * @description: 用户服务中心启动类
 * @date 2021/10/25 13:43
 */
@EnableElasticJob
public class UacProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(UacProviderApplication.class, args);
    }

}

```
