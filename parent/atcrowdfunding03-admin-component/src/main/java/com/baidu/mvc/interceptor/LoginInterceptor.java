package com.baidu.mvc.interceptor;

import com.baidu.constant.CrowdConstant;
import com.baidu.entity.Admin;
import com.baidu.exception.AccessForbiddenException;
import com.sun.org.apache.regexp.internal.RE;
import org.aspectj.weaver.bcel.BcelAccessForInlineMunger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 拦截器 对请求进行登录检查 对部分资源进行保护
 * 没有登录的请求无法访问
 * @Author Administrator
 * @create 2020/7/16 0016 10:39
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o)
            throws Exception {

        //1.通过reques对象获取Session对象
        HttpSession session = request.getSession();

        //2.从session域中获取Admin对象
        Admin admin = (Admin) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN);

        //3.判断admin是否为null
        if(admin == null){
            throw new AccessForbiddenException(CrowdConstant.MESSAGE_ACCESS_FORBIDEN);
        }
        //不为null ，返回true
        return true;
    }


}
