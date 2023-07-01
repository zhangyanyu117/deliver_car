package com.zxy.Dao;

import com.zxy.Pojo.User;
import com.zxy.util.JDBCUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * 操控数据库中的user_tb表的对象
 */
@Repository
public class UserDao {

    @Autowired
    //创建JdbcTemplate对象
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * 登陆方法
     * @param loginUser
     * @return 查询成功就返回user，查询失败就返回null
     */

    public User login(User loginUser){
        // 用try catch来包裹，当查询成功就返回user，查询失败就返回null
        try {
            String sql = "select * from user_tb where username = ?and password = ?";
            //使用queryForObject方法查询并封装成user类，注意使用RowMapper
            User user = template.queryForObject(
                    sql,
                    new BeanPropertyRowMapper<User>(User.class),
                    loginUser.getUsername(),
                    loginUser.getPassword()
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

    public User inquiry(User inquiryUser){

        try {
            String sql = "select * from user_tb where username = ?";
            //使用queryForObject方法查询并封装成user类，注意使用RowMapper
            User user = template.queryForObject(
                    sql,
                    new BeanPropertyRowMapper<User>(User.class),
                    inquiryUser.getUsername()
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
     * 注册方法
     * @param registerUser
     * @return 注册成功就返回true，注册失败就返回false
     */

    public boolean register(User registerUser){
        try {
            String sql="insert into user_tb (username,password,phone) values (?,?,?)";
            Object[] args={registerUser.getUsername(),registerUser.getPassword(),registerUser.getPhone()};
            template.update(sql,args);
            return true;
        }catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 修改密码
     * @param alterUser
     * @return 修改成功就返回true，修改失败就返回false
     */

    public boolean alterPassword(User alterUser){
        try {
            String sql="update user_tb set password=? where username=?";
            Object[] args={alterUser.getPassword(),alterUser.getUsername()};
            template.update(sql,args);
            System.out.println("inquiry database success");
            return true;
        }catch (DataAccessException e)
        {
            System.out.println("error");
            e.printStackTrace();
            return false;
        }
    }
}

