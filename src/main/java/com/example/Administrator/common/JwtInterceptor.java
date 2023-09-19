package com.example.Administrator.common;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.Administrator.entity.User;
import com.example.Administrator.exception.ServiceException;
import com.example.Administrator.mapper.UserMapper;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//relationship between JWTInterceptor and InterceptorConfig
public class JwtInterceptor implements HandlerInterceptor {

    @Resource
    private UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("token"); //token that get from header of frontend
        if (StrUtil.isBlank(token)) {
            token = request.getParameter("token"); //token that get from url    ?token=xxx
        }

        // if the path see thre @AuthAccess, the path will be open and not restricted
        if(handler instanceof HandlerMethod){
            AuthAccess annotation=((HandlerMethod) handler).getMethodAnnotation(AuthAccess.class);
            if(annotation!=null){
                return true;
            }
        }

//        execute authenticatio
        if (StrUtil.isBlank(token)) {
            throw new ServiceException("401", "Please login");
        }

//        get the userId from token
        String userId;
        try {
            userId = JWT.decode(token).getAudience().get(0); //decode the token from jwt token
        } catch (JWTDecodeException j) {
            throw new ServiceException("401", "Please login");
        }

//        get the userId from token and get the User from database
        User user = userMapper.selectUserById(Integer.valueOf(userId));
        if (user == null) {
            throw new ServiceException("401", "Please login");
        }

//        generate a verifier by using password of user that has been encrypted
        //must use password for verifier
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try {
            jwtVerifier.verify(token); //verify token
        } catch (JWTVerificationException e) {
            throw new ServiceException("401", "Please login");
        }

        return true;
    }
}
