package com.baidu.mvc.handler;

import com.baidu.entity.Menu;
import com.baidu.service.api.MenuService;

import com.baidu.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * @Author Administrator
 * @Date 2020/5/12
 */
//@Controller
//@ResponseBody
@RestController    // @Controller + @ResponseBody = @RestController
public class MenuHandler {
    @Autowired
    private MenuService menuService;

    //@ResponseBody
    @RequestMapping("menu/remove.json")//.json不重要
    public ResultEntity<Menu> removeMenu(@RequestParam("id") Integer id) throws InterruptedException{

        menuService.removeMenu(id);
        return ResultEntity.successWithoutData();
    }

    //@ResponseBody
    @RequestMapping("/menu/update.json")//.json不重要
    public ResultEntity<Menu> updateMenu(Menu menu) throws InterruptedException{

        menuService.updateMenu(menu);
        return ResultEntity.successWithoutData();
    }

    //@ResponseBody
    @RequestMapping("/menu/save.json")//.json不重要
    public ResultEntity<Menu> saveMenu(Menu menu) throws InterruptedException{

        menuService.saveMenu(menu);
        return ResultEntity.successWithoutData();
    }

    //@ResponseBody
    @RequestMapping("/menu/get/whole/tree.json")//.json不重要
    public ResultEntity<Menu> getWholeTree() {
        List<Menu> menuList = menuService.getAll();
        // 声明一个变量用来存储找到的根节点
        Menu root = null;

        // 3.创建一个Map对象来存储id和Menu对象的对应关系便于查找父节点
        Map<Integer, Menu> menuMap = new HashMap<>();

        // 4.遍历MenuList对象填充MenuMap
        for (Menu menu : menuList) {
            Integer id = menu.getId();
            menuMap.put(id, menu);
        }
        // 5. 再次MenuList，查找父节点 组装根节点
        for (Menu menu : menuList) {

            // 7.获取当前menu对象的pid属性值
            Integer pid = menu.getPid();

            // 8.如果pid为Null,判定为父节点
            if (pid == null) {
                root = menu;
                // 如果当前节点为根节点，那么就肯定没有父节点
                continue;
            }
            // 9.如果pid不为null，这说明有父节点 根据pid到Map对象中查找对应的Menu对象
            Menu father = menuMap.get(pid);
            // 10.将当前节点存入父节点的children集合
            father.getChildren().add(menu);
        }
        // 11.经过上面的运算，根节点包含整个树结构，返回根节点，就是返回整个树
        return ResultEntity.successWithData(root);

    }

}
//    public ResultEntity<Menu> getWholeTreeOld(){
//        List<Menu> menuList=menuService.getAll();
//        // 声明一个变量用来存储找到的根节点
//        Menu root = null;
//
//        for (Menu menu : menuList){
//            Integer pid = menu.getPid();
//            if (pid == null){
//                root=menu;
//                // 找到跟节点后继续下个循环
//                continue;
//            }
//            // pid不为null有父节点
//            for (Menu menuFather : menuList) {
//                Integer id = menuFather.getId();
//                if (Objects.equals(pid,id)){
//                    menuFather.getChildren().add(menu);
//                    break;
//                }
//
//
//            }
//
//        }
//        return ResultEntity.sucessWithData(root) ;
//    }
//}
