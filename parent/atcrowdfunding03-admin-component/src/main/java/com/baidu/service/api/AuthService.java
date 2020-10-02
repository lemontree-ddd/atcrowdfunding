package com.baidu.service.api;

import com.baidu.entity.Auth;
import com.baidu.util.ResultEntity;

import java.util.List;
import java.util.Map;

/**
 * @Author Administrator
 * @create 2020/7/21 0021 10:27
 */
public interface AuthService {
    List<Auth> getAll();

    //根据adminId权限信息
    List<Integer> getAssignedAuthIdByRoleId(Integer roleId);

    void saveRoleAuthrealthinship(Map<String, List<Integer>> map);

    //根据adminId查询已分配权限
    List<String> getAssignedAuthNameByAdminId(Integer adminId);
}
