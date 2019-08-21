package com.winston.utils.service;

import com.alibaba.fastjson.JSON;
import com.winston.utils.redis.KeyPrefix;
import com.winston.utils.redis.SessionRedisKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author weigaosheng
 * @description
 * @CalssName RedisService
 * @date 2019/3/1
 * @params
 * @return
 */
@Service
public class RedisService {

    @Autowired
    private JedisPool jedisPool;

    /**
     * @return a
     * @Author weigaosheng
     * @Description 根据key获取相应的值并转换成对应的bean对象返回
     * @Date 14:59 2019/3/
     * @Param
     **/
    public <T> T get(KeyPrefix prefix, String key, Class<T> clazz){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            // 生成真正的key
            String realKey = prefix.getPrefix() + key;
            String str = jedis.get(realKey);
            return stringToBean(str, clazz);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            returnToPool(jedis);
        }
    }

    public <T> T get(String key, Class<T> clazz){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            // 生成真正的key
            String str = jedis.get(key);
            return stringToBean(str, clazz);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            returnToPool(jedis);
        }
    }

    /***
     * 获取实时在线人数
     * @return
     */
    public int getPersonCount(){
        return get(SessionRedisKey.sessionKey,"session",Integer.class);
    }

    /**
     * @return a
     * @Author weigaosheng
     * @Description 设置key-value，如果时间为0，就直接设置永不过期
     * @Date 14:58 2019/3/
     * @Param
     **/
    public <T> boolean set(KeyPrefix prefix, String key, T value){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String str = beanToString(value);
            if(str == null || str.length() <= 0){
                return false;
            }
            // 生成真正的key
            String realKey = prefix.getPrefix() + key;
            int seconds = prefix.expireSeconds();
            if(seconds <= 0){
                jedis.set(realKey, str);
            }else{
                jedis.setex(realKey, seconds, str);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            returnToPool(jedis);
        }
    }


    /**
     * @return a
     * @Author weigaosheng
     * @Description 重设过期时间时长
     * @Date 14:54 2019/3/
     * @Param
     **/
    public <T> boolean retExpire(KeyPrefix prefix, String key, int second, T value){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String str = beanToString(value);
            // 生成真正的key
            String realKey = prefix.getPrefix() + key;
            jedis.setex(realKey, second, str);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * @return a
     * @Author weigaosheng
     * @Description 判断key是否存在
     * @Date 14:54 2019/3/
     * @Param
     **/
    public <T> boolean exists(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            // 生成真正的key
            String realKey = prefix.getPrefix() + key;
            return jedis.exists(realKey);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            returnToPool(jedis);
        }
    }
    public <T> boolean exists( String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return jedis.exists(key);
        }catch (Exception e){
            return false;
        }finally {
            returnToPool(jedis);
        }
    }
    /**
     * @return a
     * @Author weigaosheng
     * @Description 删除
     * @Date 14:54 2019/3/
     * @Param
     **/
    public boolean del(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            // 生成真正的key
            String realKey = prefix.getPrefix() + key;
            long ret = jedis.del(realKey);
            return ret>0;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * @return a
     * @Author weigaosheng
     * @Description 删除
     * @Date 14:54 2019/3/
     * @Param
     **/
    public boolean del(String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            long ret = jedis.del(key);
            return ret>0;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            returnToPool(jedis);
        }
    }
    /**
     * @return a
     * @Author weigaosheng
     * @Description 增加值，比如key1-122，执行命令incr key1 后，值变成123
     * @Date 15:03 2019/3/4
    * @Param
     **/
    public <T> Long incr(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            // 生成真正的key
            String realKey = prefix.getPrefix() + key;
            return jedis.incr(realKey);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * @return a
     * @Author weigaosheng
     * @Description 减少值，比如key1-122，执行命令decr key1 后，值变成121
     * @Date 15:03 2019/3/
     * @Param
     **/
    public <T> Long decr(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            // 生成真正的key
            String realKey = prefix.getPrefix() + key;
            return jedis.decr(realKey);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * @return a
     * @Author weigaosheng
     * @Description 把json对象转换成字符串
     * @Date 9:58 2019/3/
     * @Param
     **/
    public static <T> String beanToString(T value) {
        if(value == null){
            return null;
        }
        Class<?> clazz = value.getClass();
        if(clazz == int.class || clazz == Integer.class){
            return "" + value;
        }else if(clazz == String.class){
            return (String) value;
        }else if(clazz == long.class || clazz == Long.class){
            return ""+ value;
        }else{
            return JSON.toJSONString(value);
        }
    }

    /**
     * @return a
     * @Author weigaosheng
     * @Description 把string类型的数据转换成对象
     * @Date 10:25 2019/3/
     * @Param
     **/
    @SuppressWarnings("unchecked")
    public static <T> T stringToBean(String string, Class<T> clazz) {
        if(StringUtils.isEmpty(string) || clazz == null){
            return null;
        }
        if(clazz == int.class || clazz == Integer.class){
            return (T) Integer.valueOf(string);
        }else if(clazz == String.class){
            return (T) string;
        }else if(clazz == long.class || clazz == Long.class){
            return (T) Long.valueOf(string);
        }else{
            return JSON.toJavaObject(JSON.parseObject(string), clazz);
        }
    }

    private void returnToPool(Jedis jedis) {
        if(jedis != null){
            jedis.close();
        }
    }


}
