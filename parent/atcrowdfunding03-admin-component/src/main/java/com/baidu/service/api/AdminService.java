package com.baidu.service.api;

import com.baidu.entity.Admin;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @Author Administrator
 * @create 2020/7/12 0012 22:47
 */

public interface AdminService {

    void saveAdmintest(Admin admin);

    void saveAdmin(Admin admin);

    List<Admin> getAll();

    Admin getAdminByLoginAcct(String loginAcct, String userPswd);

    PageInfo<Admin> getPageInfo(String keyword,Integer pageNum,Integer pageSize);


    void remove(Integer adminId);


    Admin getAdminById(Integer adminId);

    void update(Admin admin);

    void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList);

    Admin getAdminByLoginAcct(String username);
}
