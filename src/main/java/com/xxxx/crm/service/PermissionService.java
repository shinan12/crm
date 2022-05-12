package com.xxxx.crm.service;

import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.PermissionMapper;
import com.xxxx.crm.vo.Permission;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author shinan
 * @version 1.0
 */
@Service
public class PermissionService extends BaseService<Permission,Integer> {
    @Resource
    private PermissionMapper permissionMapper;

    public List<String > selectUserRolevalue(int id) {
        return permissionMapper.selectUserRolevalue(id);
    }
}
