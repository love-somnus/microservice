# 介绍

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

# starter模块

- [x] 分布式缓存 microservice-cache-starter
- [x] [分布式限流](https://github.com/love-somnus/microservice/blob/main/microservice-limit/README.md) microservice-limit-starter
- [x] [分布式锁](https://github.com/love-somnus/microservice/blob/main/microservice-lock/README.md) microservice-lock-starter
- [x] [分布式定时任务](https://github.com/love-somnus/microservice/tree/main/microservice-elastic-job-starter#readme) microservice-elastic-job-starter

> oauth2-local.yaml

```yaml
server:
  port: 8002

feign:
  hystrix:
    enabled: true
  sentinel:
    enabled: true
  client:
    config:
      default:
        connectTimeout: 100000
        readTimeout: 100000

reactive:
  feign:
    loadbalancer:
      enabled: true
    circuit:
      breaker:
        enabled: false

spring:
  cloud:
    nacos:
      discovery:
        server-addr: 192.168.95.41:8848
    sentinel:
      transport:
        port: 8719
        dashboard: 192.168.95.41:8080
  datasource:
    type: com.zaxxer.hikari.HikariDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://192.168.95.18:3306/oauth2?setUnicode=true&characterEncoding=utf8&useSSL=false&&serverTimezone=Asia/Shanghai
    username: root
    password: 123456
    hikari:
      minimum-idle: 5
      idle-timeout: 600000
      maximum-pool-size: 10
      auto-commit: true
      pool-name: MyHikariCP
      max-lifetime: 1800000
      connection-timeout: 300000
      connection-test-query: SELECT 1

mybatis:
  type-aliases-package: com.somnus.microservice.provider.oauth2.model.domain
  mapper-locations: classpath:mapper/*.xml

pagehelper:
  helperDialect: mysql
  reasonable: true
  supportMethodsArguments: true
  params: count=countSql

dubbo:
  application:
    name: oauth2
  registry:
    address: nacos://192.168.95.41:8848
  protocol:
    name: dubbo
    port: -1
    serialization: kryo
  scan:
    basePackages: com.somnus.microservice.provider.oauth2.web.rpc

management:
  endpoints:
    web:
      exposure:
        include: "*"

logging:
  config: classpath:logback-spring.xml
#logstash:
#  kafka:
#    bootstrap-servers: 192.168.95.10:9092,192.168.95.11:9092,192.168.95.12:9092
#    topic: wt-uac-test

```


> uac-local.yaml

```yaml
server:
  port: 8001

feign:
  hystrix:
    enabled: true
  sentinel:
    enabled: true
  client:
    config:
      default:
        connectTimeout: 100000
        readTimeout: 100000

reactive:
  feign:
    loadbalancer:
      enabled: true
    circuit:
      breaker:
        enabled: false

spring:
  cloud:
    nacos:
      discovery:
        server-addr: 192.168.95.41:8848
    sentinel:
      transport:
        port: 8719
        dashboard: 192.168.95.41:8080
  datasource:
    type: com.zaxxer.hikari.HikariDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://192.168.95.18:3306/uac?setUnicode=true&characterEncoding=utf8&useSSL=false&&serverTimezone=Asia/Shanghai
    username: root
    password: 123456
    hikari:
      minimum-idle: 5
      idle-timeout: 600000
      maximum-pool-size: 10
      auto-commit: true
      pool-name: MyHikariCP
      max-lifetime: 1800000
      connection-timeout: 300000
      connection-test-query: SELECT 1

  redis:
    timeout: 5000
    cluster:
      max-redirects: 5
      nodes:
      - 192.168.95.31:6379
      - 192.168.95.32:6379
      - 192.168.95.33:6379
      - 192.168.95.36:6379
      - 192.168.95.37:6379
      - 192.168.95.38:6379
    database: 0
    jedis:
      pool:
        max-active: 8 #连接池最大连接数（使用负值表示没有限制） 默认 8
        max-idle: 8 #连接池中的最大空闲连接 默认 8
        max-wait: -1 #连接池最大阻塞等待时间（使用负值表示没有限制） 默认 -1
        min-idle: 0 #连接池中的最小空闲连接 默认 0   

mybatis:
  type-aliases-package: com.somnus.microservice.provider.uac.model.domain
  mapper-locations: classpath:mapper/*.xml

pagehelper:
  helperDialect: mysql
  reasonable: true
  supportMethodsArguments: true
  params: count=countSql

dubbo:
  application:
    name: uac
  registry:
    address: nacos://192.168.95.41:8848
  protocol:
    name: dubbo
    port: -1
    serialization: kryo
  scan:
    basePackages: com.somnus.microservice.provider.uac.web.rpc

management:
  endpoints:
    web:
      exposure:
        include: "*"

logging:
  config: classpath:logback-spring.xml
#logstash:
#  kafka:
#    bootstrap-servers: 192.168.95.10:9092,192.168.95.11:9092,192.168.95.12:9092
#    topic: wt-uac-test

elastic:
  job:
      zk:
        namespace: elasticjob
        serverLists: 192.168.95.33:2181,192.168.95.33:2182,192.168.95.33:2183
jwt:
  token:
    expired: 12
    refresh:
      expired: 12
```
> gateway-local.yaml

```yaml
server:
  port: 8000

spring:
  security:
    oauth2:
      resourceserver:
        jwt: #配置RSA的公钥访问地址
          jwk-set-uri: http://localhost:8002/oauth2/jwks 
  cloud:
    nacos:
      discovery:
        server-addr: 192.168.95.41:8848
    gateway:
      globalcors:
        corsConfigurations:
          '[/**]':
            allowedOrigins: "*"
            allowedMethods:
              - GET
              - POST      
      discovery:
        locator:
          enabled: true
      default-filters:
        - StripPrefix=1
      routes:
        - id: uac
          uri: lb://uac
          filters:
            - name: Retry
              args:
                retries: 3
                statuses: BAD_GATEWAY
            - name: RequestRateLimiter
              args:
                redis-rate-limiter.replenishRate: 10
                redis-rate-limiter.burstCapacity: 20
                key-resolver: "#{@ipKeyResolver}"
          predicates:
            - Method=GET,POST
            - Path=/uac/**
        - id: cpc
          uri: lb://cpc
          filters:
            - name: Retry
              args:
                retries: 3
                statuses: BAD_GATEWAY
            - name: RequestRateLimiter
              args:
                redis-rate-limiter.replenishRate: 10
                redis-rate-limiter.burstCapacity: 20
                key-resolver: "#{@ipKeyResolver}"
          predicates:
            - Method=GET,POST
            - Path=/cpc/**
        - id: oauth2
          uri: lb://oauth2
          filters:
            - name: Retry
              args:
                retries: 3
                statuses: BAD_GATEWAY
            - name: RequestRateLimiter
              args:
                redis-rate-limiter.replenishRate: 10
                redis-rate-limiter.burstCapacity: 20
                key-resolver: "#{@ipKeyResolver}"
          predicates:
            - Method=GET,POST
            - Path=/oauth2/**
  redis:
    timeout: 5000
    cluster:
      max-redirects: 5
      nodes:
      - 192.168.95.31:6379
      - 192.168.95.32:6379
      - 192.168.95.33:6379
      - 192.168.95.36:6379
      - 192.168.95.37:6379
      - 192.168.95.38:6379
    database: 0
    jedis:
      pool:
        max-active: 8 #连接池最大连接数（使用负值表示没有限制） 默认 8
        max-idle: 8 #连接池中的最大空闲连接 默认 8
        max-wait: -1 #连接池最大阻塞等待时间（使用负值表示没有限制） 默认 -1
        min-idle: 0 #连接池中的最小空闲连接 默认 0    
  
```
