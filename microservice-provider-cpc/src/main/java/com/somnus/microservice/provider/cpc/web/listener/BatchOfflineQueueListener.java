package com.somnus.microservice.provider.cpc.web.listener;

import com.fasterxml.jackson.core.type.TypeReference;
import com.rabbitmq.client.Channel;
import com.somnus.microservice.commons.base.request.MessageBody;
import com.somnus.microservice.commons.base.utils.JacksonUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareBatchMessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;
import java.util.Map;

/**
 * @author kevin.liu
 * @date 2022/9/19 15:24
 */
@Slf4j
@RequiredArgsConstructor
public class BatchOfflineQueueListener implements ChannelAwareBatchMessageListener {

    private final RabbitTemplate template;

    private final StringRedisTemplate stringRedisTemplate;


    @Override
    @SneakyThrows
    @RabbitListener(queues = "ws.offline", containerFactory = "batchQueueRabbitListenerContainerFactory")
    public void onMessageBatch(List<Message> messages, Channel channel) {
        log.info("离线消息 收到{}条message", messages.size());
        /*
         * deliveryTag：表示消息投递序号。
         * multiple：是否批量失败确认。
         * requeue：值为 true 消息将重新入队列。
         */
        for (Message message : messages) {

            String body = new String(message.getBody());

            MessageBody messageBody = JacksonUtil.parseJson(body, new TypeReference<MessageBody>() {});

            MessageProperties properties = message.getMessageProperties();

            Map<Object, Object> map = stringRedisTemplate.opsForHash().entries("wt:bcgames:websocket:user:");

            if (map.containsKey(messageBody.getReceiver())) {
                // 调用 STOMP 代理进行消息转发
                log.info("推送离线DeliveryTag[{}]消息, 内容为[{}]", properties.getDeliveryTag(), body);
                template.convertAndSend("online.exchange", "ws.online." + map.get(messageBody.getReceiver()), messageBody);
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } else {
                //放回队列
                log.warn("放回队列DeliveryTag[{}]消息, 内容为[{}]", properties.getDeliveryTag(), body);
                /*
                 * deliveryTag：表示消息投递序号。
                 * multiple：是否批量失败确认。
                 * requeue：值为 true 消息将重新入队列。
                 */
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), true, true);
            }

        }
    }
}
