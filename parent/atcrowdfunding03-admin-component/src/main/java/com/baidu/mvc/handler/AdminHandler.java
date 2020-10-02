package com.baidu.mvc.handler;

import com.baidu.constant.CrowdConstant;
import com.baidu.entity.Admin;
import com.baidu.service.api.AdminService;
import com.github.pagehelper.PageInfo;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @Author Administrator
 * @create 2020/7/15 0015 18:33
 */
@Controller
public class AdminHandler extends HttpServlet {

    @Autowired
    private AdminService adminService;

    @RequestMapping("/admin/update.html")
    public String update(Admin admin,
                         @RequestParam("pageNum") Integer pageNum,
                         @RequestParam("keyword") String keyword
    ){
        adminService.update(admin);
        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
    }

    @RequestMapping("/admin/to/edit/page.html")
    public String edit(@RequestParam("adminId") Integer adminId, ModelMap modelMap){

        Admin admin = adminService.getAdminById(adminId);
        modelMap.addAttribute("admin",admin);
        return "admin-edit";
    }

    @RequestMapping("/admin/save.html")
    public String save(Admin admin, HttpServletRequest request){

        adminService.saveAdmin(admin);

        // 重定向到分页页面，使用重定向是为了避免刷新浏览器重复提交表单
        return "redirect:/admin/get/page.html?pageNum=" + 0x7fffffff;
    }

    @RequestMapping("/admin/remove/{adminId}/{pageNum}/{keyword}.html")
    public String remove(
            @PathVariable("adminId") Integer adminId,
            @PathVariable("pageNum") Integer pageNum,
            @PathVariable("keyword") String keyword){
        // 执行参数
        adminService.remove(adminId);
        // 不带数据 查询不了数值
        // return "admin-page";
        // forword：重复删除
        // 重定向到admin/get/page.html地址 同时为了保持原本的页面以及关键字，需要附加pageNum keyword
        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
    }

    // 分页显示用户信息和 带关键字查询
    @RequestMapping("/admin/get/page.html")
    public String getPageInfo(
            // 配置默认值
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize,
            ModelMap modelMap
    ) {
        PageInfo<Admin> pageInfo = adminService.getPageInfo(keyword, pageNum, pageSize);
        modelMap.addAttribute(CrowdConstant.ATTR_NAME_NAME_INFO,pageInfo);
        return "admin-page";
    }

    /**
     * 退出功能
     * @return
     */
    @RequestMapping("/admin/do/logout.html")
    public String logOut(){
        return "redirect:/admin/to/login/page.html";
    }


    // 错误原因：由于访问地址弄成/admin/do/login.html使得不经过登录提交直接跳转，使得参数为null
    // 应该admin/to/login/page.html进行访问再由登录跳转admin/do/login.html，获得数据
    // SpringSecurity使用后，不会使用这个地址
    @RequestMapping("/admin/do/login.html")
    public String doLogin(
                          @RequestParam("loginAcct") String loginAcct,
                          @RequestParam("userPswd") String userPswd,
                          HttpSession session
    ) {
/*
       // 进行账号密码判断
        String loginAcct1 = request.getParameter("loginAcct");
        String userPswd1 = request.getParameter("userPswd");
        System.out.println("登录账号"+loginAcct1);
        System.out.println("登录密码"+userPswd1);
*/

        //调用Service方法执行登录检查
        //如果能够返回admin说明登录成功，如果账号、密码不正确则会抛出异常
        Admin admin = adminService.getAdminByLoginAcct(loginAcct, userPswd);

        //将登录成功对象返回的admin对象存入Session域
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN,admin);
        // 为了避免跳转到后台主页面再刷新浏览器导致重复提交登录表单，重定向到目标页面。
        // 并且重定向的话也不能直接就admin.main 浏览器无法直接访问WEB-INF/下面的jsp页面
        //借助view-controller 跳转到主页面
        return "redirect:/admin/to/main/page.html";
    }

}
