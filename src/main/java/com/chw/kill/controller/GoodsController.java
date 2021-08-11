package com.chw.kill.controller;

import com.chw.kill.domain.User;
import com.chw.kill.result.RespBean;
import com.chw.kill.service.IGoodsService;
import com.chw.kill.service.IUserService;
import com.chw.kill.vo.DetailVo;
import com.chw.kill.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @Author Chihw
 * @Description
 * @Date 2021/6/8 22:00
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired   //做手动渲染Thrmeleaf模板
    private ThymeleafViewResolver thymeleafViewResolver;


    /**
     * @Description: 商品列表页，进行页面缓存
     * windows优化前吞吐量：1039.1
     * windows优化后使用缓存吞吐量：2124.5
     * @param: [model, user, request, response]
     * @return: java.lang.String
     * @date: 2021/6/12 20:57
     */
    @RequestMapping(value="/toList",produces = "text/html;charset=utf-8")
    @ResponseBody   //返回相应对象
    public String toList(Model model,User user,HttpServletRequest request,HttpServletResponse response){
        model.addAttribute("user",user);
        model.addAttribute("goodsList",goodsService.findGoodsVo());
        //Redis中获取页面，如果不为空，直接返回页面
        ValueOperations valueOperations=redisTemplate.opsForValue();
        String html= (String) valueOperations.get("goodsList");
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        //如果为空，手动渲染，存入Redis并返回
        WebContext webContext=new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
        //获取引擎，获取模板名称，页面内容。手动渲染页面。
        html=thymeleafViewResolver.getTemplateEngine().process("goodsList",webContext);
        if(!StringUtils.isEmpty(html)){
            valueOperations.set("goodsList",html,60, TimeUnit.SECONDS);
        }
        //return "goodsList";
        return html;
    }
    /**
     * @Description: 商品详情页，进行URL缓存
     * @param: [model, user, goodsId, request, response]
     * @return: java.lang.String
     * @date: 2021/6/12 21:41
     */
    @RequestMapping(value = "toDetail/{goodsId}",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toDetail(Model model,User user, @PathVariable Long goodsId,HttpServletRequest request,HttpServletResponse response){
        model.addAttribute("user",user);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        Date startDate =goodsVo.getStartDate();
        Date endDate=goodsVo.getEndDate();
        Date nowDate=new Date();
        System.out.println(nowDate);
        int killState=0;
        int remainSeconds;  //还有多长时间开始秒杀
        if(nowDate.before(startDate)){
            remainSeconds= (int) ((startDate.getTime()-nowDate.getTime())/1000);
        }else if(nowDate.after(endDate)){
            killState=2;
            remainSeconds=-1;
        }else{
            killState=1;
            remainSeconds=0;

        }
        model.addAttribute("goods",goodsVo);
        model.addAttribute("killState",killState);
        model.addAttribute("remainSeconds",remainSeconds);

        ValueOperations valueOperations=redisTemplate.opsForValue();
        //从Redis中获取页面，如果不为空，直接返回页面
        String html= (String) valueOperations.get("goodsDetail:"+goodsId);
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        WebContext webContext=new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
        html=thymeleafViewResolver.getTemplateEngine().process("goodsDetail",webContext);
        if(!StringUtils.isEmpty(html)){
            valueOperations.set("goodsDetail:"+goodsId,html,60,TimeUnit.SECONDS);
        }
        //return "goodsDetail";
        return html;
    }

    @RequestMapping("detail/{goodsId}")
    @ResponseBody
    public RespBean detail(User user, @PathVariable Long goodsId){
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        Date startDate =goodsVo.getStartDate();
        Date endDate=goodsVo.getEndDate();
        Date nowDate=new Date();
        System.out.println(nowDate);
        int killState=0;
        int remainSeconds;  //还有多长时间开始秒杀
        if(nowDate.before(startDate)){
            remainSeconds= (int) ((startDate.getTime()-nowDate.getTime())/1000);
        }else if(nowDate.after(endDate)){
            killState=2;
            remainSeconds=-1;
        }else{
            killState=1;
            remainSeconds=0;
        }
        DetailVo detailVo=new DetailVo();
        detailVo.setUser(user);
        detailVo.setGoodsVo(goodsVo);
        detailVo.setKillStatus(killState);
        detailVo.setRemainSeconds(remainSeconds);
        return RespBean.success(detailVo);
    }

    /* *
     * @Description: 商品列表页
     * @param: [session, model, ticket]
     * @return: java.lang.String
     * @date: 2021/6/8 22:09
     */
    /*@RequestMapping("/toList")
    public String toList(HttpServletRequest request, HttpServletResponse response, Model model, @CookieValue("userticket") String ticket){
        if(StringUtils.isEmpty(ticket)){
            return "login";
        }
        //User user = (User) session.getAttribute(ticket);
        User user=userService.getUserByCookie(ticket,request,response);
        System.out.println(user);
        if(null==user){
            return "login";
        }
        model.addAttribute("user",user);
        return "goodsList";
    }*/

     /* *
     * @Description: 商品列表页
     * @param: [model, user]
     * @return: java.lang.String
     * @date: 2021/6/9 22:25*/
   /* @RequestMapping("/toList")
    public String toList(Model model,User user,HttpServletRequest request,HttpServletResponse response){
        model.addAttribute("user",user);
        model.addAttribute("goodsList",goodsService.findGoodsVo());
        return "goodsList";
    }*/

    /*@RequestMapping("toDetail/{goodsId}")
    public String toDetail(Model model,User user, @PathVariable Long goodsId){
        model.addAttribute("user",user);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        Date startDate =goodsVo.getStartDate();
        Date endDate=goodsVo.getEndDate();
        Date nowDate=new Date();
        System.out.println(nowDate);
        int killState=0;
        int remainSeconds;  //还有多长时间开始秒杀
        if(nowDate.before(startDate)){
            remainSeconds= (int) ((startDate.getTime()-nowDate.getTime())/1000);
        }else if(nowDate.after(endDate)){
            killState=2;
            remainSeconds=-1;
        }else{
            killState=1;
            remainSeconds=0;

        }
        model.addAttribute("goods",goodsVo);
        model.addAttribute("killState",killState);
        model.addAttribute("remainSeconds",remainSeconds);
        return "goodsDetail";
    }*/
}
