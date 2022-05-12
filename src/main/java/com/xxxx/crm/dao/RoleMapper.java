package com.xxxx.crm.dao;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.vo.Role;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseMapper<Role,Integer> {
    /**
     * 查询对应的角色名称和id反馈给前台使用
     * @param id
     * @return
     */
    public List<Map<String,Object>> queryAllRoles(Integer id);

    /**
     * 通过名称查询角色数据
     * @param roleName
     * @return
     */
    public Role queryRoleByRoleName(String roleName);

    /**
     * 通过名称查询角色数据
     * @param cancelByRoleName
     * @return
     */
    //public Role queryRoleByRoleName(String cancelByRoleName);
}