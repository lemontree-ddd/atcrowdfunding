package com.baidu.security.entity;

/**
 * @Author Administrator
 * @create 2020/7/23 0023 9:46
 */
public class Admin {

    private Integer id;
    private String loginacct;
    private String userpswd;
    private String username;
    private String emain;

    public Admin() {
    }

    public Admin(Integer id, String loginacct, String userpswd, String username, String emain) {
        this.id = id;
        this.loginacct = loginacct;
        this.userpswd = userpswd;
        this.username = username;
        this.emain = emain;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLoginacct() {
        return loginacct;
    }

    public void setLoginacct(String loginacct) {
        this.loginacct = loginacct;
    }

    public String getUserpswd() {
        return userpswd;
    }

    public void setUserpswd(String userpswd) {
        this.userpswd = userpswd;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmain() {
        return emain;
    }

    public void setEmain(String emain) {
        this.emain = emain;
    }
}
