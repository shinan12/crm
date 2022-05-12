package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.query.UserQuery;
import com.xxxx.crm.service.UserService;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.utils.LoginUserUtil;
import com.xxxx.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author shinan
 * @version 1.0
 */
@Controller
@RequestMapping("user")
public class UserController extends BaseController {
    @Resource
    private UserService userService;

    /**
     * 用户登录
     * @param userName
     * @param userPwd
     * @return
     */
    @PostMapping("login")
    @ResponseBody
    public ResultInfo login(String userName,String userPwd) {
        return userService.loginCheck(userName, userPwd);
    }
    /**
     * 修改密码
     */
    @PostMapping("update")
    @ResponseBody
    public ResultInfo update(HttpServletRequest request,String oldPassword, String newPassword, String confirmPassword) {
        //获取登录用户的id
        int id = LoginUserUtil.releaseUserIdFromCookie(request);
        userService.userUpdate(id,oldPassword,newPassword,confirmPassword);

        return success();
    }
    //打开修改密码页面
    @RequestMapping("toPasswordPage")
    public String toPasswordPage() {
        return "user/password";
    }

    /**
     * 多条件查询
     * @param query
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryUserByParams(UserQuery query) {
        return userService.queryUserByParams(query);
    }

    /**
     * 进入用户界面
     * @return
     */
    @RequestMapping("index")
    public String index() {
        return "user/user";
    }

    /**
     * 添加用户
     * @param user
     * @return
     */
    @RequestMapping("save")
    @ResponseBody
    public ResultInfo saveUser(User user) {
        userService.saveUser(user);
        return success("用户添加成功");
    }

    /**
     * 更新用户
     * @param user
     * @return
     */
    @RequestMapping("updateUser")
    @ResponseBody
    public ResultInfo updateUser(User user) {
        userService.updateUser(user);
        return success("用户更新成功");
    }

    /**
     * 进入用户添加或更新页面
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("addOrUpdateUserPage")
    public String toUpdateAddPage(Integer id,HttpServletRequest request) {
        //如果是修改操作，那么需要将数据显示在页面中
        if(id != null) {
            User user = userService.selectByPrimaryKey(id);
            AssertUtil.isTrue(user == null,"数据异常请重试");
            request.setAttribute("user",user);
        }
        return "user/add_update";
    }
    @RequestMapping("deleteBatch")
    @ResponseBody
    public ResultInfo deleteUsers(Integer[] ids) {
        userService.deleteUsers(ids);
        return success("用户记录删除成功");
    }

}
