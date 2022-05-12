package com.xxxx.crm.controller;
import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.query.CusDevPlanQuery;
import com.xxxx.crm.service.CusDevPlanService;
import com.xxxx.crm.service.SaleChanceService;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.vo.CusDevPlan;
import com.xxxx.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
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
@RequestMapping("cus_dev_plan")
public class CusDevPlanController extends BaseController {
    @Resource
    private SaleChanceService saleChanceService;
    @Resource
    private CusDevPlanService cusDevPlanService;
    /**
     * 客户开发主页面
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "cusDevPlan/cus_dev_plan";
    }

    /**
     * 进入开发计划项数据页面
     * @param sId
     * @return
     */
    @RequestMapping("toCusDevPlanDataPage")
    public String toCusDevPlanDataPage(Integer sId, HttpServletRequest request) {
        AssertUtil.isTrue(sId == null,"数据异常，请重试");
        SaleChance saleChance = saleChanceService.selectByPrimaryKey(sId);
        if(saleChance != null ){
            request.setAttribute("saleChance",saleChance);
        }
        return "cusDevPlan/cus_dev_plan_data";
    }

    /**
     * * 查询营销机会的计划项数据列表
     * @param query
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryByParams(CusDevPlanQuery query) {
        return cusDevPlanService.queryByParams(query);
    }

    /**
     * 添加计划项
     * @param cusDevPlan
     */
    @RequestMapping("save")
    @ResponseBody
    public ResultInfo addCusDevPlan(CusDevPlan cusDevPlan) {
        cusDevPlanService.addCusDevPlan(cusDevPlan);
        return success("计划项添加成功");
    }
    /**
     * 修改计划项
     * @param cusDevPlan
     */
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateCusDevPlan(CusDevPlan cusDevPlan) {
        cusDevPlanService.updateCusDevPlan(cusDevPlan);
        return success("计划项修改成功");
    }
    /**
     * 修改计划项
     * @return
     */
    @RequestMapping("toAddOrUpdatePage")
    public String updateCusDevPlan(Integer id,Integer sId,HttpServletRequest request) {
        if(id != null) {
            CusDevPlan cusDevPlan = cusDevPlanService.selectByPrimaryKey(id);
            AssertUtil.isTrue(null == cusDevPlan,"计划项数据异常请重试");
            request.setAttribute("cusDevPlan",cusDevPlan);
        }
        request.setAttribute("sId",sId);
        return "cusDevPlan/add_update";
    }

    /**
     * 删除修改计划
     * @param id
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo delete(Integer id) {
        cusDevPlanService.delete(id);
        return success();

    }
}
