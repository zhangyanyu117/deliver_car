package com.zxy.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.zxy.Dao.OrderDao;
import com.zxy.Dao.UserDao;
import com.zxy.Dao.RedisDao;
import com.zxy.Pojo.Order;
import com.zxy.Pojo.User;
import com.zxy.util.JsonUtils;
import com.zxy.util.PasswordUtils;
import com.zxy.util.TokenUtils;
import org.springframework.core.NestedIOException;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.List;
import java.util.Map;



@CrossOrigin
@RestController
@RequestMapping("/user")
public class userController {


    @RequestMapping("/login")
    public JSONObject login(@RequestParam Map<String,String> params) throws NestedIOException, IOException {

        // 设置编码
        //req.setAttribute("","");
        //req.setCharacterEncoding("utf-8");
        // 获取参数
        String username = params.get("username");//req.getParameter();
        String password = params.get("password");//req.getParameter();
        System.out.println("login请求成功");
        //加密
        String encodedPassword = PasswordUtils.encode(password);
        // 把参数封装成User对象
        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        // 调用inquiry方法
        UserDao dao = new UserDao();
        User inquiryUser = dao.inquiry(user);
        if(inquiryUser==null){
            String str="username has not been registered";
            JSONObject json=JsonUtils.getJson(str,"404");
            return json;
        }
        User returnUser=dao.login(user);
        System.out.println("success");
        //resp.setContentType("text/html;charset=utf-8");
        if (returnUser == null) {

            String str = "login failed with wrong username or password";
            JSONObject json = JsonUtils.getJson(str, "404");
            return json;
            //resp.getWriter().print(json);
        } else {
            JSONObject json = new JSONObject();
            json.put("code", "11401");
            json.put("user_id", String.valueOf(returnUser.getUser_id()));
            json.put("username", returnUser.getUsername());
            String token= TokenUtils.sign(returnUser);
            System.out.println(token);
            //存入redis过期时间为7天
            RedisDao redisDao =new RedisDao();
            redisDao.set(token,String.valueOf(returnUser.getUser_id()),7*24*60*60);
            json.put("token",token);
            return json;
            //resp.getWriter().print(json);
        }
    }

    @RequestMapping("/register")
    public JSONObject register (@RequestParam Map<String,String> params) throws  IOException
    {
        //req.setCharacterEncoding("utf-8");
        // 获取参数
        String username = params.get("username");//req.getParameter();
        String password = params.get("password");//req.getParameter();
        //加密
        String encodedPassword = PasswordUtils.encode(password);
        // 把参数封装成User对象
        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setPhone(username);
        //调用inquiry方法判断是否已存在用户
        UserDao dao = new UserDao();
        User returnUser = dao.inquiry(user);
        //resp.setContentType("text/html;charset=utf-8");
        if(returnUser!=null)
        {
            String str="fail to register,user has been existed";
            JSONObject json= JsonUtils.getJson(str,"404");
            return json;
            //resp.getWriter().print(json);
            //throw new BaseException(BaseErrorEnum.USER_REGISTER_ERROR);
        }else{
            boolean f=dao.register(user);
            if(f) {
                JSONObject json=JsonUtils.getJson("注册成功","11402");
                return json;
                //resp.getWriter().print(json);
            }else{
                String str="system is not available,please try again";
                JSONObject json=JsonUtils.getJson(str,"404");
                return json;
                //resp.getWriter().print(json);
            }
        }
    }

    @RequestMapping("/alterpwd")
    public JSONObject alterpwd (@RequestParam Map<String,String> params) throws  IOException
    {
        //req.setCharacterEncoding("utf-8");
        // 获取参数
        String username = params.get("username");//req.getParameter();
        String password = params.get("password");//req.getParameter();
        //加密
        String encodedPassword = PasswordUtils.encode(password);
        // 把参数封装成User对象
        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        System.out.println("修改成功");
        // 调用inquiry方法，检验用户是否存在
        UserDao dao = new UserDao();
        User returnUser = dao.inquiry(user);
        //resp.setContentType("text/html;charset=utf-8");
        if(returnUser==null){
            JSONObject json= JsonUtils.getJson("用户不存在","404");
            return json;
            //resp.getWriter().print(json);
            // throw new BaseException(BaseErrorEnum.USER_NOT_EXITS);
        }else{
            boolean f=dao.alterPassword(user);
            if(f){
                JSONObject json=JsonUtils.getJson("修改成功","11403");
                return json;
                //resp.getWriter().print(json);
            }else{
                String str="system is not available,please try again";
                JSONObject json= JsonUtils.getJson(str,"404");
                return json;
                //resp.getWriter().print(json);
                // throw new BaseException(BaseErrorEnum.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @RequestMapping("/home")
    public JSONArray home(@RequestParam Map<String,String> params) throws  IOException{

        String username = params.get("username");//req.getParameter("username");
        String user_id_s = params.get("user_id");//req.getParameter("user_id");
        //封装user对象
        User user=new User();
        try{
            int user_id=Integer.parseInt(user_id_s);
            user.setUser_id(user_id);
        }catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
        user.setUsername(username);
        UserDao userDao=new UserDao();
        User return_user=userDao.inquiry(user);
        //调用getHomeOrder方法，显示主页未完成订单
        OrderDao orderDao=new OrderDao();
        List<Order> return_order=orderDao.getHomeOrder(user);
        //jsonList存储多个订单信息
        JSONArray list =new JSONArray();
        JSONObject json1= JsonUtils.getJson(return_user);
        list.add(0,"11501");
        list.add(json1);
        if(return_order==null)
        {
            //resp.setContentType("text/html;charset=utf-8");
            String str="there is no order";
            JSONObject json2=JsonUtils.getJson(str);
            list.add(json2);
            //String json_str=list.toString();
            //resp.getWriter().print(json_str);
        }else {
            //resp.setContentType("text/html;charset=utf-8");
            for(Order o:return_order){
                JSONObject json2=JsonUtils.getJson(o);
                list.add(json2);
            }

            //esp.getWriter().print(json_str);
        }
        //String json_str=list.toString();
        return list;
    }
}