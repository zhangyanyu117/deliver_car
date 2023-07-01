package com.zxy.Dao;

import com.zxy.Pojo.Admin;
import com.zxy.Pojo.Order;
import com.zxy.Pojo.Package;
import com.zxy.Pojo.User;
import com.zxy.util.JDBCUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdministratorDao {
    @Autowired
    //创建JdbcTemplate对象
    JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    public Admin login(Admin admin) {
        try {
            String sql = "select * from admin_tb where admin_id = ? and password = ?";
            Admin user = template.queryForObject(
                    sql,
                    new BeanPropertyRowMapper<Admin>(Admin.class),
                    admin.getAdmin_id(),
                    admin.getPassword()
            );
            System.out.println("inquiry database success");
            return user;
        } catch (DataAccessException e) {
            System.out.println("error");
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 查询用户
     * @param inquiryUser
     * @return 查询成功就返回user，查询失败就返回null
     */

    public Admin inquiry(Admin inquiryUser){

        try {
            String sql = "select * from admin_tb where admin_id = ?";
            //使用queryForObject方法查询并封装成user类，注意使用RowMapper
            Admin user = template.queryForObject(
                    sql,
                    new BeanPropertyRowMapper<Admin>(Admin.class),
                    inquiryUser.getAdmin_id()
            );
            System.out.println("inquiry database success");
            return user;
        } catch (DataAccessException e) {
            System.out.println("error");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 显示所有包裹
     * @param
     */
    public List<Package> inquiryAllPack(){
        List<Package> list=new ArrayList<Package>();
        try {
            Connection con=JDBCUtils.getConnection();//建立连接
            String sql="select * from package_tb";
            PreparedStatement st=con.prepareStatement(sql);//预编译sql语句
            ResultSet rs=st.executeQuery();//执行SQL语句，产生结果集
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

    /**
     * 显示所有未取包裹
     * @param
     */
    public List<Package> inquiryPack(){
        List<Package> list=new ArrayList<Package>();
        try {
            Connection con=JDBCUtils.getConnection();//建立连接
            String sql="select * from package_tb where state=0";
            PreparedStatement st=con.prepareStatement(sql);//预编译sql语句
            ResultSet rs=st.executeQuery();//执行SQL语句，产生结果集
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

    /**
     * 显示所有订单
     * @param
     */
    public List<Order> getAllOrder (){
        List<Order> list = new ArrayList<Order>();
        try {
            Connection con = JDBCUtils.getConnection();
            String sql = "select * from order_tb";
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Order order_list = new Order();
                order_list.setOrder_id(rs.getInt(1));
                order_list.setOrdertime(rs.getString(2));
                order_list.setState(rs.getString(3));
                order_list.setPackage_id(rs.getInt(4));
                order_list.setUser_id(rs.getInt(5));
                order_list.setStation(rs.getString(6));
                order_list.setPretime(rs.getString(7));
                order_list.setVerify_code(rs.getString(8));
                order_list.setDoor_number(rs.getString(9));
                list.add(order_list);
            }
            con.close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 主页显示管理员待处理订单
     * @param
     */
    public List<Order> getHomeOrder (){
        List<Order> list = new ArrayList<Order>();
        try {
            Connection con = JDBCUtils.getConnection();
            String sql = "select * from order_tb where state = 000 or state=100";
            PreparedStatement st = con.prepareStatement(sql);
            //st.setInt(1, user.getUser_id());
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Order order_list = new Order();
                order_list.setOrder_id(rs.getInt(1));
                order_list.setOrdertime(rs.getString(2));
                order_list.setState(rs.getString(3));
                order_list.setPackage_id(rs.getInt(4));
                order_list.setUser_id(rs.getInt(5));
                order_list.setStation(rs.getString(6));
                order_list.setPretime(rs.getString(7));
                order_list.setVerify_code(rs.getString(8));
                order_list.setDoor_number(rs.getString(9));
                list.add(order_list);
            }
            con.close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 订单已入柜
     * @param order
     */
    public boolean transit(Order order)
    {

        try {
            String sql = "update order_tb set state = 100 where order_id = ?";
            Object[] args = {order.getOrder_id()};
            template.update(sql, args);
            return true;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 已入柜的订单详情
     * @param inquiryOrder
     */
    public Order Detail(Order inquiryOrder)
    {
        try {
            String sql="select * from order_tb where door_number=? and state = 100";
            Order order=template.queryForObject(
                    sql,
                    new BeanPropertyRowMapper<Order>(Order.class),
                    inquiryOrder.getDoor_number()
            );
            return order;
        }catch (DataAccessException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据package_id查询package
     * @param inquiryPack
     */
    public Package inquiry_package_id(Package inquiryPack){

        try {
            String sql = "select * from package_tb where package_id = ?";
            //使用queryForObject方法查询并封装成user类，注意使用RowMapper
            Package pack = template.queryForObject(
                    sql,
                    new BeanPropertyRowMapper<Package>(Package.class),
                    inquiryPack.getPackage_id()
            );
            System.out.println("inquiry database success");
            return pack;
        } catch (DataAccessException e) {
            System.out.println("error");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据order_id查询package
     * @param inquiryOrder
     */
    public Order inquiry_order_id(Order inquiryOrder){

        try {
            String sql = "select * from order_tb where order_id = ?";
            //使用queryForObject方法查询并封装成user类，注意使用RowMapper
            Order order = template.queryForObject(
                    sql,
                    new BeanPropertyRowMapper<Order>(Order.class),
                    inquiryOrder.getOrder_id()
            );
            System.out.println("inquiry database success");
            return order;
        } catch (DataAccessException e) {
            System.out.println("error");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 订单已删除
     * @param order
     */
    public boolean delete(Order order)
    {

        try {
            String sql = "update order_tb set state = 111 where order_id = ?";
            Object[] args = {order.getOrder_id()};
            template.update(sql, args);
            return true;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 包裹已删除
     * @param pack
     */
    public boolean delete(Package pack)
    {

        try {
            String sql = "update package_tb set state = 11 where package_id = ?";
            Object[] args = {pack.getPackage_id()};
            template.update(sql, args);
            return true;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 已入柜的订单验证码
     * @param
     */
    public List<Order> get_VerifyCode()
    {
        List<Order> list = new ArrayList<Order>();
        try {
            Connection con = JDBCUtils.getConnection();
            String sql = "select * from order_tb where state = 100 and door_number = 1";
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Order order_list = new Order();
                order_list.setOrder_id(rs.getInt(1));
                order_list.setOrdertime(rs.getString(2));
                order_list.setState(rs.getString(3));
                order_list.setPackage_id(rs.getInt(4));
                order_list.setUser_id(rs.getInt(5));
                order_list.setStation(rs.getString(6));
                order_list.setPretime(rs.getString(7));
                order_list.setVerify_code(rs.getString(8));
                order_list.setDoor_number(rs.getString(9));
                list.add(order_list);
            }
            con.close();
            return list;
        }catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 取件终端专用
     * @return
     */
    public boolean accomplish()
    {

        try {
            String sql = "update order_tb set state = 011 where state = 100 and door_number=1";
            Object[] args = {};
            template.update(sql, args);
            return true;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }
}


