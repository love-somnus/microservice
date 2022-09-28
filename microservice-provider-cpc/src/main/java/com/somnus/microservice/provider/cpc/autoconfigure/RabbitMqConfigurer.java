package com.somnus.microservice.provider.cpc.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.batch.BatchingStrategy;
import org.springframework.amqp.rabbit.batch.SimpleBatchingStrategy;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.BatchingRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * @author kevin.liu
 * @date 2022/9/28 19:48
 */
@Slf4j
@Configuration
public class RabbitMqConfigurer {

    @Bean
    public TaskScheduler batchQueueTaskScheduler(){
        return new ThreadPoolTaskScheduler();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory batchQueueRabbitListenerContainerFactory(CachingConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        //设置批量
        factory.setBatchListener(true);
        //设置BatchMessageListener生效
        factory.setConsumerBatchEnabled(true);
        //设置监听器一次批量处理的消息数量
        factory.setBatchSize(10000);
        //设置消费者的确认模式
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        //设置超时时间，等待多久后再次投递消息
        factory.setReceiveTimeout(1000L);
        return factory;
    }

    @Primary
    @Bean("singleQueueRabbitTemplate")
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            log.info("单个消息发送成功:correlationData({}),ack({}),cause({})", correlationData, ack, cause);
        });
        rabbitTemplate.setReturnsCallback(callback -> {
            log.info("批量消息丢失:exchange({}),route({}),replyCode({}),replyText({}),message:{}", callback.getExchange(), callback.getRoutingKey(), callback.getReplyCode(), callback.getReplyText(), callback.getMessage());
        });
        /* 不用默认的jdk序列化，采用jackson*/
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean("batchQueueRabbitTemplate")
    public BatchingRabbitTemplate batchQueueRabbitTemplate(CachingConnectionFactory connectionFactory, TaskScheduler taskScheduler){
        //!!!重点： 所谓批量， 就是spring 将多条message重新组成一条message, 发送到mq, 从mq接受到这条message后，在重新解析成多条message
        //一次批量的数量
        int batchSize = 10;
        // 缓存大小限制,单位字节，
        // simpleBatchingStrategy的策略，是判断message数量是否超过batchSize限制或者message的大小是否超过缓存限制，
        // 缓存限制，主要用于限制"组装后的一条消息的大小"
        // 如果主要通过数量来做批量("打包"成一条消息), 缓存设置大点
        // 详细逻辑请看simpleBatchingStrategy#addToBatch()//10KB
        int bufferLimit = 10240;
        long timeout = 1000;
        //注意，该策略只支持一个exchange/routingKey
        //A simple batching strategy that supports only one exchange/routingKey
        BatchingStrategy batchingStrategy = new SimpleBatchingStrategy(batchSize, bufferLimit, timeout);
        BatchingRabbitTemplate rabbitTemplate = new BatchingRabbitTemplate(connectionFactory, batchingStrategy, taskScheduler);
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            log.info("批量消息发送成功:correlationData({}),ack({}),cause({})", correlationData, ack, cause);
        });
        rabbitTemplate.setReturnsCallback(callback -> {
            log.info("批量消息丢失:exchange({}),route({}),replyCode({}),replyText({}),message:{}", callback.getExchange(), callback.getRoutingKey(), callback.getReplyCode(), callback.getReplyText(), callback.getMessage());
        });
        /* 不用默认的jdk序列化，采用jackson*/
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public TopicExchange onlineTopicExchange() {
        return ExchangeBuilder.topicExchange("topic.online.exchange").durable(true).build();
    }

    @Bean
    public Queue wsOnlineQueue() {
        return QueueBuilder.durable("ws.online").autoDelete().build();
    }

    @Bean
    public Queue wsNotifyQueue() {
        return QueueBuilder.durable("ws.notify").autoDelete().build();
    }

    @Bean
    public Queue wsPlaySongQueue() {
        return QueueBuilder.durable("ws.play.song").autoDelete().build();
    }

    /**
     * 横幅广播(消息订阅)
     */
    @Bean
    public Queue wsBannerQueue() {
        return QueueBuilder.durable("ws.banner").autoDelete().build();
    }

    @Bean
    public Queue wsOfflineQueue() {
        return QueueBuilder.durable("ws.offline").autoDelete().build();
    }

    @Bean
    public Binding bindingExchangeWsOnline(Queue wsOnlineQueue, TopicExchange onlineTopicExchange) {
        return BindingBuilder.bind(wsOnlineQueue).to(onlineTopicExchange).with("ws.online.*");
    }

    @Bean
    public Binding bindingExchangeWsPlaySong(Queue wsPlaySongQueue, TopicExchange onlineTopicExchange) {
        return BindingBuilder.bind(wsPlaySongQueue).to(onlineTopicExchange).with("ws.play.song.*");
    }

    @Bean
    public Binding bindingExchangeWsNotify(Queue wsNotifyQueue, TopicExchange onlineTopicExchange) {
        return BindingBuilder.bind(wsNotifyQueue).to(onlineTopicExchange).with("ws.notify.*");
    }

    @Bean
    public FanoutExchange bannerFanoutExchange() {
        return ExchangeBuilder.fanoutExchange("fanout.banner.exchange").durable(true).build();
    }
    @Bean
    public Binding bindingExchangeWsBanner(Queue wsBannerQueue, FanoutExchange bannerFanoutExchange) {
        return BindingBuilder.bind(wsBannerQueue).to(bannerFanoutExchange);
    }
    @Bean
    public FanoutExchange offlineFanoutExchange() {
        return ExchangeBuilder.fanoutExchange("fanout.offline.exchange").durable(true).build();
    }
    @Bean
    public Binding bindingExchangeOffline(Queue wsOfflineQueue, FanoutExchange offlineFanoutExchange) {
        return BindingBuilder.bind(wsOfflineQueue).to(offlineFanoutExchange);
    }

}
