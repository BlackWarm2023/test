package com.interceptor;

import com.alibaba.fastjson2.JSON;
import com.commen.utils.JwtUtils;
import com.common.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: 拦截器
 * @author: BlackWarm
 * @date: 2023年 09月 24日  16:51
 */

@Component
// 日志组件
@Slf4j
public class JwtValidateInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("X-Token");
        log.debug(request.getRequestURI() + " 需要验证: " + token);
        if (token != null){
            try{
                jwtUtils.parseToken(token);
                log.debug(request.getRequestURI() + "验证通过");
                return true;
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        log.debug(request.getRequestURI() + "验证失败,禁止访问");
        response.setContentType("application/json;charset=utf-8");
        Result<Object> fail = Result.fail(20005,"jwt无效,请重新登录");
        response.getWriter().write(JSON.toJSONString(fail));
        //拦截
        return false;
    }
}
