package com.chw.kill.config;


import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * @Author Chihw
 * @Description 秒杀消息队列配置类
 * @Date 2021/6/17 20:19
 */
@Configuration
public class KillRabbitMQConfig {
    public static final String QUEUE="killQueue";
    public static final String EXCHANGE="killExchange";

    @Bean
    public Queue queue(){
        return new Queue(QUEUE);
    }

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(EXCHANGE);
    }
    @Bean
    public Binding binding(){
        return BindingBuilder.bind(queue()).to(topicExchange()).with("kill.#");
    }

}
