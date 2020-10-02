package com.baidu.service.impl;

import com.baidu.constant.CrowdConstant;
import com.baidu.entity.Admin;
import com.baidu.entity.AdminExample;
import com.baidu.exception.LoginAcctAlreadyInUseException;
import com.baidu.exception.LoginAcctAlreadyInUseForUpdateException;
import com.baidu.exception.LoginFailedException;
import com.baidu.mapper.AdminMapper;
import com.baidu.service.api.AdminService;
import com.baidu.util.CrowdUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @Author Administrator
 * @create 2020/7/12 0012 22:46
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;
    
    private Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void saveAdmintest(Admin admin) {
        adminMapper.insert(admin);
    }

    @Override
    public void saveAdmin(Admin admin) {

        String userPswd = admin.getUserPswd();
        // 旧加密形式
        //userPswd = CrowdUtil.md5(userPswd);
        userPswd = bCryptPasswordEncoder.encode(userPswd);
        admin.setUserPswd(userPswd);

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = format.format(date);
        admin.setCreateTime(createTime);

        try {
            adminMapper.insert(admin);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("异常全类名="+e.getClass().getName());
            // 检测当前捕获的异常对象，如果是 DuplicateKeyException 类型说明是账号重复导致的
            if (e instanceof DuplicateKeyException){
                throw new LoginAcctAlreadyInUseException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
        }

        //throw new RuntimeException();
    }

    @Override
    public List<Admin> getAll() {
        return adminMapper.selectByExample(new AdminExample());
    }

    @Override
    public void remove(Integer adminId) {
        adminMapper.deleteByPrimaryKey(adminId);
    }

    @Override
    public Admin getAdminById(Integer adminId) {
        return adminMapper.selectByPrimaryKey(adminId);
    }

    @Override
    public void update(Admin admin) {

        try {
            adminMapper.updateByPrimaryKeySelective(admin);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("异常全类名="+e.getClass().getName());

            if (e instanceof DuplicateKeyException){
                throw new LoginAcctAlreadyInUseForUpdateException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
        }
    }

    @Override
    public void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList) {
        //删除旧数据，再加新数据
        adminMapper.deleteOldRelationship(adminId);
        // 根据roleIdList和adminId保存新的关联性
        if (roleIdList != null && roleIdList.size()>0){
            adminMapper.insertNewRelationship(adminId,roleIdList);
        }
    }

    @Override
    public Admin getAdminByLoginAcct(String username) {
        AdminExample example = new AdminExample();
        AdminExample.Criteria criteria = example.createCriteria();
        criteria.andLoginAcctEqualTo(username);
        List<Admin> admins = adminMapper.selectByExample(example);

        /*//2.判断Admin对象是否为null
        if (admins == null || admins.size() == 0){
            System.out.println("admin为空");
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        if (admins.size()>1){
            System.out.println("admin为大于1");
            throw new RuntimeException(CrowdConstant.MESSAGE_SYSTEM_LOGIN_NOT_UNIQUE);
        }*/
        Admin admin = admins.get(0);
        return admin;
    }

    /**
     * 开启分页功能 并且将查询的对象封装
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize) {

        //1.调用PageHelper的静态方法开启分页功能
        PageHelper.startPage(pageNum, pageSize);

        //2.执行查询
        List<Admin> list = adminMapper.selectAdminByKeyword(keyword);

        return new PageInfo<>(list);
    }


    @Override
    public Admin getAdminByLoginAcct(String loginAcct, String userPswd) {

        // 1.根据登录账号查询admin对象
        //   创建AdminExample对象
        AdminExample adminExample = new AdminExample();
        //   创建Criteria对象
        AdminExample.Criteria criteria = adminExample.createCriteria();
        //   在Criteria对象中封装查询条件
        criteria.andLoginAcctEqualTo(loginAcct);
        //   调用AdminMapper的方法执行查询
        List<Admin> list = adminMapper.selectByExample(adminExample);

        //2.判断Admin对象是否为null
        if (list == null || list.size() == 0){
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        if (list.size()>1){
            throw new RuntimeException(CrowdConstant.MESSAGE_SYSTEM_LOGIN_NOT_UNIQUE);
        }

        //3.如果Admin对象为null则抛出异常
        Admin admin = list.get(0);
        if (admin == null){
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        //4.如果Admin对象不为null则将数据库密码从Admin对象中取出
        String userPswdDB = admin.getUserPswd();
        //5.将表单提交的明文进行加密
        String userPswdForm = CrowdUtil.md5(userPswd);

        //6.对密码进行比较 如果比较结果是不一致则抛出异常
        if (!Objects.equals(userPswdDB,userPswdForm)){
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        //8.结果一致则返回Admin对象
        return admin;
    }
}
