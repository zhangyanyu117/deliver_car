package com.zxy.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class VerifyCodeUtils {

    public static String codeCreate(){
        String str = "0123456789";
        //将字符串转换为一个新的字符数组。
        char[] VerificationCodeArray = str.toCharArray();
        Random random = new Random();
        int count = 0;//计数器
        StringBuilder stringBuilder = new StringBuilder();
        while(true) {
            //随机生成一个随机数
            int index = random.nextInt(VerificationCodeArray.length);
            char c = VerificationCodeArray[index];
            //限制四位不重复数字
            if (stringBuilder.indexOf(c + "") == -1) {
                stringBuilder.append(c);
                //计数器加1
                count++;
            }
            //当count等于4时结束，随机生成四位数的验证码
            if (count == 4) {
                break;
            }
        }
        return stringBuilder.toString();
    }

}
