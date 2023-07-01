package com.zxy.controller;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.zxy.Dao.AdministratorDao;
import com.zxy.Dao.PackageDao;
import com.zxy.Pojo.Package;
import com.zxy.Pojo.User;
import com.zxy.util.JsonUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/pack")
public class packController {

    @RequestMapping("/inquiry")
    public JSONArray inquiry (@RequestParam Map<String,String> params) throws IOException
    {
        // 设置编码
        //req.setCharacterEncoding("utf-8");
        // 获取参数
        String user_id_s = params.get("user_id");//req.getParameter();
        // 把参数封装成Package对象
        Package pack = new Package();
        try {
            int user_id=Integer.parseInt(user_id_s);
            pack.setUser_id(user_id);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        //调用packageData
        PackageDao packDao=new PackageDao();
        List<Package> return_pack=packDao.inquiry(pack);
        //resp.setContentType("text/html;charset=utf-8");
        JSONArray list=new JSONArray();
        list.add(0,"11504");
        if(return_pack==null)
        {
            String str="there is no package";
            JSONObject json= JsonUtils.getJson(str);
            list.add(json);
            //resp.getWriter().print(json);
        }
        else
        {
            for(Package p:return_pack)
            {
                JSONObject json= JsonUtils.getJson(p);
                list.add(json);
            }
        }
        return list;
    }
    @RequestMapping("/search")
    public JSONObject detail (@RequestParam Map<String,String> params) throws IOException
    {
        String package_id_s = params.get("package_id");//
        String user_id_s = params.get("user_id");
        // 把参数封装成Package对象
        Package pack = new Package();
        try {
            int package_id=Integer.parseInt(package_id_s);

            pack.setPackage_id(package_id);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        int user_id=Integer.parseInt(user_id_s);
        //调用packageData
        AdministratorDao administratorDao=new AdministratorDao();
        Package return_pack=administratorDao.inquiry_package_id(pack);
        //resp.setContentType("text/html;charset=utf-8");

        if(return_pack==null)
        {
            String str="there is no such package";
            JSONObject json= JsonUtils.getJson(str);
            return json;
        }else if(return_pack.getUser_id()!=user_id)
        {
            String str="you have no permissions to inquiry this package";
            JSONObject json=JsonUtils.getJson(str,"404");
            return json;
        }
        else
        {
            JSONObject json = JsonUtils.getJson(return_pack, "11506");
            return json;
        }
    }

    @RequestMapping("/newPack")
    public JSONObject newPack (@RequestParam Map<String,String> params) throws IOException
    {
        // 设置编码
        //req.setCharacterEncoding("utf-8");
        // 获取参数
        String phone=params.get("phone");
        String user_id_s=params.get("user_id");
        // 把参数封装成Package对象
        Package pack = new Package();
        pack.setPhone(phone);

        User user =new User();
        try {
            int user_id=Integer.parseInt(user_id_s);
            user.setUser_id(user_id);
            pack.setUser_id(user_id);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        //调用packageData
        PackageDao packDao=new PackageDao();
        User returnUser=packDao.inquiry(user);
        if(returnUser.getPhone().equals(phone))
        {
            boolean f=packDao.newPack(pack);
            if(f)
            {
                String str="success to create new package";
                JSONObject json=JsonUtils.getJson(str,"11507");
                return json;
            }else {
                String str="fail to create new package";
                JSONObject json=JsonUtils.getJson(str,"404");
                return json;
            }
        }
       else{
            String str="the binding of phone is different";
            JSONObject json=JsonUtils.getJson(str,"404");
            return json;
        }
    }
}
