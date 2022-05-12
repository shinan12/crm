package com.xxxx.crm.dao;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.model.ModelModel;

import com.xxxx.crm.vo.Module;
import org.apache.ibatis.annotations.Param;
import org.springframework.ui.Model;

import java.util.List;

public interface ModuleMapper extends BaseMapper<Module,Integer> {
    /**
     * 查询所有资源
     * @return
     */
    public List<ModelModel> queryAllModules();

    /**
     * 查询所有模块资源
     * @return
     */
    public List<Module> queryModules();

    /**
     * 同级下名称唯一
     * @param grade
     * @param moduleName
     * @return
     */
    Module queryModuleByGradeName(@Param("grade") Integer grade, @Param("moduleName") String moduleName);

    Module queryModulByGradeAUrl(@Param("grade")Integer grade, @Param("url")String url);

    Module queryModuleById(Integer parentId);

    Module queryModuleByOptValue(String optValue);


    Integer queryCountModuleByParentId(Integer mId);

    Integer deleteModuleByMid(Integer mId);
}