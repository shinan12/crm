package com.xxxx.crm.service;


import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.ModuleMapper;
import com.xxxx.crm.dao.PermissionMapper;
import com.xxxx.crm.dao.RoleMapper;
import com.xxxx.crm.model.ModelModel;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.vo.Module;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shinan
 * @version 1.0
 */
@Service
public class ModelService extends BaseService<Module,Integer> {
    @Resource
    private ModuleMapper moduleMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private PermissionMapper permissionMapper;

    /**
     * 查询所有的模块资源
     * @param rId
     * @return
     */
    public List<ModelModel> queryAllModules(Integer rId) {
        AssertUtil.isTrue(rId == null||roleMapper.selectByPrimaryKey(rId)==null,"角色不存在");
        List<Integer> mIds = permissionMapper.selectPermissionByRid(rId);
        List<ModelModel> treeModels = moduleMapper.queryAllModules();
        for(ModelModel treeModel:treeModels) {
            Integer id = treeModel.getId();
            if(mIds.contains(id)) {
                treeModel.setChecked(true);
                treeModel.setOpen(true);
            }
        }
        return treeModels;
    }

    /**
     * 查询所有的模块资源
     * @return
     */
    public Map<String,Object> queryModules() {
        Map<String, Object> result = new HashMap<>();
        List<Module> modules = moduleMapper.queryModules();
        AssertUtil.isTrue(modules == null || modules.size() < 1,"资源数据异常");
        result.put("count", modules.size());
        result.put("data", modules);
        result.put("code", 0);
        result.put("msg", "");
        return result;
    }

    /**
     * 模块添加
     *      *   1.数据校验
     *             模块名称
     *               非空，同级唯一
     *             地址 URL
     *               二级菜单：非空，同级唯一
     *             父级菜单 parentId
     *               一级：null | -1
     *               二级|三级：非空 | 必须存在
     *             层级 grade
     *               非空  值必须为 0|1|2
     *             权限码
     *                非空  唯一
     *           2.默认值
     *             is_valid
     *             updateDate
     *             createDate
     *           3.执行添加操作  判断受影响行数
     * @param module
     */
    public void moduleAdd(Module module) {
        //层级 grade  非空  值必须为 0|1|2
        AssertUtil.isTrue(module.getGrade() == null,"层级不能为空");
        AssertUtil.isTrue(!(module.getGrade() == 0 || module.getGrade() == 1 || module.getGrade() == 2),"层级有误");
        //模块名称 非空  同级唯一
        AssertUtil.isTrue(StringUtils.isBlank(module.getModuleName()),"模块名称不能为空");
        Module dbModule = moduleMapper.queryModuleByGradeName(module.getGrade(),module.getModuleName());
        AssertUtil.isTrue(dbModule != null,"模块名称已存在");
        // 二级菜单URL：非空，同级唯一
        if(module.getGrade() == 1) {
            AssertUtil.isTrue(StringUtils.isBlank(module.getUrl()),"模块地址不能为空");
            dbModule = moduleMapper.queryModulByGradeAUrl(module.getGrade(), module.getUrl());
            AssertUtil.isTrue(dbModule != null,"地址已存在，请重新输入");
        }
        //父级菜单  二级|三级：非空 | 必须存在
        if(module.getGrade() == 1 || module.getGrade() == 2) {
            AssertUtil.isTrue(module.getParentId() == null,"父ID不能为空");
            dbModule = moduleMapper.queryModuleById(module.getParentId());
            AssertUtil.isTrue(dbModule == null,"父ID不存在");
        }
        //权限码  非空  唯一
        AssertUtil.isTrue(StringUtils.isBlank(module.getOptValue()),"权限码不能为空");
        dbModule = moduleMapper.queryModuleByOptValue(module.getOptValue());
        AssertUtil.isTrue(dbModule != null,"权限码已存在");

        module.setIsValid((byte)1);
        module.setCreateDate(new Date());
        module.setUpdateDate(new Date());
        AssertUtil.isTrue(moduleMapper.insertSelective(module)<1,"模块添加失败");

    }

    public void moduleUpdate(Module module) {
        // id  非空，并且资源存在
        AssertUtil.isTrue(module.getId() == null,"待删除的资源不存在");
        Module dbModule = moduleMapper.selectByPrimaryKey(module.getId());
        AssertUtil.isTrue(dbModule == null,"系统异常");

        //层级 grade  非空  值必须为 0|1|2
        AssertUtil.isTrue(module.getGrade() == null,"层级不能为空");
        AssertUtil.isTrue(!(module.getGrade() == 0 || module.getGrade() == 1 || module.getGrade() == 2),"层级有误");

        //模块名称 非空  同级唯一
        AssertUtil.isTrue(StringUtils.isBlank(module.getModuleName()),"模块名称不能为空");
        dbModule = moduleMapper.queryModuleByGradeName(module.getGrade(),module.getModuleName());
        AssertUtil.isTrue(dbModule != null && !(module.getId().equals(dbModule.getId())),"模块名称已存在");

        // 二级菜单URL：非空，同级唯一
        if(module.getGrade() == 1){
            AssertUtil.isTrue(StringUtils.isBlank(module.getUrl()),"模块地址不能为空");
            dbModule = moduleMapper.queryModulByGradeAUrl(module.getGrade(),module.getUrl());
            AssertUtil.isTrue(dbModule != null && !(module.getId().equals(dbModule.getId())),"地址已存在，请重新输入");
        }

        //父级菜单  二级|三级：非空 | 必须存在
        if(module.getGrade() == 1 || module.getGrade() == 2){
            AssertUtil.isTrue(module.getParentId() == null,"父ID不能为空");
            dbModule = moduleMapper.queryModuleById(module.getParentId());
            AssertUtil.isTrue(dbModule == null && !(module.getId().equals(dbModule.getId())),"父ID不存在");
        }

        //权限码  非空  唯一
        AssertUtil.isTrue(StringUtils.isBlank(module.getOptValue()),"权限码不能为空");
        dbModule = moduleMapper.queryModuleByOptValue(module.getOptValue());
        AssertUtil.isTrue(dbModule != null && !(module.getId().equals(dbModule.getId())),"权限码已存在");

        //默认值
        module.setUpdateDate(new Date());

        //执行修改操作
        AssertUtil.isTrue(moduleMapper.updateByPrimaryKeySelective(module) < 1,"资源修改失败");
    }

    public void moduleDelete(Integer mId) {
        AssertUtil.isTrue(mId == null,"系统异常请重试");
        AssertUtil.isTrue(selectByPrimaryKey(mId) == null,"待删除数据不存在");
        Integer count = moduleMapper.queryCountModuleByParentId(mId);
        AssertUtil.isTrue(count > 0,"该模块下存在子模块，不能删除");
        count = permissionMapper.queryCountByModuleId(mId);
        if(count > 0) {
            AssertUtil.isTrue(permissionMapper.deletePermissionByModuleId(mId) != count,"权限删除失败");
        }
        AssertUtil.isTrue(moduleMapper.deleteModuleByMid(mId) < 1,"资源删除失败");
    }
}
