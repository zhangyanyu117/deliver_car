package com.zxy.Dao;

import com.zxy.Pojo.Order;
import com.zxy.Pojo.Package;
import com.zxy.Pojo.User;
import com.zxy.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/*
* 操控package_tb数据库表
* */

@Repository
public class PackageDao {


    JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    /**
     * 包裹查询
     * @param pack
     */
    public List<Package> inquiry(Package pack){
        List<Package> list=new ArrayList<Package>();
        try {
            Connection con=JDBCUtils.getConnection();//建立连接
            String sql="select * from package_tb where user_id = ? and state = 0";
            PreparedStatement st=con.prepareStatement(sql);//预编译sql语句
            System.out.println("预编译");
            st.setInt(1, pack.getUser_id());//给SQL传参
            System.out.println("SQL传参");
            ResultSet rs=st.executeQuery();//执行SQL语句，产生结果集
            System.out.println("产生结果集");
            while (rs.next()){
                Package pack_list=new Package();
                pack_list.setPackage_id(rs.getInt(1));
                pack_list.setPhone(rs.getString(2));
                pack_list.setState(rs.getString(3));
                pack_list.setUser_id(rs.getInt(4));
                list.add(pack_list);
            }
            con.close();
            return list;
        }
        catch (SQLException e) {
            System.out.println("error");
            e.printStackTrace();
            return null;
        }
    }

    public boolean complete(Order order) {
        try {
            String sql = "update package_tb set state = 1 where package_id = ?";
            Object[] args = {order.getPackage_id()};
            template.update(sql, args);
            return true;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 导入新包裹
     * @param pack
     */
    public boolean newPack(Package pack) {

        try {
            String sql = "insert into package_tb (phone,user_id) values (?,?)";
            Object[] args = {pack.getPhone(),pack.getUser_id()};
            template.update(sql, args);

        } catch (DataAccessException e1) {
            e1.printStackTrace();
            return false;
        }
        return true;
    }

    public User inquiry(User inquiryUser){

        try {
            String sql = "select * from user_tb where user_id = ?";
            //使用queryForObject方法查询并封装成user类，注意使用RowMapper
            User user = template.queryForObject(
                    sql,
                    new BeanPropertyRowMapper<User>(User.class),
                    inquiryUser.getUser_id()
            );
            System.out.println("inquiry database success");
            return user;
        } catch (DataAccessException e) {
            System.out.println("error");
            e.printStackTrace();
            return null;
        }
    }
}
