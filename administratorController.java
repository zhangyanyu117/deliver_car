package com.zxy.controller;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.zxy.Dao.*;
import com.zxy.Pojo.Admin;
import com.zxy.Pojo.Order;
import com.zxy.Pojo.Package;
import com.zxy.Pojo.User;
import com.zxy.util.CallShell;
import com.zxy.util.JsonUtils;
import com.zxy.util.PasswordUtils;
import com.zxy.util.TokenUtils;
import org.springframework.core.NestedIOException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/administrator")
public class administratorController {

    @RequestMapping("/login")
    public JSONObject login(@RequestParam Map<String,String> params) throws NestedIOException, IOException {

        // 设置编码
        //req.setAttribute("","");
        //req.setCharacterEncoding("utf-8");
        // 获取参数
        String admin_id = params.get("admin_id");//req.getParameter();
        String password = params.get("password");//req.getParameter();
        System.out.println("login请求成功");
        //加密
       // String encodedPassword = PasswordUtils.encode(password);
        // 把参数封装成User对象
        Admin admin=new Admin();
        admin.setAdmin_id(admin_id);
        admin.setPassword(password);

        // 调用inquiry方法
        AdministratorDao dao = new AdministratorDao();
        Admin inquiryUser = dao.inquiry(admin);
        if(inquiryUser==null){
            String str="admin has not existed";
            JSONObject json=JsonUtils.getJson(str,"404");
            return json;
        }
        Admin returnUser = dao.login(admin);
        System.out.println("success");
        //resp.setContentType("text/html;charset=utf-8");
        if (returnUser == null) {

            String str = "login failed with wrong admin_id or password";
            JSONObject json = JsonUtils.getJson(str, "404");
            return json;
            //resp.getWriter().print(json);
        } else {
            JSONObject json = new JSONObject();
            json.put("code", "11801");
            json.put("admin_id", returnUser.getAdmin_id());
            json.put("password", returnUser.getPassword());
            return json;
            //resp.getWriter().print(json);
        }
    }


    @RequestMapping("/home")
    public JSONArray home (@RequestParam Map<String,String> params)
    {
        AdministratorDao dao = new AdministratorDao();
        List<Order> return_orders = dao.getHomeOrder();
        JSONArray list = new JSONArray();
        list.add(0,"11701");
        if(return_orders==null)
        {
            String str="there is no remaining order to handle";
            JSONObject json = JsonUtils.getJson(str);
            list.add(json);
        }else {
            for(Order o:return_orders)
            {
                JSONObject json = JsonUtils.getJson(o);
                list.add(json);
            }
        }
        return list;
    }

    @RequestMapping("/inquiryAllOrder")
    public JSONArray inquiryAllOrder (@RequestParam Map<String,String> params)
    {
        AdministratorDao dao = new AdministratorDao();
        List<Order> return_orders = dao.getAllOrder();
        JSONArray list = new JSONArray();
        list.add(0,"11802");
        if(return_orders==null)
        {
            String str="there is no historical order";
            JSONObject json = JsonUtils.getJson(str);
            list.add(json);
        }else {
            for(Order o:return_orders)
            {
                JSONObject json = JsonUtils.getJson(o);
                list.add(json);
            }
        }
        return list;
    }
    @RequestMapping("/inquiryAllPack")
    public JSONArray inquiryAllPack (@RequestParam Map<String,String> params) throws IOException
    {

        AdministratorDao administratorDao=new AdministratorDao();

        List<Package> return_pack=administratorDao.inquiryAllPack();
        JSONArray list=new JSONArray();
        list.add(0,"11803");
        if(return_pack==null)
        {
            String str="there is no package";
            JSONObject json= JsonUtils.getJson(str);
            list.add(json);
            //resp.getWriter().print(json);
        }
        else
        {
            //resp.setContentType("text/html;charset=utf-8");
            for(Package p:return_pack)
            {
                JSONObject json= JsonUtils.getJson(p);
                list.add(json);
            }
            //resp.getWriter().print(json_str);
        }
        //String json_str=list.toString();
        return list;
    }

    @RequestMapping("/inquiryPack")
    public JSONArray inquiryPack (@RequestParam Map<String,String> params) throws IOException
    {

        AdministratorDao administratorDao=new AdministratorDao();

        List<Package> return_pack=administratorDao.inquiryPack();
        //resp.setContentType("text/html;charset=utf-8");
        JSONArray list=new JSONArray();
        list.add(0,"11804");
        if(return_pack==null)
        {
            String str="there is no remaining package";
            JSONObject json= JsonUtils.getJson(str);
            list.add(json);
            //resp.getWriter().print(json);
        }
        else
        {
            //resp.setContentType("text/html;charset=utf-8");
            for(Package p:return_pack)
            {
                JSONObject json= JsonUtils.getJson(p);
                list.add(json);
            }
            //resp.getWriter().print(json_str);
        }
        return list;
    }
    @RequestMapping("/transit")
    public JSONObject transit (@RequestParam Map<String,String> params)
    {
        String order_id_s = params.get("order_id");

        Order order = new Order();
        int order_id=Integer.parseInt(order_id_s);
        order.setOrder_id(order_id);

        AdministratorDao dao = new AdministratorDao();
        boolean f=dao.transit(order);

        if(f){
            String str="the package is already";
            JSONObject json= JsonUtils.getJson(str,"11702");
            return json;
        }else{
            String str="system is not available,please try again";
            JSONObject json= JsonUtils.getJson(str,"404");
            return json;
        }
    }

    @RequestMapping("/cancel")
    public JSONObject cancel (@RequestParam Map<String,String> params) throws IOException
    {

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
        if(f)
        {
            String str="this order is canceled";
            JSONObject json= JsonUtils.getJson(str,"11703");
            return json;
        }
        else {
            String str="system is not available,please try again";
            JSONObject json= JsonUtils.getJson(str,"404");
            return json;
        }
    }

    @RequestMapping("/search_order")
    public JSONObject search_order (@RequestParam Map<String,String> params) throws IOException {
        // 设置编码
        //req.setCharacterEncoding("utf-8");
        // 获取参数
        String order_id_s = params.get("order_id");//req.getParameter();
        // 把参数封装成Order对象
        Order order = new Order();
        int order_id = Integer.parseInt(order_id_s);
        order.setOrder_id(order_id);
        //调用orderDetail方法，根据订单id查看订单详情
        // 管理者拥有查看所有包裹权限
        AdministratorDao administratorDao=new AdministratorDao();
        Order return_order = administratorDao.inquiry_order_id(order);
        if (return_order == null) {
            String str = "this order is not available";
            JSONObject json = JsonUtils.getJson(str, "404");
            return json;
        }
        else {
            JSONObject json = JsonUtils.getJson(return_order, "11805");
            return json;
        }
    }

    @RequestMapping("/search_pack")
    public JSONObject search_pack (@RequestParam Map<String,String> params) throws IOException
    {

        String package_id_s = params.get("package_id");//req.getParameter();
        // 把参数封装成Package对象
        Package pack = new Package();
        try {
            int package_id=Integer.parseInt(package_id_s);
            pack.setPackage_id(package_id);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        //调用packageData
        AdministratorDao administratorDao=new AdministratorDao();
        Package return_pack=administratorDao.inquiry_package_id(pack);
        //resp.setContentType("text/html;charset=utf-8");

        if(return_pack==null)
        {
            String str="there is no such package";
            JSONObject json= JsonUtils.getJson(str);
           return json;
        }
        else
        {
            JSONObject json = JsonUtils.getJson(return_pack, "11806");
            return json;
        }

    }
    @RequestMapping("/Order_Detail")
    public JSONObject Order_Detail (@RequestParam Map<String,String> params) throws IOException
    {
        String door_number = params.get("door_number");

        Order order = new Order();
        order.setDoor_number(door_number);

        AdministratorDao dao = new AdministratorDao();
        Order return_order = dao.Detail(order);

        JSONObject json = new JSONObject();
        json.put("code","11704");
        json.put("order_id",return_order.getOrder_id());
        json.put("verify_code",return_order.getVerify_code());
        json.put("door_number",return_order.getDoor_number());
        return json;
    }

    @RequestMapping("/delete_order")
    public JSONObject delete_order (@RequestParam Map<String,String> params) throws IOException {
        // 设置编码
        //req.setCharacterEncoding("utf-8");
        // 获取参数
        String order_id_s = params.get("order_id");//req.getParameter();
        // 把参数封装成Order对象
        Order order = new Order();
        int order_id = Integer.parseInt(order_id_s);
        order.setOrder_id(order_id);
       //删除订单，管理员依旧可见，用户不可见
        AdministratorDao administratorDao=new AdministratorDao();
        boolean f=administratorDao.delete(order);
        if (f) {
            String str = "this order has been deleted";
            JSONObject json = JsonUtils.getJson(str, "11809");
            return json;
        }
        else {
            String str="system is not available,please try again";
            JSONObject json= JsonUtils.getJson(str,"404");
            return json;
        }
    }

    @RequestMapping("/delete_pack")
    public JSONObject delete_pack (@RequestParam Map<String,String> params) throws IOException {
        // 设置编码
        //req.setCharacterEncoding("utf-8");
        // 获取参数
        String package_id_s = params.get("package_id");//req.getParameter();
        // 把参数封装成Order对象
        Package pack=new Package();
        int package_id = Integer.parseInt(package_id_s);
        pack.setPackage_id(package_id);
        //删除订单，管理员依旧可见，用户不可见
        AdministratorDao administratorDao=new AdministratorDao();
        boolean f=administratorDao.delete(pack);
        if (f) {
            String str = "this package has been deleted";
            JSONObject json = JsonUtils.getJson(str, "11810");
            return json;
        }
        else {
            String str="system is not available,please try again";
            JSONObject json= JsonUtils.getJson(str,"404");
            return json;
        }
    }
    @RequestMapping("/get_VerifyCode")
    public JSONObject get_VerifyCode (@RequestParam Map<String,String> params)
    {
        AdministratorDao dao = new AdministratorDao();
        List<Order> return_orders = dao.get_VerifyCode();
        JSONArray list = new JSONArray();
        if(return_orders==null)
        {
            String str="there is no historical order";
            JSONObject json = new JSONObject();
            json.put("code",null);
            return json;
        }else {
                Order o=return_orders.get(0);
                String verify_code=o.getVerify_code();
                JSONObject json = new JSONObject();
                json.put("code",verify_code);
                return json;
        }
    }
    @RequestMapping("/complete")
    public JSONObject complete (@RequestParam Map<String,String> params)
    {
        AdministratorDao dao = new AdministratorDao();
        boolean b=dao.accomplish();
        if(b){
            JSONObject json = JsonUtils.getJson("complete","11512");
            return json;
        }else{
            JSONObject json = JsonUtils.getJson("the System is not available, please try again","404");
            return json;
        }

    }
    @RequestMapping("/open_door")
    public JSONObject open_door (@RequestParam Map<String,String> params)
    {
        CallShell.runShell();
        JSONObject json = JsonUtils.getJson("open successfully","11513");
        return json;
    }

}
