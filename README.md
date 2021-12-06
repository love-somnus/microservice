# 1. 介绍

- [x] 本项目可以当做如何构建微服务的示例项目，你应该创建哪些module，当然我这个不是标准答案，
      但肯定是相对合理的（由于module很多，笔者不想创建若干个仓库，所以全部放入当前仓库中，在此建议您真实项目中不要照葫芦画瓢，还是一个个的去创建仓库去吧），
- [x] 本项目会给出项目中常用的一些脚手架，并封装成starter，方便开箱即用（曾经有小白问：为什么不直接封装成工具类，统一放入到一个jar呢，引入静态类，就可以用了。(⊙o⊙)… 少年，多多读点书去吧）
- [x] 本项目使用当前最新稳定版本的SpringCloud 2020.0.4 和适配最新的SpringBoot 2.4.6（当前日期2021-11-11）
- [x] 本项目使用目前业界推崇的spring-cloud-alibaba 实现微服务，使用版本为当前最稳定的2021.1（当前日期2021-11-11）
- [x] 本项目使nacos选择了最新的2.0.3，nacos中的配置文件，下面会粘贴出来（当前日期2021-11-11）
- [x] 本项目因为是示例项目，所以只创建一个服务提供者cpc，一个服务消费者uac（真实线上项目，没有严格意义的某个module是什么角色，它们可能互为提供者和消费者）
- [x] 本项目网关gateway服务已实现接口token认证(实现方式会在readme介绍)
- [x] 本项目oauth2服务为资源授权服务(用于获取token，可使用三种授权模式)，
      使用了最新的[spring-security-oauth2-authorization-server](https://github.com/spring-projects/spring-authorization-server)
      具体如何使用会在oauth2中介绍
- [x] 其它，由于时间仓促，更新会比较慢，请谅解

# 2. starter模块

- [x] [分布式缓存](https://github.com/love-somnus/microservice/blob/main/microservice-cache/README.md) microservice-cache-starter
- [x] [分布式限流](https://github.com/love-somnus/microservice/blob/main/microservice-limit/README.md) microservice-limit-starter
- [x] [分布式锁](https://github.com/love-somnus/microservice/blob/main/microservice-lock/README.md) microservice-lock-starter
- [x] [分布式定时任务](https://github.com/love-somnus/microservice/tree/main/microservice-elastic-job-starter#readme) microservice-elastic-job-starter

   > starter效果演示demo，放在cpc服务中

# 3. 配置文件

项目采用了[redisson](https://github.com/redisson/redisson/wiki/%E7%9B%AE%E5%BD%95)，由于采用了配置分离，故也把redisson.yaml文件也放入了nacos中，URL路径如下：

```yaml
http://ip:8848/nacos/v1/cs/configs?dataId=redisson.yaml&group=DEFAULT_GROUP
```

## 3.1 redisson.yaml(单机)

```yaml
clusterServersConfig:
  idleConnectionTimeout: 10000
  connectTimeout: 10000
  timeout: 3000
  retryAttempts: 3
  retryInterval: 1500
  failedSlaveReconnectionInterval: 3000
  failedSlaveCheckInterval: 60000
  password: null
  subscriptionsPerConnection: 5
  clientName: null
  loadBalancer: !<org.redisson.connection.balancer.RoundRobinLoadBalancer> {}
  subscriptionConnectionMinimumIdleSize: 1
  subscriptionConnectionPoolSize: 50
  slaveConnectionMinimumIdleSize: 24
  slaveConnectionPoolSize: 64
  masterConnectionMinimumIdleSize: 24
  masterConnectionPoolSize: 64
  readMode: "SLAVE"
  subscriptionMode: "SLAVE"
  nodeAddresses:
    - "redis://192.168.95.31:6379"
    - "redis://192.168.95.32:6379"
    - "redis://192.168.95.33:6379"
    - "redis://192.168.95.36:6379"
    - "redis://192.168.95.37:6379"
    - "redis://192.168.95.38:6379"
  scanInterval: 1000
  pingConnectionInterval: 0
  keepAlive: false
  tcpNoDelay: false
threads: 16
nettyThreads: 32
codec: !<org.redisson.codec.JsonJacksonCodec> {}
transportMode: "NIO"
```

## 3.2 redisson.yaml(集群)

```yaml
singleServerConfig:
  idleConnectionTimeout: 10000
  pingTimeout: 1000
  connectTimeout: 10000
  timeout: 3000
  retryAttempts: 3
  retryInterval: 1500
  reconnectionTimeout: 3000
  failedAttempts: 3
  password: 123456
  subscriptionsPerConnection: 5
  clientName: null
  address: "redis://127.0.0.1:6379"
  subscriptionConnectionMinimumIdleSize: 1
  subscriptionConnectionPoolSize: 50
  connectionMinimumIdleSize: 32
  connectionPoolSize: 64
  database: 0
  dnsMonitoringInterval: 5000
threads: 16
nettyThreads: 32
codec: !<org.redisson.codec.JsonJacksonCodec> {}
transportMode: "NIO"
```

# 4. 测试

- 网关swagger

        ```URL
        http://192.168.97.101:8000/swagger-ui/index.html
        ```

## 4.1 oauth2创建用户

```url
http://192.168.97.101:8002/auth/user/save
```

[![QQ-20211206104944.png](https://i.postimg.cc/BbQwBL24/QQ-20211206104944.png)](https://postimg.cc/jwmXsj8F)

4.2 oauth2获取授权码

- ##### [授权码流程（Authorization Code）](https://github.com/love-somnus/microservice/wiki/Spring-Authorization-Server介绍和使用)

- ##### [凭证式（Client Credentials）](https://github.com/love-somnus/microservice/wiki/Spring-Authorization-Server介绍和使用)
