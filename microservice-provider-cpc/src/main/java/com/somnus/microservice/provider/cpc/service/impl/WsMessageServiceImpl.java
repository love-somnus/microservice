package com.somnus.microservice.provider.cpc.service.impl;

import com.somnus.microservice.commons.base.request.MessageBody;
import com.somnus.microservice.commons.core.support.BaseService;
import com.somnus.microservice.provider.cpc.model.domain.WsMessage;
import com.somnus.microservice.provider.cpc.service.WsMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.BatchingRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author kevin.liu
 * @date 2022/9/28 19:45
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class WsMessageServiceImpl extends BaseService<WsMessage> implements WsMessageService {

    private final RabbitTemplate template;

    private final BatchingRabbitTemplate batchTemplate;

    @Override
    public void p2p(MessageBody messageBody) {
        log.info("准备发送消息。。。。。。" + messageBody.getContent());

        WsMessage message = new WsMessage(messageBody.getSender(), messageBody.getReceiver(), messageBody.getContent());

        template.convertAndSend("topic.online.exchange","ws.notify." + messageBody.getReceiver(), messageBody.wrap(message.getId()), messages -> messages);

        /* 在线用户 */
        /*if(redisTemplate.opsForHash().entries("wt:bcgames:websocket:user:" + messageBody.getAppId()).containsKey(messageBody.getReceiver())){

            log.info("用户[{}]在线，消息推送开始>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>", messageBody.getReceiver());

            String sessionId = redisTemplate.opsForHash().get("wt:bcgames:websocket:user:" + messageBody.getAppId(), messageBody.getReceiver()).toString();

            mapper.insert(message.wrap(WsMessageType.ONLINE));

            // 调用 STOMP 代理进行消息转发
            template.convertAndSend("topic.online.exchange","p2p.online." + sessionId, messageBody.wrap(message.getId()), messages -> messages);
        }*/
        /* 离线用户 */
        /*else{
            log.info("用户[{}]不在线，消息推送到个人离线队列中>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>", messageBody.getReceiver());

            mapper.insert(message.wrap(ChatMessageType.OFFLINE));

            batchTemplate.convertAndSend("offline.exchange", "p2p.offline", messageBody.wrap(message.getId()));

        }*/
    }
}
