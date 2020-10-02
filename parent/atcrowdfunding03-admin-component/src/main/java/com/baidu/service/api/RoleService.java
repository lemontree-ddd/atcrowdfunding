package com.baidu.service.api;

import com.baidu.entity.Role;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @Author Administrator
 * @create 2020/7/17 0017 18:37
 */
public interface RoleService {

    PageInfo<Role> getPageInfo(Integer pageNum,Integer pageSize,String keyword);

    void saveRole(Role role);

    void updateRole(Role role);

    void remove(List<Integer> roleIdList);

    // 根据adminId查询已分配的角色
    List<Role> getAssignedRole(Integer adminId);

    // 根据admin查询没有分配的角色
    List<Role> getUnAssignedRole(Integer adminId);
}
