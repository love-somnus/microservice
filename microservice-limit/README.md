## 介绍

- 支持若干个分布式系统对同一资源在给定的时间段里最多的访问限制次数(超出次数返回false)；等下个时间段开始，才允许再次被访问(返回true)，周而复始；也支持本地多线程访问的限流
- 支持两种调用方式，`注解方式`和`直接调用`
- 参数说明
    - `name` 资源的名字
    - `key` 资源Key。资源Key的完整路径是`prefix + "" + name + "" + key`，`prefix`为config.propertie里的`namespace`值
    - `limitPeriod` 给定的时间段(单位为秒)
    - `limitCount` 最多的访问限制次数（注意，如果是Guava方式本地限流，`limitCount`必须等于1，因为Guava的机制是设置每秒访问次数）
    - `restrictIp` 是否限定ip（默认限定）

## 示例

### 注解方式

```java
public interface MyService7 {
    @Limit(name = "limit", key = "#id1 + \"-\" + #id2", limitPeriod = 10, limitCount = 5)
    String doA(String id1, String id2);
}

@Service("myService8Impl")
public class MyService8Impl {
    private static final Logger LOG = LoggerFactory.getLogger(MyService8Impl.class);

    @Limit(name = "limit", key = "#id1 + \"-\" + #id2", limitPeriod = 10, limitCount = 5)
    public String doB(String id1, String id2) {
        LOG.info("doB");

        return "B";
    }
}

@SpringBootApplication
@EnableLimit
public class LimitAopApplication {
    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(LimitAopApplication.class, args);

        MyService7 myService7 = applicationContext.getBean(MyService7.class);
        Timer timer1 = new Timer();
        timer1.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        myService7.doA("X", "Y");
                    }

                }).start();
            }
        }, 0L, 3000L);

        MyService8Impl myService8 = applicationContext.getBean(MyService8Impl.class);
        Timer timer2 = new Timer();
        timer2.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        myService8.doB("X", "Y");
                    }

                }).start();
            }
        }, 0L, 4000L);
    }
}
```

### 直接调用方式

```java
@SpringBootApplication
@EnableLimit
public class LimitApplication {
    private static final Logger LOG = LoggerFactory.getLogger(LimitApplication.class);

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(LimitApplication.class, args);

        // 在给定的10秒里最多访问5次(超出次数返回false)；等下个10秒开始，才允许再次被访问(返回true)，周而复始
        LimitExecutor limitExecutor = applicationContext.getBean(LimitExecutor.class);

        Timer timer1 = new Timer();
        timer1.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                for (int i = 0; i < 3; i++) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                LOG.info("Timer1 - Limit={}", limitExecutor.tryAccess("limit", "X-Y", 10, 5));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        }, 0L, 1000L);

        Timer timer2 = new Timer();
        timer2.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                for (int i = 0; i < 3; i++) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                LOG.info("Timer1 - Limit={}", limitExecutor.tryAccess("limit", "X-Y", 10, 5));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        }, 0L, 1500L);
    }
}

```