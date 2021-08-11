package com.chw.kill.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author Chihw
 * @Description
 * @Date 2021/6/13 21:32
 */
@Controller
public class RabbitMQController {
    /*@Autowired
    private MQSender mqSender;

    *//**
     * @Description: 测试发送RabbitMQ消息
     * @param: []
     * @return: void
     * @date: 2021/6/13 21:33
     *//*
    @RequestMapping("/mq")
    @ResponseBody
    public void mq(){
        mqSender.send("Hello");
    }
    *//**
     * @Description: Fanout模式
     * @param: []
     * @return: void
     * @date: 2021/6/13 22:15
     *//*
    @RequestMapping("/mq/fanout")
    @ResponseBody
    public void mq01(){
        mqSender.send("Fanout Hello");
    }
    *//**
     * @Description: Direct模式
     * @param: []
     * @return: void
     * @date: 2021/6/13 22:41
     *//*
    @RequestMapping("/mq/direct01")
    @ResponseBody
    public void mq02(){
        mqSender.send01(" Hello,Red");
    }
    @RequestMapping("/mq/direct02")
    @ResponseBody
    public void mq03(){
        mqSender.send02(" Hello,Green");
    }
    *//**
     * @Description: topic模式
     * @param: []
     * @return: void
     * @date: 2021/6/13 23:02
     *//*
    @RequestMapping("/mq/topic01")
    @ResponseBody
    public void mq04(){
        mqSender.send03(" Hello,Red");
    }
    @RequestMapping("/mq/topic02")
    @ResponseBody
    public void mq05(){
        mqSender.send04(" Hello,Green");
    }

    @RequestMapping("/mq/headers01")
    @ResponseBody
    public void mq06(){
        mqSender.send05(" Hello,Header01");
    }
    @RequestMapping("/mq/headers02")
    @ResponseBody
    public void mq07(){
        mqSender.send06(" Hello,Header02");
    }
*/
}
