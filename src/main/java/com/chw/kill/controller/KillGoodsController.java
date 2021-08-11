package com.chw.kill.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chw.kill.config.AccessLimit;
import com.chw.kill.domain.KillOrder;
import com.chw.kill.domain.Order;
import com.chw.kill.domain.User;
import com.chw.kill.exception.GlobalException;
import com.chw.kill.rabbitmq.KillSender;
import com.chw.kill.result.RespBean;
import com.chw.kill.result.RespBeanEnum;
import com.chw.kill.service.IGoodsService;
import com.chw.kill.service.IKillOrderService;
import com.chw.kill.utils.JsonUtil;
import com.chw.kill.vo.GoodsVo;
import com.chw.kill.vo.KillVo;
import com.rabbitmq.tools.json.JSONUtil;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author chw
 * @since 2021-06-10
 */
@Slf4j
@Controller
@RequestMapping("/killGoods")
public class KillGoodsController implements InitializingBean {

    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private IKillOrderService killOrderService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private KillSender killSender;
    @Autowired
    private RedisScript<Long> script;

    private Map<Long,Boolean> EmptyStockMap=new HashMap<>();

    /**
     * @Description: 秒杀  Kill大写，是页面静态化之前
     * windows秒杀前QPS=785
     * @param: [model, user, goodsId]
     * @return: java.lang.String
     * @date: 2021/6/10 20:58
     */
  /*  @RequestMapping("doKill")
    public String doKill(Model model, User user,Long goodsId){
        if(user==null){
            return "login";
        }
        model.addAttribute("user",user);
        GoodsVo goods=goodsService.findGoodsVoByGoodsId(goodsId);
        //判断库存
        if(goods.getStockCount()< 1 ){
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
            return "doKillFail";
        }
        //判断是否重复抢购
        //Mybatis-plus写法
        KillOrder killOrder=killOrderService.getOne(new QueryWrapper<KillOrder>().eq("user_id",user.getId()).eq("goods_id",goodsId));
        System.out.println(killOrder);
        if(killOrder!=null){
            model.addAttribute("errmsg",RespBeanEnum.REPEATE_ERROR.getMessage());
            return "doKillFail";
        }
        Order order=killOrderService.doKill(user,goods);
        model.addAttribute("order",order);
        model.addAttribute("goods",goods);
        return "orderDetail";
    }*/


    /**
     * @Description: 秒杀静态化
     * @param: [model, user, goodsId]
     * @return: com.chw.kill.result.RespBean
     * @date: 2021/6/13 11:04
     */
    /*@RequestMapping(value="dokill",method = RequestMethod.POST)
    @ResponseBody
    public RespBean dokill( User user, Long goodsId){
        if(user==null){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        GoodsVo goods=goodsService.findGoodsVoByGoodsId(goodsId);
        //判断库存
        if(goods.getStockCount()< 1 ){
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        //判断是否重复抢购
        //Mybatis-plus写法
        //KillOrder killOrder=killOrderService.getOne(new QueryWrapper<KillOrder>().eq("user_id",user.getId()).eq("goods_id",goodsId));
        KillOrder killOrder= (KillOrder) redisTemplate.opsForValue().get("order:"+user.getId()+":"+goodsId);
        System.out.println(killOrder);
        if(killOrder!=null){
            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }
        Order order=killOrderService.doKill(user,goods);
        return RespBean.success(order);
    }*/

    /**
     * @Description: Redis预减库存
     * @param: [user, goodsId]
     * @return: com.chw.kill.result.RespBean
     * @date: 2021/6/17 19:58
     */
    @RequestMapping(value="/{path}/dokill",method = RequestMethod.POST)
    @ResponseBody
    public RespBean dokill(@PathVariable String path, User user, Long goodsId){
        if(user==null){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        ValueOperations valueOperations=redisTemplate.opsForValue();
        boolean check=killOrderService.checkPath(user,goodsId,path);
        if (!check){
            return RespBean.error(RespBeanEnum.REQUEST_ILLEGAL);
        }
        //判断是否重复抢购
         KillOrder killOrder= (KillOrder) redisTemplate.opsForValue().get("order"+user.getId()+":"+goodsId);
         if (killOrder!=null){
             return  RespBean.error(RespBeanEnum.REPEATE_ERROR);
         }
         //内存标记，减少Redis的访问
         if(EmptyStockMap.get(goodsId)){
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
         }
         //预减库存     redis中的递减，原子性
         //Long stock= valueOperations.decrement("killGoods:"+goodsId);
        Long stock= (Long) redisTemplate.execute(script, Collections.singletonList("killGoods:"+goodsId),Collections.EMPTY_LIST);
         if(stock<0){
             EmptyStockMap.put(goodsId,true);
             valueOperations.increment("killGoods:"+goodsId);  //使库存变为0
             return RespBean.error(RespBeanEnum.EMPTY_STOCK);
         }
        KillVo killVo = new KillVo(user, goodsId);
        killSender.sendKillMessage(JsonUtil.objectToJson(killVo));
        return RespBean.success(0);
    }
    /**
     * @Description: 初始化时会执行的方法
     *
     * 系统初始化，把商品数量加载到Redis
     * @param: []
     * @return: void
     * @date: 2021/6/17 19:59
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> list=goodsService.findGoodsVo();
        if(CollectionUtils.isEmpty(list)){ //判断list是否未空
            return;
        }
        //不为空存入redis
        list.forEach(goodsVo -> {
            redisTemplate.opsForValue().set("killGoods:"+goodsVo.getId(),goodsVo.getStockCount());
            EmptyStockMap.put(goodsVo.getId(),false);
                }
        );
    }
    /**
     * @Description: 获取秒杀结果
     * -1 表示失败   0表示排队中
     * @param: [user, goodsId]
     * @return: com.chw.kill.result.RespBean
     * @date: 2021/6/17 21:38
     */
    @RequestMapping(value = "/result",method = RequestMethod.GET)
    @ResponseBody
    public RespBean getResult(User user,Long goodsId){
        if(user==null){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        Long orderId=killOrderService.getResult(user,goodsId);
        return RespBean.success(orderId);
    }

    /**
     * @Description: 获取秒杀地址
     * @param: [user, path]
     * @return: com.chw.kill.result.RespBean
     * @date: 2021/6/18 21:23
     */
    @AccessLimit(second=5,maxCount=5,needLogin=true) //通用接口限流
    @RequestMapping(value = "/path",method = RequestMethod.GET)
    @ResponseBody
    public RespBean path(User user, Long goodsId, String captcha, HttpServletRequest request){
        if (user==null){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        //简单接口限流
        /*ValueOperations valueOperations=redisTemplate.opsForValue();
        //限制访问次数。5秒内访问5次
        String uri=request.getRequestURI();
        captcha="0"; //不用算
        Integer count= (Integer) valueOperations.get(uri+":"+user.getId());
        if(count==null){
            valueOperations.set(uri+":"+user.getId(),1,5,TimeUnit.SECONDS);
        }else if(count<5){
            valueOperations.increment(uri+":"+user.getId());
        }else {
            return RespBean.error(RespBeanEnum.ACCESS_LIMIT_REAHCED);
        }*/
        //判断验证码
        boolean check=killOrderService.checkCaptcha(user,goodsId,captcha);
        if(!check){
            return RespBean.error(RespBeanEnum.ERROR_CAPTCHA);
        }
        KillOrder killOrder= (KillOrder) redisTemplate.opsForValue().get("order:"+user.getId()+":"+goodsId);
        if (killOrder!=null){
            return  RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }
        String str=killOrderService.createPath(user,goodsId);
        return RespBean.success(str);
    }

    @RequestMapping(value = "/captcha",method = RequestMethod.GET)
    public void verifyCode(User user, Long goodsId, HttpServletResponse response){
        if(user==null || goodsId<0){
            throw new GlobalException(RespBeanEnum.REQUEST_ILLEGAL);
        }
        //设置请求头为输出图片的类型
        response.setContentType("image/jpg");
        response.setHeader("Pargam","No-cache");
        response.setDateHeader("Expires",0);
        //生成验证码，将结果放入Redis
        ArithmeticCaptcha captcha=new ArithmeticCaptcha(130,32,3);
        redisTemplate.opsForValue().set("captcha:"+user.getId()+":"+goodsId,captcha.text(),300, TimeUnit.SECONDS);
        try {
            captcha.out(response.getOutputStream());
        } catch (IOException e) {
            log.info("验证码生成失败",e.getMessage());
        }
    }

}
