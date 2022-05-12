package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.model.ModelModel;
import com.xxxx.crm.service.ModelService;
import com.xxxx.crm.vo.Module;
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
@RequestMapping("module")
public class ModuleController extends BaseController {
    @Resource
    private ModelService modelService;


    /**
     * 查询所有资源
     * @return
     */
    @RequestMapping("queryAllModules")
    @ResponseBody
    public List<ModelModel> queryAllModules(Integer rId) {
        return modelService.queryAllModules(rId);
    }

    /**
     * 查询所有资源资源管理使用
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryModules() {
        return modelService.queryModules();
    }

    /**
     * 跳转到模块界面
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "module/module";
    }

    @RequestMapping("toAdd")
    public String toAdd(Integer grade, Integer parentId, Model model){
        model.addAttribute("grade",grade);
        model.addAttribute("parentId",parentId);
        return "module/add";
    }
    /**
     *
     * @param module
     * @return
     */
    @RequestMapping("add")
    @ResponseBody
    public ResultInfo moduleAdd(Module module) {
        modelService.moduleAdd(module);
        return success("资源添加成功");
    }
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo moduleUpdate(Module module) {
        modelService.moduleUpdate(module);
        return success("资源更新成功");
    }
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo moduleDelete(Integer mId) {
        modelService.moduleDelete(mId);
        return success("资源删除成功");
    }

    /**
     * 修改资源
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("updateModulePage")
    public String updateModulePage(Integer id, HttpServletRequest request) {
        Module module = modelService.selectByPrimaryKey(id);
        request.setAttribute("module",module);
        return "module/update";
    }

}
