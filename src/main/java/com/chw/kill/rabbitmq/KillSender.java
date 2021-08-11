package com.chw.kill.rabbitmq;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * @Author Chihw
 * @Description
 * @Date 2021/6/17 20:23
 */
@Service
@Slf4j
public class KillSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * @Description: 发送秒杀信息
     * @param: [message]
     * @return: void
     * @date: 2021/6/17 20:25
     */
    public void sendKillMessage(String message){
        log.info("发送消息："+message);
        rabbitTemplate.convertAndSend("killExchange","kill.message",message);
    }
}
