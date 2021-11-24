## 介绍
1.  缓存注解既可以加在接口上，也可以加在实现类上，也可以加在没有接口只有类的情形下
   - 1.1 注解说明
       - 1)注解`com.somnus.microservice.cache.annotation.Cacheable`，新增缓存
       - 2)注解`com.somnus.microservice.cache.annotation.CachePut`，更新缓存
       - 3)注解`com.somnus.microservice.cache.annotation.CacheEvict`，清除缓存
   - 1.2 参数说明
       - 1)`name` 缓存的名字
       - 2)`key` 缓存Key。缓存Key的完整路径是`prefix + "_" + name + "_" + key`，prefix为config.propertie里的namespace值
       - 3)`expire` 过期时间，一旦过期，缓存数据自动会从Redis删除（只用于`Cacheable`和`CachePut`）
       - 4)`allEntries` 是否全部清除缓存内容（只用于`CacheEvict`）。如果为true，按照`prefix + "_" + name + "*"`方式去匹配删除Key；如果为false，则按照`prefix + "_" + name + "_" + key + "*"`
       - 5)`beforeInvocation` 缓存清理是在方法调用前还是调用后（只用于`CacheEvict`）
2. 缓存的Key在config-redis.xml中有个`RedisCacheEntity`的`prefix`(前缀)全局配置项目，它和name，key组成一个SPEL语义，即`[prefix]_[name]_[key]`，该值将作为Redis的Key存储，对应的Redis的Value就是缓存
3. 对于方法返回的值为null的时候，不做任何缓存相关操作；对于方法执行过程中抛出异常后，不做任何缓存相关操作

## 切换缓存类型
```java
# Prefix
prefix=netease
# Cache config
# cache.enabled=true
cache.type=redisCache
# 当切面拦截出现异常，如果忽略该异常，则不影响当前业务方法调用，否则中断当前业务方法调用，缺省为true
# cache.aop.exception.ignore=true
# 全局缓存过期值，单位毫秒（小于等于零，表示永不过期），当注解上没配置该值的时候，以全局值为准，缺省为-1
# cache.expire=-1
# 扫描含有@Cacheable，@CacheEvict，@CachePut等注解的接口或者类所在目录
cache.scan.packages=com.somnus.microservice.provider.cmc.service
```
## 示例
```java
public interface MyService5 {
    @Cacheable(name = "cache", key = "#id1 + \"-\" + #id2", expire = -1L)
    String doA(String id1, String id2);

    @CachePut(name = "cache", key = "#id1 + \"-\" + #id2", expire = -1L)
    String doB(String id1, String id2);

    @CacheEvict(name = "cache", key = "#id1 + \"-\" + #id2", allEntries = false, beforeInvocation = false)
    String doC(String id1, String id2);
}
```

```java
@Service("myService6Impl")
public class MyService6Impl {
    private static final Logger LOG = LoggerFactory.getLogger(MyService6Impl.class);

    @Cacheable(name = "cache", key = "#id1 + \"-\" + #id2", expire = 60000L)
    public String doD(String id1, String id2) {
        try {
            TimeUnit.MILLISECONDS.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        LOG.info("doD");

        return "D";
    }

    @CachePut(name = "cache", key = "#id1 + \"-\" + #id2", expire = 60000L)
    public String doE(String id1, String id2) {
        try {
            TimeUnit.MILLISECONDS.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        LOG.info("doE");

        return "E";
    }

    @CacheEvict(name = "cache", key = "#id1 + \"-\" + #id2", allEntries = false, beforeInvocation = false)
    public String doF(String id1, String id2) {
        try {
            TimeUnit.MILLISECONDS.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        LOG.info("doF");

        return "F";
    }
}
```

```java
@SpringBootApplication
@EnableCache
@ComponentScan(basePackages = { "com.somnus.microservice.provider.cmc.service" })
public class CacheAopApplication {
    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(CacheAopApplication.class, args);

        // 下面步骤请一步步操作，然后结合Redis Desktop Manager等工具查看效果
        MyService5 myService5 = applicationContext.getBean(MyService5.class);

        // 新增缓存Key为M-N，Value为A到Redis
        myService5.doA("M", "N");

        // 更新缓存Key为M-N，Value为B到Redis
        // myService5.doB("M", "N");

        // 清除缓存Key为M-N到Redis
        // myService5.doC("M", "N");

        MyService6Impl myService6 = applicationContext.getBean(MyService6Impl.class);

        // 新增缓存Key为X-Y，Value为D到Redis
        myService6.doD("X", "Y");

        // 更新缓存Key为X-Y，Value为E到Redis
        //myService6.doE("X", "Y");

        // 清除缓存Key为X-Y到Redis
        // myService6.doF("X", "Y");
    }
}
```