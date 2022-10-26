package com.somnus.microservice.provider.cpc.web.listener;

import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareBatchMessageListener;

import java.util.List;

/**
 * @author kevin.liu
 * @date 2022/9/20 20:49
 */
@Slf4j
@RequiredArgsConstructor
public class BatchNotifyQueueListener implements ChannelAwareBatchMessageListener {

    @Override
    @SneakyThrows
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "ws.notify"),
            exchange = @Exchange(name = "topic.online.exchange", type = ExchangeTypes.TOPIC),
            key = "ws.notify.*"
    ), containerFactory = "batchQueueRabbitListenerContainerFactory")
    public void onMessageBatch(List<Message> messages, Channel channel) {

        for (Message message : messages){

            String body = new String(message.getBody());

            MessageProperties properties = message.getMessageProperties();

            log.info("消息[ws.notify]->队列DeliveryTag[{}]->消息内容[{}]已经被消费", properties.getDeliveryTag(), body);

            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }
}

