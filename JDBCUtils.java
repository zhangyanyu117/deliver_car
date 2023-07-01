package com.zxy.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.sql.Connection;

/** 配置mysql连接池 */

@Component
public class JDBCUtils {
    // 预定义连接池对象

    @Autowired
    private static DataSource ds;


    static {
        // 加载配置文件
        Properties pro = new Properties();
        try {
            pro.load(JDBCUtils.class.getClassLoader().getResourceAsStream("druid.properties"));
            // 初始化连接池对象
            ds = DruidDataSourceFactory.createDataSource(pro);

        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 获取连接池对象

    @Bean
    public static DataSource getDataSource(){
        return ds;
    }
    // 获取连接

    @Bean
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
