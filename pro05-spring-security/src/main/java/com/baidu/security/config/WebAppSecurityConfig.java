package com.baidu.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;

/**
 * <p>Title: WebAppSecurityConfig</p>
 * Description：
 * date：2020/5/13 21:40
 */
// 当前类标记为配置类
@Configuration
// 启动Web环境下权限控制功能
@EnableWebSecurity
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private MyPasswordEncoder passwordEncoder;


    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        //super.configure(builder);   一定要禁用默认规则

//		builder.inMemoryAuthentication()       // 在内存完成用户名密码的校验 指定当前用户拥有什么样的角色
//				.withUser("tom")
//				.password("123123")
//			    .roles("ADMIN")
//				.and()
//				.withUser("nay")
//				.password("123")
//				.roles("UPDATE", "初中生");

        // 使用数据库的
        builder.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder);
    }

    /**
     * 注意！！！  大范围的拦截要放在后面 小范围的放在前面  否则会失效
     */
    @Override
    protected void configure(HttpSecurity security) throws Exception {

        // 给请求授权
        security
                .authorizeRequests()                         // 对请求进行授权
                .antMatchers("/index.jsp")      // 针对index.jsp进行授权
                .permitAll()                                 // [permitAll] : 无条件访问
                .antMatchers("/layui/**")       // 针对/layui目录下所有资源进行授权
                .permitAll()                                //  无条件访问
                .and()
                .authorizeRequests()                        //  对请求进行授权
                .anyRequest()                               //  任意请求
                .authenticated()                            //  需要登录以后才可以访问
                .and()
                .formLogin()                                // 使用表单形式登录

                // 指定登录页的同时会影响到：“提交登录表单的地址” “退出登录的地址” “登录失败地址”
                .loginPage("/index.jsp")                    // 指定登录页面（如果没有指定会访问SpringSecurity自带）
                .loginProcessingUrl("/do/login.html")       // 指定提交登录表单的地址
                .usernameParameter("loginAcct")             // 定制登录账号的请求参数名
                .passwordParameter("userPswd")
                .defaultSuccessUrl("/main.html")                      // 设置登录成功后默认前方的url
                .and()
                .csrf()
                .disable()          //禁用CSRF功能

                .formLogin() // 开启表单登录功能
                .loginPage("/admin/to/login/page.html")               // 指定登录页面
                .loginProcessingUrl("/security/do/login.html")        // 处理登录请求的地址
                .defaultSuccessUrl("/admin/to/main/page.html")        // 登录成功后跳转的地址
                .usernameParameter("loginAcct")                         // 账号的请求参数名
                .passwordParameter("userPswd")                          // 密码的请求参数名
                .and()
                .logout()
                .logoutUrl("/security/do/logout.html")               // 处理退出请求的地址
                .logoutSuccessUrl("/admin/to/login/page.html")       // 登录退出后跳转的地址
                ;

    }
}
