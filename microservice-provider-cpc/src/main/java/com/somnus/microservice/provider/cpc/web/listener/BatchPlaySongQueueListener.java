package com.somnus.microservice.provider.cpc.web.listener;

import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareBatchMessageListener;

import java.util.List;

/**
 * @author kevin.liu
 * @date 2022/9/23 9:28
 */
@Slf4j
@RequiredArgsConstructor
public class BatchPlaySongQueueListener implements ChannelAwareBatchMessageListener {

    @Override
    @SneakyThrows
    @RabbitListener(queues = "ws.play.song", containerFactory = "batchQueueRabbitListenerContainerFactory")
    public void onMessageBatch(List<Message> messages, Channel channel) {

        for (Message message : messages){

            String body = new String(message.getBody());

            MessageProperties properties = message.getMessageProperties();

            log.info("消息[ws.play.song]->队列DeliveryTag[{}]->消息内容[{}]已经被消费", properties.getDeliveryTag(), body);

            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }
}

