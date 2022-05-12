package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.query.RoleQuery;
import com.xxxx.crm.service.RoleService;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.vo.Role;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author shinan
 * @version 1.0
 */
@Controller
@RequestMapping("role")
public class RoleController extends BaseController {
    @Resource
    private RoleService roleService;

    /**
     * 查询角色列表
     * @return
     */
    @RequestMapping("queryAllRoles")
    @ResponseBody
    public List<Map<String,Object>> queryAllRoles(Integer id) {
        return roleService.queryAllRoles(id);
   }

    /**
     * 跳转到授权页面
     * @return
     */
   @RequestMapping("toAddGrantPage")
   public String toAddGrantPage(Integer rId, HttpServletRequest request) {
       AssertUtil.isTrue(rId == null,"角色不存在");
       request.setAttribute("roleId",rId);
        return "role/grant";
   }

   @RequestMapping("addGrant")
   @ResponseBody
   public ResultInfo addGrant(Integer roleId,Integer[] mIds) {
       roleService.addGrant(roleId,mIds);
       return success();
   }


    /**
     * 打开角色页面
     * @return
     */
   @RequestMapping("index")
    public String index() {
       return "role/role";
   }

    /**
     * 多条件分页查询角色数据
     * @param roleQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
   public Map<String,Object> userList(RoleQuery roleQuery) {
        return roleService.queryByParamsForTable(roleQuery);
   }

    /**
     * 打开添加/修改页面
     * @param id
     * @param model
     * @return
     */
   @RequestMapping("addOrUpdateRolePage")
   public String addUserPage(Integer id, Model model) {
        if(id != null) {
            model.addAttribute("role", roleService.selectByPrimaryKey(id));
        }
       return "role/add_update";
   }

    /**
     * 添加角色数据
     * @param role
     * @return
     */
   @RequestMapping("save")
   @ResponseBody
   public ResultInfo saveRole(Role role) {
       roleService.saveRole(role);
       return success("角色记录添加成功");
   }

    /**
     * 修改角色数据
     * @param role
     * @return
     */
   @RequestMapping("update")
   @ResponseBody
   public ResultInfo updateRole(Role role) {
       roleService.updateRole(role);
       return success("角色记录修改成功");
   }

    /**
     * 删除角色
     * @param id
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteUser(Integer id) {
        roleService.deleteRole(id);
        return success("角色记录删除成功");
    }

}
