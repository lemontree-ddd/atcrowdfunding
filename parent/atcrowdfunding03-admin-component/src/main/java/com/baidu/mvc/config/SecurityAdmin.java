package com.baidu.mvc.config;

import com.baidu.entity.Admin;
import org.springframework.aop.interceptor.AbstractMonitoringInterceptor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

/**
 * @Author Administrator
 * @create 2020/7/25 0025 10:44
 */
public class SecurityAdmin extends User {

    private static final long serialVersionUID = 1L;

    // 原始的Admin对象，包含Admin对象的全部属性
    private Admin originalAdmin;

    public SecurityAdmin(Admin originalAdmin,
                         // 创建角色、权限信息的集合
                         List<GrantedAuthority> authorities) {

        // 调用父类构造器
        super(originalAdmin.getLoginAcct(),originalAdmin.getUserPswd(), authorities);

        // 给本类的 this.originalAdmin 赋值
        this.originalAdmin = originalAdmin;

        // 将原始Admin对象的密码擦除
        this.originalAdmin.setUserPswd(null);
    }

    public Admin getOriginalAdmin() {
        return originalAdmin;
    }

}
