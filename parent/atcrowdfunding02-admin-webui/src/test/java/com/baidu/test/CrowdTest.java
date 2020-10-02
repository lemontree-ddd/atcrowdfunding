package com.baidu.test;

/**
 * @Author Administrator
 * @create 2020/7/12 0012 16:40
 */

import com.baidu.entity.Admin;
import com.baidu.entity.Role;
import com.baidu.mapper.AdminMapper;
import com.baidu.mapper.RoleMapper;
import com.baidu.service.api.AdminService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 推荐使用Spring的项目就可以使用spring-test单元测试模块，会自动注入我们需要的组件
 * 1.导入Spring-test jar包
 * 2.@ContextConfiguration 指定spring配置文件的位置
 * 3.直接autowired
 * @Author Administrator
 * @create 2020/7/11 0011 16:12
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-persist-mybatis.xml","classpath:spring-persist-tx.xml"})
public class CrowdTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleMapper roleMapper;

    @Test
    public void testRoleSave(){
        for (int i=0;i<256;i++){
            roleMapper.insert(new Role(null,"role"+i));
        }
    }
    @Test
    public void test(){
        for (int i=0;i<238;i++){
            adminMapper.insert(new Admin(null,"loginAcct"+i,"userpswd"+i,"userName"+i,"email"+i,"createTime"+null));
        }
    }

    @Test
    public void testTx() {
        Admin admin = new Admin(null, "hhwesdfw", "123213", "蓉蓉", "lisa@qq.com", null);
        adminService.saveAdmintest(admin);

    }

    @Test
    public void testLog(){

        //1.获取Logger对象，这里传入的Class对象就是当前打印日志的类
        Logger logger = LoggerFactory.getLogger(CrowdTest.class);

        //2.根据不同日志级别打印日志
        logger.debug("Hello I am Debug");
        logger.debug("Hello I am Debug");
        logger.debug("Hello I am Debug");

        logger.info("info level");
        logger.info("info level");
        logger.info("info level");

        logger.warn("Warn level");
        logger.warn("Warn level");
        logger.warn("Warn level");

        logger.error("error level");
        logger.error("error level");
        logger.error("error level");

    }

    @Test
    public void testInsertAdmin(){
        Admin admin = new Admin(null, "zhangsan", "321123", "张三", "zhangsan@qq.com", null);
        int count = adminMapper.insert(admin);
        System.out.println("受影响行数："+count);
    }

    @Test
    public void testConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
    }
}
