package com.zxy.util;

import com.alibaba.fastjson2.JSONObject;

import com.zxy.Pojo.Order;
import com.zxy.Pojo.Package;
import com.zxy.Pojo.User;
import org.springframework.stereotype.Component;

@Component
public class JsonUtils {


    public static JSONObject getJson(User user,String code) {
        JSONObject json = new JSONObject();
        json.put("code",code);
        json.put("user_id", user.getUser_id());
        json.put("username", user.getUsername());
        json.put("password", user.getPassword());
        json.put("phone",user.getPhone());
        return json;
    }


    public static JSONObject getJson(Package pack,String code){
        JSONObject json = new JSONObject();
        json.put("code",code);
        json.put("package_id",pack.getPackage_id());
        json.put("phone",pack.getPhone());
        json.put("state",pack.getState());
        json.put("user_id", pack.getUser_id());
        return json;
    }


    public static JSONObject getJson(Order order,String code) {
        JSONObject json = new JSONObject();
        json.put("code",code);
        json.put("order_id", order.getOrder_id());
        json.put("ordertime",order.getOrdertime());
        json.put("state",order.getState());
        json.put("package_id",order.getPackage_id());
        json.put("user_id", order.getUser_id());
        json.put("station", order.getStation());
        json.put("pretime", order.getPretime());
        json.put("verify_code",order.getVerify_code());
        json.put("door_number",order.getDoor_number());
        return json;
    }


    public static JSONObject getJson(String str,String code)
    {
        JSONObject json = new JSONObject();
        json.put("code",code);
        json.put("message",str);
        return json;
    }


    public static JSONObject getJson(User user) {
        JSONObject json = new JSONObject();
        json.put("user_id", user.getUser_id());
        json.put("username", user.getUsername());
        json.put("password", user.getPassword());
        json.put("phone",user.getPhone());
        return json;
    }


    public static JSONObject getJson(Package pack){
        JSONObject json = new JSONObject();
        json.put("package_id",pack.getPackage_id());
        json.put("phone",pack.getPhone());
        json.put("state",pack.getState());
        json.put("user_id", pack.getUser_id());
        return json;
    }


    public static JSONObject getJson(Order order) {
        JSONObject json = new JSONObject();
        json.put("order_id", order.getOrder_id());
        json.put("ordertime",order.getOrdertime());
        json.put("state",order.getState());
        json.put("package_id",order.getPackage_id());
        json.put("user_id", order.getUser_id());
        json.put("station", order.getStation());
        json.put("pretime", order.getPretime());
        json.put("verify_code",order.getVerify_code());
        json.put("door_number",order.getDoor_number());
        return json;
    }


    public static JSONObject getJson(String str)
    {
        JSONObject json = new JSONObject();
        json.put("message",str);
        return json;
    }
}
