package com.chw.kill;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
/**
 * @Description: Redis分布式锁测试实现
 * @param:
 * @return:
 * @date: 2021/6/18 20:10
 */
@SpringBootTest
public class KillApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisScript<Boolean> script;

    @Test
    public  void Test01() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //占位，如果k不存在可以设置成功
        Boolean isLock=valueOperations.setIfAbsent("k1","v1");
        //如果占位成功，进行正常操作
        if(isLock){
            valueOperations.set("name","xxx");
            String name= (String) valueOperations.get("name");
            System.out.println("name=" +name);
            //Integer.parseInt("xxx");  //抛出异常则删锁失败，线程被占用,解决方法：设置过期时间
            //操作结束，删除锁
            //redisTemplate.delete("k1");
        }else{
            System.out.println("线程正在使用");
        }
    }
    @Test
    public void Test02() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //添加过期时间，防止应用在运行过程中抛出异常导致锁无法释放
        Boolean isLock=valueOperations.setIfAbsent("k1","v1",5, TimeUnit.SECONDS);
        //可以获取锁，但是会造成其他影响，可能删除下一个线程的锁，导致紊乱
        if(isLock){
            valueOperations.set("name","xxx");
            String name= (String) valueOperations.get("name");
            System.out.println("name=" +name);
            Integer.parseInt("xxx");  //会抛异常
            //操作结束，删除锁
            //redisTemplate.delete("k1");
        }else{
            System.out.println("线程正在使用");
        }
    }

    //所以给value一个随机值，先获取到锁，再判断锁的值是否一致。一致才删除
    //这三个操作不是原子性的，所以使用lua脚本实现，同时也减少了网络传输。两种写法：1.通过服务器写好，不好修改2.通过java传输，增加网络消耗

    @Test
    public void Test03() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String value= UUID.randomUUID().toString();
        Boolean isLock=valueOperations.setIfAbsent("k1",value,5, TimeUnit.SECONDS);
        if(isLock){
            valueOperations.set("name","xxx");
            String name= (String) valueOperations.get("name");
            System.out.println("name=" +name);
            System.out.println(valueOperations.get("k1"));
            Boolean result=(Boolean) redisTemplate.execute(script, Collections.singletonList("k1"),value);
            System.out.println(result);
        }else{
            System.out.println("线程正在使用");
        }
    }

}
