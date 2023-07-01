package com.zxy.util;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

@Component
public class PasswordUtils {

    public static String encode(String rawPassword)
    {
        String salt = "fgdwghhfsncjsuiowidnassdtrbp";
        String encodedPassword = DigestUtils.md5DigestAsHex(
                (salt + salt + rawPassword + salt + salt).getBytes());
        return encodedPassword;
    }
}
