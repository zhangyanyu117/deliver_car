package com.zxy.Dao;

import com.zxy.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;



@Component("RedisDao")
public class RedisDao {


    //RedisUtils redisUtils=new RedisUtils();
    //Jedis jedis = RedisUtils.getJedis();
    /**
     * 通过key删除（字节）
     * @param key
     */
    public void del(byte [] key){
        Jedis jedis = RedisUtils.getJedis();
        try{
            jedis.del(key);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
    /**
     * 通过key删除
     * @param key
     */
    public void del(String key){
        Jedis jedis = RedisUtils.getJedis();
        try{
            jedis.del(key);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 添加key value 并且设置存活时间(byte)
     * @param key
     * @param value
     * @param liveTime
     */
    public void set(byte [] key,byte [] value,int liveTime){
        Jedis jedis = RedisUtils.getJedis();
        try{
            jedis.set(key, value);
            jedis.expire(key, liveTime);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
    /**
     * 添加key value 并且设置存活时间
     * @param key
     * @param value
     * @param liveTime
     */
    public void set(String key,String value,int liveTime){
        Jedis jedis = RedisUtils.getJedis();
        try{
            jedis.set(key, value);
            jedis.expire(key, liveTime);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
    /**
     * 添加key value
     * @param key
     * @param value
     */
    public void set(String key,String value){
        Jedis jedis = RedisUtils.getJedis();
        try{
            jedis.set(key, value);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
    /**添加key value (字节)(序列化)
     * @param key
     * @param value
     */
    public void set(byte [] key,byte [] value){
        Jedis jedis = RedisUtils.getJedis();
        try{
            jedis.set(key, value);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
    /**
     * 获取redis value (String)
     * @param key
     * @return
     */
    public String get(String key){
        Jedis jedis = RedisUtils.getJedis();
        try{
            String value = jedis.get(key);
            return value;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }
    /**
     * 获取redis value (byte [] )(反序列化)
     * @param key
     * @return
     */
    public byte[] get(byte [] key){
        Jedis jedis = RedisUtils.getJedis();
        try{
            byte[] value = jedis.get(key);
            return value;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }
       return null;
    }

    /**
     * 通过正则匹配keys
     * @param pattern
     * @return
     */
    public Set<String> keys(String pattern){
        Jedis jedis = RedisUtils.getJedis();
        try{
            Set<String> value = jedis.keys(pattern);
            return value;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    /**
     * 检查key是否已经存在
     * @param key
     * @return
     */
    public boolean exists(String key){
        Jedis jedis = RedisUtils.getJedis();
        try{
            boolean value = jedis.exists(key);
            return value;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return false;
    }
    /**
     * 检查key是否过期
     * @param key
     * @return
     */
    public Long ttl(String key){
        Jedis jedis = RedisUtils.getJedis();
        try{
            Long value = jedis.ttl(key);
            return value;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }
    /*******************redis list操作************************/
    /**
     * 往list中添加元素
     * @param key
     * @param value
     */
    public void lpush(String key,String value){
        Jedis jedis = RedisUtils.getJedis();
        try{
            jedis.lpush(key, value);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void rpush(String key,String value){
        Jedis jedis = RedisUtils.getJedis();
        try{
            jedis.rpush(key, value);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 数组长度
     * @param key
     * @return
     */
    public Long llen(String key){
        Jedis jedis = RedisUtils.getJedis();
        try{
            Long len = jedis.llen(key);
            return len;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    /**
     * 获取下标为index的value
     * @param key
     * @param index
     * @return
     */
    public String lindex(String key,Long index){
        Jedis jedis = RedisUtils.getJedis();
        try{
            String str = jedis.lindex(key, index);
            return str;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public String lpop(String key){
        Jedis jedis = RedisUtils.getJedis();
        try{
            String str = jedis.lpop(key);
            return str;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public List<String> lrange(String key, long start, long end){
        Jedis jedis = RedisUtils.getJedis();
        try{
            List<String> str = jedis.lrange(key, start, end);
            return str;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }
    /*********************redis list操作结束**************************/

    /**
     * 清空redis 所有数据
     * @return
     */
    public String flushDB(){
        Jedis jedis = RedisUtils.getJedis();
        try{
            String str = jedis.flushDB();
            return str;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }
    /**
     * 查看redis里有多少数据
     */
    public long dbSize(){
        Jedis jedis = RedisUtils.getJedis();
        long len=0;
        try{
            len = jedis.dbSize();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return len;
    }
    /**
     * 检查是否连接成功
     * @return
     */
    public String ping(){
        Jedis jedis = RedisUtils.getJedis();
        try{
            String str = jedis.ping();
            return str;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }
       return null;
    }
}
