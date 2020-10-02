package com.baidu.mvc.config;


import com.baidu.constant.CrowdConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;


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
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;

    // 将带盐值加密的 放入IOC容器中 bean默认为单实例 每次调用时，检查是否有这个bean,如果有就不会真正执行这个函数
    @Bean
    public BCryptPasswordEncoder getPasswordEncoder(){

        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        //super.configure(builder);   一定要禁用默认规则

        // 临时使用内存版登录的模式测试代码
        //       builder.inMemoryAuthentication().withUser("tom").password("123123").roles("ADMIN");
        // 使用数据库的
        builder.userDetailsService(userDetailsService)
               // 进行加密判断
               .passwordEncoder(getPasswordEncoder());
    }

    /**
     * 注意！！！  大范围的拦截要放在后面 小范围的放在前面  否则会失效
     */
    @Override
    protected void configure(HttpSecurity security) throws Exception {

        // 给请求授权
        security
                .authorizeRequests()    // 对请求进行授权
                .antMatchers("/admin/to/login/page.html")// 登录页面设置
                .permitAll()                                          // 无条件访问
                .antMatchers("/bootstrap/**")               // 对静态资源设置
                .permitAll()
                .antMatchers("/crowd/**")
                .permitAll()
                .antMatchers("/css/**")
                .permitAll()
                .antMatchers("/fonts/**")
                .permitAll()
                .antMatchers("/img/**")
                .permitAll()
                .antMatchers("/jquery/**")
                .permitAll()
                .antMatchers("/script/**")
                .permitAll()
                .antMatchers("/static/layui/**")
                .permitAll()
                .antMatchers("/ztree/**")
                .permitAll()

                .antMatchers("/admin/get/page.html")
                .hasRole("经理")
                //其他任意请求
                .anyRequest()
                .authenticated()//认证后访问
                .and()
                .exceptionHandling() //自定义异常映射，以免在filter阶段抛出403异常
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
                        httpServletRequest.setAttribute("exception",new Exception(CrowdConstant.MESSAGE_LOGIN_DENIED));
                        httpServletRequest.getRequestDispatcher("/WEB-INF/pages/system-error.jsp").forward(httpServletRequest,httpServletResponse);
                    }
                })
                .and()
                .csrf() // 跨站请求伪造功能
                .disable() //取消

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
