package com.chw.kill.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Chihw
 * @Description 公共返回对象
 * @Date 2021/6/7 21:28
 */
@Data
@NoArgsConstructor //无参构造
@AllArgsConstructor //全参构造
public class RespBean {
    private long code;
    private String message;
    private Object obj;
    /**
     * @Description: 成功返回结果
     * @param: []
     * @return: com.chw.kill.result.RespBean
     * @date: 2021/6/7 21:53
     */
    public static RespBean success(){
        return new RespBean(RespBeanEnum.SUCCESS.getCode(),RespBeanEnum.SUCCESS.getMessage(),null);
    }
    /**
     * @Description: 方法重载，成功返回结果
     * @param: [obj]
     * @return: com.chw.kill.result.RespBean
     * @date: 2021/6/7 21:54
     */
    public static RespBean success(Object obj){
        return new RespBean(RespBeanEnum.SUCCESS.getCode(),RespBeanEnum.SUCCESS.getMessage(),obj);
    }
    /**
     * @Description: 失败返回结果
     * @param: [respBeanEnum]
     * @return: com.chw.kill.result.RespBean
     * @date: 2021/6/7 22:00
     */
    public static RespBean error(RespBeanEnum respBeanEnum){
        return new RespBean(respBeanEnum.getCode(),respBeanEnum.getMessage(),null);
    }
    /**
     * @Description: 失败返回结果
     * @param: [respBeanEnum, obj]
     * @return: com.chw.kill.result.RespBean
     * @date: 2021/6/7 22:00
     */
    public static RespBean error(RespBeanEnum respBeanEnum,Object obj){
        return new RespBean(respBeanEnum.getCode(),respBeanEnum.getMessage(),obj);
    }

}
