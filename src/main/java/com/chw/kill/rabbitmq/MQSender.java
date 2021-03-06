/*
package com.chw.kill.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


*/
/**
 * @Author Chihw
 * @Description 消息发送者
 * @Date 2021/6/13 21:28
 *//*

@Service
@Slf4j
public class MQSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    */
/**
     * @Description: 消息生产者
     * @param: [msg]
     * @return: void
     * @date: 2021/6/13 21:51
     *//*

    public void send(Object msg){
        log.info("发送消息"+msg);
        //rabbitTemplate.convertAndSend("queue",msg);
        rabbitTemplate.convertAndSend("fanoutExchange","queuetest",msg);
    }

    public void send01(Object msg){
        log.info("发送red消息"+msg);
        rabbitTemplate.convertAndSend("directExchange","queue.red",msg);
    }
    public void send02(Object msg){
        log.info("发送green消息"+msg);
        rabbitTemplate.convertAndSend("directExchange","queue.green",msg);
    }
    public void send03(Object msg){
        log.info("发送消息(QUEUE01接收)"+msg);
        rabbitTemplate.convertAndSend("topicExchange","queue.red.message",msg);
    }
    public void send04(Object msg){
        log.info("发送消息(被两个QUEUE接收)"+msg);
        rabbitTemplate.convertAndSend("topicExchange","message.queue.green.abc",msg);
    }

    public void send05(String msg){
        log.info("发送消息(被两个QUEUE接收)"+msg);
        MessageProperties properties=new MessageProperties();
        properties.setHeader("color","red");
        properties.setHeader("speed","fast");
        Message message=new Message(msg.getBytes(),properties);
        rabbitTemplate.convertAndSend("headersExchange","",message);
    }
    public void send06(String msg){
        log.info("发送消息(被QUEUE01接收)"+msg);
        MessageProperties properties=new MessageProperties();
        properties.setHeader("color","red");
        properties.setHeader("speed","normal");
        Message message=new Message(msg.getBytes(),properties);
        rabbitTemplate.convertAndSend("headersExchange","",message);
    }

}
*/
