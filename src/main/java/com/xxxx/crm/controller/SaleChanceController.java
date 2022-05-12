package com.xxxx.crm.controller;

import com.xxxx.crm.annotation.RequirePermission;
import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.query.SaleChanceQuery;
import com.xxxx.crm.service.SaleChanceService;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.utils.CookieUtil;
import com.xxxx.crm.utils.LoginUserUtil;
import com.xxxx.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
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
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {
    @Resource
    private SaleChanceService saleChanceService;

    /**
     * 多条件分页查询营销机会
     * @param saleChanceQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryByParams(SaleChanceQuery saleChanceQuery,Integer flag,HttpServletRequest request) {
        // 查询参数 flag=1 代表当前查询为开发计划数据，设置查询分配人参数
        if(flag != null && flag == 1) {
            //获取当前登录用户的id
            Integer id = LoginUserUtil.releaseUserIdFromCookie(request);
            saleChanceQuery.setAssignMan(id);
        }
        return saleChanceService.queryByParams(saleChanceQuery);
    }

    /**
     * 进入营销机会页面
     * @return
     */
    @RequestMapping("index")
    public String index() {
        return "saleChance/sale_chance";
    }

    /**
     * 添加数据
     * @param request
     * @param saleChance
     * @return
     */
    @RequestMapping("save")
    @ResponseBody
    @RequirePermission(code = 101002)
    public ResultInfo saveSaleChance(HttpServletRequest request, SaleChance saleChance) {
        //获取创建人
        String userName = CookieUtil.getCookieValue(request, "userName");
        saleChance.setCreateMan(userName);
        saleChanceService.saveSaleChance(saleChance);
        return success();
    }

    @RequestMapping("toAddUpdatePage")
    public String toAddUpdatePage(Integer id,HttpServletRequest request) {
        if(id != null) {
            SaleChance saleChance = saleChanceService.selectByPrimaryKey(id);
            AssertUtil.isTrue(saleChance == null,"数据异常请重试");
            request.setAttribute("saleChance", saleChance);
        }
        return "saleChance/add_update";
    }

    /**
     * 修改数据
     * @param saleChance
     * @return
     */
    @ResponseBody
    @RequestMapping("update")
    public ResultInfo update( SaleChance saleChance) {
        saleChanceService.updateSaleChance(saleChance);
        return success();
    }
    @RequestMapping("queryAllSales")
    @ResponseBody
    public List<Map<String,Object>> queryAllSales() {
        return saleChanceService.queryAllSales();
    }

    /**
     * 更新开发状态
     * @param id
     * @param devResult
     * @return
     */
    @RequestMapping("updateDevResult")
    @ResponseBody
    public ResultInfo updateDevResult(Integer id,Integer devResult) {
        saleChanceService.updateDevResult(id, devResult);
        return success();
    }
}
