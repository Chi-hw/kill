package com.chw.kill.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


/**
 * @Author Chihw
 * @Description
 * @Date 2021/6/13 21:25
 */
@Configuration
public class RabbitMQConfig {
   /* //Headers模式
    public static final String QUEUE01="queue_headers01";
    public static final String QUEUE02="queue_headers02";
    public static final String EXCHANGE="headerExchange";
    @Bean
    public Queue queue01(){
        return new Queue(QUEUE01);
    }
    @Bean
    public Queue queue02(){
        return new Queue(QUEUE02);
    }
    @Bean
    public HeadersExchange headersExchange(){
        return new HeadersExchange(EXCHANGE);
    }
    @Bean
    public Binding binging01(){
        Map<String,Object> map=new HashMap<>();
        map.put("color","red");
        map.put("speed","low");
        return BindingBuilder.bind(queue01()).to(headersExchange()).whereAny(map).match();
    }
    @Bean
    public Binding binging02(){
        Map<String,Object> map=new HashMap<>();
        map.put("color","red");
        map.put("speed","fast");
        //必须同时匹配转发到同一队列
        return BindingBuilder.bind(queue02()).to(headersExchange()).whereAll(map).match();
    }
*/
  /*
    Topic模式

    public static final String QUEUE01="queue_topic01";
    public static final String QUEUE02="queue_topic02";
    public static final String EXCHANGE="topicExchange";
    public static final String ROUTINGKEY01="#.queue.#";
    public static final String ROUTINGKEY02="*.queue.#";
    @Bean
    public Queue queue01(){
        return new Queue(QUEUE01);
    }
    @Bean
    public Queue queue02(){
        return new Queue(QUEUE02);
    }
    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(EXCHANGE);
    }
    @Bean
    public Binding binging01(){
        return BindingBuilder.bind(queue01()).to(topicExchange()).with(ROUTINGKEY01);
    }
    @Bean
    public Binding binging02(){
        return BindingBuilder.bind(queue02()).to(topicExchange()).with(ROUTINGKEY02);
    }
    */
    /*


    //direct模式

    public static final String QUEUE01 = "queue_direct01";
    public static final String QUEUE02 = "queue_direct02";
    public static final String EXCHANGE = "directExchange";
    public static final String ROUTINGKEY01 = "queue.red";
    public static final String ROUTINGKEY02 = "queue.green";
    @Bean
    public Queue queue01(){
        return new Queue(QUEUE01);
    }
    @Bean
    public Queue queue02(){
        return new Queue(QUEUE02);
    }
    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(EXCHANGE);
    }
    @Bean
    public Binding binging01(){
        return BindingBuilder.bind(queue01()).to(directExchange()).with(ROUTINGKEY01);
    }
    @Bean
    public Binding binging02(){
        return BindingBuilder.bind(queue02()).to(directExchange()).with(ROUTINGKEY02);
    }*/

    /*


    //fanout模式

    private static final String QUEUE01="queue_fanout01";
    private static final String QUEUE02="queue_fanout02";
    private static final String EXCHANGE="fanoutExchange";


    *//**
     * @Description: 准备队列
     * @param: []
     * @return: org.springframework.amqp.core.Queue
     * @date: 2021/6/13 21:50
     *//*
    @Bean
    public Queue queue(){
        //消息要不要持久化，队列配置为持久化才可以使消息持久化
        return new Queue("queue",true);
    }


    @Bean
    public Queue queue01(){
        return new Queue(QUEUE01);
    }
    @Bean
    public Queue queue02(){
        return new Queue(QUEUE02);
    }
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(EXCHANGE);
    }

    //队列绑定到交换机
    @Bean
    public Binding binding01(){
        return BindingBuilder.bind(queue01()).to(fanoutExchange());
    }
    @Bean
    public Binding binding02(){
        return BindingBuilder.bind(queue02()).to(fanoutExchange());
    }*/
}
