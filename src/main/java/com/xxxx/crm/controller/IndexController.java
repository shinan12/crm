package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.service.PermissionService;
import com.xxxx.crm.service.UserService;
import com.xxxx.crm.utils.LoginUserUtil;
import com.xxxx.crm.vo.Permission;
import com.xxxx.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController extends BaseController {

    @Resource
    private UserService userService;
    @Resource
    private PermissionService permissionService;

    /**
     * 系统登录页
     * @return
     */
    @RequestMapping(value = {"/index","/"})
    public String index(){
        return "index";
    }

    
    // 系统界面欢迎页
    @RequestMapping("welcome")
    public String welcome(){
        return "welcome";
    }

    /**
     * 后端管理主页面
     * @return
     */
    @RequestMapping("main")
    public String main(HttpServletRequest request){
        //将用户数据查询出来，设置到作用域中
        int id = LoginUserUtil.releaseUserIdFromCookie(request);
        User user = userService.selectByPrimaryKey(id);
        request.setAttribute("user",user);

        //当用户登录后进去主页面之前将当前用户具备所有的权限码查询出来，放在session作用域中
        List<String> permissions = permissionService.selectUserRolevalue(id);
        request.getSession().setAttribute("permissions",permissions);
        return "main";
    }
}
