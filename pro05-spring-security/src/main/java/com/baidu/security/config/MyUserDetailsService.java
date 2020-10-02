package com.baidu.security.config;

import com.baidu.security.entity.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import sun.security.util.Password;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Administrator
 * @create 2020/7/23 0023 9:40
 */

@Component
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 总目标：根据表单提交的用户名查询User对象，并装配角色，权限等信息
    @Override
    public UserDetails loadUserByUsername
            //表单提交的用户名
            (String username) throws UsernameNotFoundException {

        String sql = "SELECT id,loginacct,userpswd,username,email FROM t_admin WHERE loginacct = ?";

        List<Admin> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Admin.class), username);

        Admin admin = list.get(0);

        // 2.给Admin设置角色权限信息
        List<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        authorities.add(new SimpleGrantedAuthority("UPDATE"));

        // 3.把admin对象和authorities封装到UserDetails
        String userpswd = admin.getUserpswd();
        return new User(username, userpswd,authorities);
    }
}
