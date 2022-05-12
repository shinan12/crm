package com.xxxx.crm.dao;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.vo.Permission;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission,Integer> {
    /**
     * 判断当前角色是否有资源
     * @param roleId
     * @return
     */
    Integer countPermission(Integer roleId);

    /**
     * 将原有的资源全部删除
     * @param roleId
     * @return
     */
    Integer deletePermissionByRoleId(Integer roleId);

    /**
     * 当前角色拥有的权限
     * @param rId
     * @return
     */
    List<Integer> selectPermissionByRid(Integer rId);

    List<String> selectUserRolevalue(Integer id);

    /**
     * 通过模块id查询关联权限
     * @param mId
     * @return
     */
    Integer queryCountByModuleId(Integer mId);

    Integer deletePermissionByModuleId(Integer mId);
}