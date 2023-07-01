package com.zxy.Dao;

import com.zxy.Pojo.Order;
import com.zxy.Pojo.User;
import com.zxy.util.JDBCUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Repository
public class OrderDao
{

    @Autowired
    //创建JdbcTemplate对象
    JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * 生成新订单
     * @param order
     */
    public boolean newOrder(Order order) {

            try {
                String sql = "insert into order_tb (ordertime,package_id,user_id,station,pretime,verify_code,door_number) values (?,?,?,?,?,?,?)";
                Object[] args = {order.getOrdertime(), order.getPackage_id(), order.getUser_id(), order.getStation(), order.getPretime(),order.getVerify_code(),order.getDoor_number()};
                template.update(sql, args);

            } catch (DataAccessException e1) {
                e1.printStackTrace();
                return false;
            }
            return true;
        }

    /**
     * 主页显示未完成订单
     * @param user
     */
    public List<Order> getHomeOrder (User user){
        List<Order> list = new ArrayList<Order>();
        try {
            Connection con = JDBCUtils.getConnection();
            String sql = "select * from order_tb where user_id = ? and state = 000";
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, user.getUser_id());
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
     * 获取该用户所有订单
     * @param user
     */
    public List<Order> getAllOrder (User user){
        List<Order> list = new ArrayList<Order>();
        try {
            Connection con = JDBCUtils.getConnection();
            String sql = "select * from order_tb where user_id = ?";
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, user.getUser_id());
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
     * 订单详情
     * @param inquiryOrder
     */
    public Order Detail(Order inquiryOrder)
    {
        try {
            String sql="select * from order_tb where order_id=?";
            Order order=template.queryForObject(
                    sql,
                    new BeanPropertyRowMapper<Order>(Order.class),
                    inquiryOrder.getOrder_id()
            );
            return order;
        }catch (DataAccessException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 订单完成
     * @param order
     */
    public boolean accomplish(Order order)
    {

        try {
            String sql = "update order_tb set state = 011 where order_id = ?";
            Object[] args = {order.getOrder_id()};
            template.update(sql, args);
            return true;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 订单取消
     * @param order
     */
    public boolean cancel(Order order)
    {
        try{
            String sql="update package_tb set state = 0 where package_id = ?";
            Object[] args = {order.getPackage_id()};
            template.update(sql, args);
        }catch (DataAccessException e1)
        {
            e1.printStackTrace();
            return false;
        }
        try {
            String sql = "update order_tb set state = 010 where order_id = ?";
            Object[] args = {order.getOrder_id()};
            template.update(sql, args);
            return true;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 订单申诉
     * @param order
     */
    public boolean appeals(Order order)
    {
        try {
            String sql = "update order_tb set state = 001 where order_id = ?";
            Object[] args = {order.getOrder_id()};
            template.update(sql, args);
            return true;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 小车请求查询订单
     * @param
     */
    public List<Order> inquiry_car(Order order)
    {
        List<Order> list = new ArrayList<Order>();
        try {
            Connection con = JDBCUtils.getConnection();
            String sql = "select * from order_tb where state = ? and pretime = ?";
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1,"000");
            st.setString(2,order.getPretime());
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
     * 检查该段时间预约是否已满
     * @param order
     */
    public String check_appointment(Order order)
    {
        List<String> door_number = new ArrayList<String>();
        door_number.add("1");
        //door_number.add("2");
        try {
            Connection con = JDBCUtils.getConnection();
            String sql = "select * from order_tb where pretime = ? and state = 000";
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, order.getPretime());
            ResultSet rs = st.executeQuery();
            while(rs.next())
            {
               if("1".equals(rs.getString(9)))
               {
                   door_number.remove("1");
               }//else if(rs.getString(9).equals("2"))
              // {
                  // door_number.remove("2");
              // }
            }
            con.close();
            if(door_number.size()==0)
            {
                return null;
            }else {
                return door_number.get(0);
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查询已经入柜的订单
     * @param order
     * @return
     */

    public List<Order> inquiry_transit(Order order)
    {
        List<Order> list = new ArrayList<Order>();
        try {
            Connection con = JDBCUtils.getConnection();
            String sql = "select * from order_tb where state = 100";
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
     * 检查剩余可选择的时间段
     * @param
     * @return pretime
     */
    public List<String> inquiry_time()
    {
        List<String> pretime = new ArrayList<String>();
        pretime.add("06:00-07:00");
        pretime.add("07:00-08:00");
        pretime.add("08:00-09:00");
        pretime.add("09:00-10:00");
        pretime.add("10:00-11:00");
        pretime.add("11:00-12:00");
        pretime.add("12:00-13:00");
        pretime.add("13:00-14:00");
        pretime.add("14:00-15:00");
        pretime.add("15:00-16:00");
        pretime.add("16:00-17:00");
        pretime.add("17:00-18:00");
        pretime.add("18:00-19:00");
        pretime.add("19:00-20:00");
        pretime.add("20:00-21:00");
        pretime.add("21:00-22:00");
        pretime.add("22:00-23:00");
        pretime.add("23:00-24:00");
        try {
            Connection con = JDBCUtils.getConnection();
            String sql = "select * from order_tb where state = 000";
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                pretime.remove(rs.getString(7));
            }
            con.close();
            return pretime;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
