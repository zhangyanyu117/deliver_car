package com.zxy.controller;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.zxy.Dao.AdministratorDao;
import com.zxy.Dao.OrderDao;
import com.zxy.Dao.PackageDao;
import com.zxy.Pojo.Order;
import com.zxy.Pojo.Package;
import com.zxy.Pojo.User;
import com.zxy.util.JsonUtils;
import com.zxy.util.VerifyCodeUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
@CrossOrigin
@RestController
@RequestMapping("/order")
public class orderController {

    @RequestMapping("/newOrder")
    public JSONObject newOrder (@RequestParam Map<String,String> params)
    {

        //获取订单请求时间
        Calendar cal=Calendar.getInstance();
        String year=String.valueOf(cal.get(Calendar.YEAR));
        String month=String.valueOf(cal.get(Calendar.MONTH)+1);
        String day=String.valueOf(cal.get(Calendar.DATE));
        String hour=String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
        String minute=String.valueOf(cal.get(Calendar.MINUTE));
        String second=String.valueOf(cal.get(Calendar.SECOND));
        // 获取参数
        String ordertime=String.join(" ",String.join("-",year,month,day)
                ,String.join(":",hour,minute,second));
        String package_id_s=params.get("package_id");
        String user_id_s=params.get("user_id");
        String station=params.get("station");
        String pretime=params.get("pretime");

        //参数封装成order对象,生成新订单
        Order order=new Order();

        try{
            int package_id=Integer.parseInt(package_id_s);
            order.setPackage_id(package_id);
            int user_id=Integer.parseInt(user_id_s);
            order.setUser_id(user_id);
        }catch (NumberFormatException e)
        {
            e.printStackTrace();
        }

        order.setOrdertime(ordertime);
        order.setStation(station);
        order.setPretime(pretime);
        //检查该段时间是否能够继续预约
        OrderDao dao=new OrderDao();
        PackageDao pdao=new PackageDao();
        String door_number=dao.check_appointment(order);
        if(door_number!=null)
        {
            order.setDoor_number(door_number);

        }else {
            String str="this pretime is not available";
            JSONObject json1=JsonUtils.getJson(str,"11509");
            return json1;
        }
        //调用newOrder方法,将新订单存入数据库
        String verify_code= VerifyCodeUtils.codeCreate();
        order.setVerify_code(verify_code);

        AdministratorDao administratorDao=new AdministratorDao();
        Package p0= new Package();
        p0.setPackage_id(order.getPackage_id());
        Package p1= administratorDao.inquiry_package_id(p0);
        boolean f1=false;
        boolean f2=false;
        JSONObject json=new JSONObject();
        if("0".equals(p1.getState()))
        {
            f1= dao.newOrder(order);
            f2= pdao.complete(order);
        }else{
            String str="this package is already taken away";
            json=JsonUtils.getJson(str,"11509");
            return json;
        }

        if(f1==true&&f2==true)
        {
            String str="create order successfully";
            json= JsonUtils.getJson(str,"11503");
            //resp.getWriter().print(json);
        }
        else if(f2==false)
        {
            String str="this package is not available,please try again";
            json=JsonUtils.getJson(str,"11509");
        }
        else
        {
            String str="fail to create order,please try again";
            json=JsonUtils.getJson(str,"404");
        }
        return json;
    }

    @RequestMapping("/inquiry")
    @ResponseBody
    public JSONArray inquiry (@RequestParam Map<String,String> params) throws IOException
    {
        // 设置编码
        //req.setCharacterEncoding("utf-8");
        // 获取参数
        String user_id_s = params.get("user_id");//req.getParameter("");
        //封装user对象
        User user=new User();
        try{
            int user_id=Integer.parseInt(user_id_s);
            user.setUser_id(user_id);
        }catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
        //调用getAllOrder方法，显示该用户所有订单
        OrderDao dao=new OrderDao();
        List<Order> return_order=dao.getAllOrder(user);
        //resp.setContentType("text/html;charset=utf-8");
        JSONArray list=new JSONArray();
        list.add(0,"11505");
        if(return_order==null)
        {
            String str="there no historical order";
            JSONObject json= JsonUtils.getJson(str);
            list.add(json);
            //String json_str=list.toString();
           // resp.getWriter().print(json_str);

        }else {
            for(Order o:return_order){
                JSONObject json=JsonUtils.getJson(o);
                list.add(json);
            }

            //resp.getWriter().print(json_str);
        }
        //String json_str=list.toString();
        return list;
    }

    @RequestMapping("/detail")
    public JSONObject detail (@RequestParam Map<String,String> params) throws IOException
    {
        // 设置编码
        //req.setCharacterEncoding("utf-8");
        // 获取参数
        String order_id_s=params.get("order_id");//req.getParameter();
        String user_id_s=params.get("user_id");
        // 把参数封装成Order对象
        Order order=new Order();
        int order_id=Integer.parseInt(order_id_s);
        int user_id=Integer.parseInt(user_id_s);
        order.setOrder_id(order_id);
        //调用orderDetail方法，根据订单id查看订单详情
        OrderDao dao=new OrderDao();
        Order return_order=dao.Detail(order);
        if(return_order==null)
        {
            String str="this order is not available";
            JSONObject json=JsonUtils.getJson(str,"404");
            return json;
        }else if(return_order.getUser_id()!=user_id)
        {
            String str="you have no permissions to inquiry this order";
            JSONObject json=JsonUtils.getJson(str,"404");
            return json;
        }
        else{
            JSONObject json= JsonUtils.getJson(return_order,"11502");
            return json;
        }

    }

    @RequestMapping("/cancel")
    public JSONObject cancel (@RequestParam Map<String,String> params) throws IOException
    {
        // 设置编码
        //req.setCharacterEncoding("utf-8");
        // 获取参数
        String order_id_s=params.get("order_id");//req.getParameter();
        String package_id_s=params.get("package_id");//req.getParameter();
        // 把参数封装成Order对象
        Order order=new Order();


        try{
            int package_id=Integer.parseInt(package_id_s);
            order.setPackage_id(package_id);
            int order_id=Integer.parseInt(order_id_s);
            order.setOrder_id(order_id);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        //调用cancel方法
        OrderDao dao=new OrderDao();
        boolean f=dao.cancel(order);

        if(f==true)
        {
            String str="order has been canceled";
            JSONObject json= JsonUtils.getJson(str,"11506");
            return json;
            //resp.getWriter().print(json);
        }
        else {
            String str="system is not available,please try again";
            JSONObject json= JsonUtils.getJson(str,"404");
            return json;
            //resp.getWriter().print(json);
            //throw new BaseException(BaseErrorEnum.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping("/appeal")
    public JSONObject appeal (@RequestParam Map<String,String> params) throws IOException
    {
        // 设置编码
        //req.setCharacterEncoding("utf-8");
        // 获取参数
        String order_id_s=params.get("order_id");//req.getParameter();
        // 把参数封装成Order对象
        Order order=new Order();
        int order_id=Integer.parseInt(order_id_s);
        order.setOrder_id(order_id);
        //调用appeals方法，根据订单if申诉订单
        OrderDao dao=new OrderDao();
        boolean f=dao.appeals(order);
        //resp.setContentType("text/html;charset=utf-8");
        if(f==true)
        {
            String str="order has been appealed";
            JSONObject json= JsonUtils.getJson(str,"11507");
            return json;
            //resp.getWriter().print(json);

        }else {
            String str="system is not available,please try again";
            JSONObject json= JsonUtils.getJson(str,"404");
            return json;
           // resp.getWriter().print(json);
            //throw new BaseException(BaseErrorEnum.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping("/complete")
    public JSONObject complete(@RequestParam Map<String,String> params) throws IOException
    {
        String order_id_s=params.get("order_id");
        // 把参数封装成Order对象
        Order order=new Order();
        int order_id=Integer.parseInt(order_id_s);
        order.setOrder_id(order_id);
        //调用appeals方法，根据订单if申诉订单
        OrderDao dao=new OrderDao();
        boolean f=dao.accomplish(order);
        if(f==true)
        {
            String str="order has been completed";
            JSONObject json= JsonUtils.getJson(str,"11508");
            return json;
        }else {
            String str="system is not available,please try again";
            JSONObject json= JsonUtils.getJson(str,"404");
            return json;
        }
    }

    @RequestMapping("/get_pretime")
    public JSONObject get_pretime(@RequestParam Map<String,String> params) {
        OrderDao dao= new OrderDao();
        List<String> pretime= new ArrayList<String>();
        pretime=dao.inquiry_time();
        JSONObject json= new JSONObject();
        if(pretime.size()!=0){
           json.put("code","11510");
           json.put("pretime_list",pretime);
        }else{
            json.put("code","404");
            json.put("message","today cannot be reserved");
        }
        return json;
    }
}
