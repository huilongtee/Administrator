package com.example.Administrator.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.Administrator.entity.User;
import com.example.Administrator.mapper.UserMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

//use @Component to add the TokenUtil into the container of Spring
@Component
public class TokenUtils {

//    the reason of use both staticUserMapper and userMapper because there is a static method below

    private static UserMapper staticUserMapper;

    @Resource
    UserMapper userMapper;

    @PostConstruct
    public void setUserService() {
        staticUserMapper = userMapper;
    }

    //generate token
    public static String generateToken(String userId, String sign) {
        return JWT.create().withAudience(userId) //put user id into the token as the audience
                .withExpiresAt(DateUtil.offsetHour(new Date(), 2)) //token will be expired in 2 hours
                .sign(Algorithm.HMAC256(sign)); //use password as the secret key of the token

    }

    //    get current login user details
    public static User getCurrentUser() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("token");
            if (StrUtil.isNotBlank(token)) {
                String userId = JWT.decode(token).getAudience().get(0);
                return staticUserMapper.selectUserById(Integer.valueOf(userId));
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

}

