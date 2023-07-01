package com.zxy.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/** 配置redis连接池 */
@Component
public class RedisUtils {

    @Autowired
    private static JedisPool jedisPool;

    static {
        //创建properties文件
        Properties properties = new Properties();
        //读取配置文件
        InputStream is= RedisUtils.class.getClassLoader().getResourceAsStream("jedis.properties");
        //加载配置文件
        try {
            properties.load(is);
        }catch (IOException e){
            e.printStackTrace();
        }
        //System.out.println(properties.getProperty("host"));
        //创建连接池对象
        JedisPoolConfig config = new JedisPoolConfig();
        //最大允许连接数
        config.setMaxTotal(Integer.parseInt(properties.getProperty("max-active")));
        //最大空闲连接数
        config.setMaxIdle(Integer.parseInt(properties.getProperty("max-idle")));
        //最小空闲连接数
        config.setMinIdle(Integer.parseInt(properties.getProperty("min-idle")));
        //创建jedisPool连接池对象 timeout客户端超时时间
        jedisPool = new JedisPool(config,properties.getProperty("host"),Integer.parseInt(properties.getProperty("port")),Integer.parseInt(properties.getProperty("timeout")),properties.getProperty("password"));
       /*
       无密码：JedisPool(config,properties.getProperty("host"),Integer.parseInt(properties.getProperty("port")));
        */
    }
    //返回数据连接池

    public static JedisPool jedisPool(){
        return jedisPool;
    }
    //返回Jedis连接

    @Bean
    public static Jedis getJedis(){
        return jedisPool.getResource();
    }
    //关闭资源
    public static void getClose(Jedis jedis){
        if(jedis!=null){
            jedis.close();
        }
    }

    public static void getClose(JedisPool jedisPool){
        if(jedisPool!=null){
            jedisPool.close();
        }
    }
    /**
     * 释放jedis资源
     * @param jedis
     */
    /*public static void returnResource(final Jedis jedis) {
        if (jedis != null) {
            jedisPool.returnResourceObject(jedis);
        }
    }*/

}