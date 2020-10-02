package com.baidu.mvc.config;

import com.baidu.constant.CrowdConstant;
import com.baidu.exception.LoginAcctAlreadyInUseException;
import com.baidu.exception.LoginAcctAlreadyInUseForUpdateException;
import com.baidu.exception.LoginFailedException;
import com.baidu.util.CrowdUtil;
import com.baidu.util.ResultEntity;
import com.google.gson.Gson;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;

/**
 * @Author Administrator
 * @create 2020/7/14 0014 17:45
 */
// @ControllerAdvice表示当前类是一个基于注解的异常处理类
@ControllerAdvice
public class CrowdExceptionResolver {

    @ExceptionHandler(value = LoginAcctAlreadyInUseForUpdateException.class)
    public ModelAndView resolveLoginAcctAlreadyInUseException(LoginAcctAlreadyInUseForUpdateException exception,
                                                              HttpServletRequest request,
                                                              HttpServletResponse response) throws IOException {
        String viewName = "admin-edit";
        return commonResoolve(viewName, exception, request, response);
    }

    @ExceptionHandler(value = LoginAcctAlreadyInUseException.class)
    public ModelAndView resolveLoginAcctAlreadyInUseException(LoginAcctAlreadyInUseException exception,
                                              HttpServletRequest request,
                                              HttpServletResponse response) throws IOException {
        String viewName = "admin-add";
        return commonResoolve(viewName, exception, request, response);
    }

    @ExceptionHandler(value = Exception.class)
    public ModelAndView resolveLoginException(Exception exception,
                                             HttpServletRequest request,
                                             HttpServletResponse response) throws IOException {
        String viewName = "admin-login";
        return commonResoolve(viewName, exception, request, response);
    }

    @ExceptionHandler(value = ArithmeticException.class)
    public ModelAndView resolveMathException(ArithmeticException exception,
                                             HttpServletRequest request,
                                             HttpServletResponse response) throws IOException {
        String viewName = "system-error";
        return commonResoolve(viewName, exception, request, response);
    }
    @ExceptionHandler(value = NullPointerException.class)
    public ModelAndView resolveNullPointerException (NullPointerException exception,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        String viewName = "system-error";
        return commonResoolve(viewName, exception, request, response);
    }

    private ModelAndView commonResoolve(String viewName,
                                         Exception exception,
                                         HttpServletRequest request,
                                         HttpServletResponse response) throws IOException {
        //1.判断当前请求的类型
        boolean judgeResult = CrowdUtil.judgeRequestType(request);

        //2.如果是Ajax请求
        if (judgeResult) {

            //3.创建ResultEntity对象
            String message = exception.getMessage();
            ResultEntity<Object> resultEntity = ResultEntity.failed(message);

            //4.创建Gson对象
            Gson gson = new Gson();

            //5.将ResultEntity对象转黄为Json字符串
            String json = gson.toJson(resultEntity);

            //6、将Json字符串作为响应体返回给浏览器
            response.getWriter().write(json);

            //7.由于上面已经通过原生的response对象返回了响应，所以不提供ModelAndView对象
            return null;
        }
        //8.如果不是Ajax请求则创建ModeAndView对象
        ModelAndView modelAndView = new ModelAndView();

        //9.将Exception对象存入模型
        modelAndView.addObject(CrowdConstant.ATTR_NAME_EXCEPTION, exception);

        //10.设置对应的视图名称
        modelAndView.setViewName(viewName);

        //11.返回ModelAndView对象
        return modelAndView;
    }



}
