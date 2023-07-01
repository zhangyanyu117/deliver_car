package com.zxy.controller;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.zxy.Dao.OrderDao;
import com.zxy.Pojo.Order;
import com.zxy.util.JsonUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/deliver_car")
public class deliver_car {

    @RequestMapping("/inquiry_task")
    public JSONArray inquiry_task(@RequestParam Map<String,String> params) throws IOException
    {
        //获取订单请求时间
        Calendar cal=Calendar.getInstance();

        String hour1=String.valueOf(cal.get(Calendar.HOUR_OF_DAY)+1);
        String hour2=String.valueOf(cal.get(Calendar.HOUR_OF_DAY)+2);

        String pretime=String.join("-",String.join(":",hour1,"00")
                ,String.join(":",hour2,"00"));

        System.out.println(pretime);

        Order order=new Order();
        order.setPretime(pretime);
        //获取参数
        String password = params.get("password");//req.getParameter();
        JSONArray list = new JSONArray();
        if("12345678".equals(password))
        {
            OrderDao dao = new OrderDao();
            List<Order> return_order = dao.inquiry_car(order);
            list.add(0,"11601");
            if(return_order==null)
            {
                String str="暂无待完成的任务";
                JSONObject json=JsonUtils.getJson(str);
                list.add(json);
            }
            else
            {
                for(Order o:return_order)
                {
                    JSONObject json= JsonUtils.getJson(o);
                    list.add(json);
                }
            }
        }
        else
        {
            JSONObject json = JsonUtils.getJson("the password is wrong, please try again","404");
            list.add(0,json);
        }
        return list;
    }

    @RequestMapping("/inquiry_transit")
    public JSONArray inquiry_transit(@RequestParam Map<String,String> params) throws IOException
    {
        //获取订单请求时间
        Order order=new Order();
        //获取参数
        String password = params.get("password");//req.getParameter();
        JSONArray list = new JSONArray();
        if("12345678".equals(password))
        {
            OrderDao dao = new OrderDao();
            List<Order> return_order = dao.inquiry_transit(order);
            list.add(0,"11602");
            if(return_order==null)
            {
                String str="暂无需要运送的包裹";
                JSONObject json=JsonUtils.getJson(str);
                list.add(json);
            }
            else
            {
                for(Order o:return_order)
                {
                    JSONObject json= JsonUtils.getJson(o);
                    list.add(json);
                }
            }
        }
        else
        {
            JSONObject json = JsonUtils.getJson("the password is wrong, please try again","404");
            list.add(0,json);
        }
        return list;
    }
}
