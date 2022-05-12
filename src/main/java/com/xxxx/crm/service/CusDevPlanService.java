package com.xxxx.crm.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.CusDevPlanMapper;
import com.xxxx.crm.dao.SaleChanceMapper;
import com.xxxx.crm.query.CusDevPlanQuery;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.vo.CusDevPlan;
import com.xxxx.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class CusDevPlanService extends BaseService<CusDevPlan,Integer> {
    @Resource
    private CusDevPlanMapper cusDevPlanMapper;
    @Resource
    private SaleChanceMapper saleChanceMapper;

    /**
     * 分页查询客户计划表
     * @param query
     * @return
     */
    public Map<String,Object> queryByParams(CusDevPlanQuery query) {
        Map<String, Object> map = new HashMap<>();
        PageHelper.startPage(query.getPage(),query.getLimit());
        List<CusDevPlan> cusDevPlans = cusDevPlanMapper.queryByParams(query);
        PageInfo<CusDevPlan> cusDevPlanPageInfo = new PageInfo<>(cusDevPlans);
        map.put("code", 0);
        map.put("msg", "");
        map.put("count",cusDevPlanPageInfo.getTotal());
        map.put("data", cusDevPlanPageInfo.getList());
        return map;
    }

    /**
     * 添加计划项 * 1. 参数校验
     *              * 营销机会ID 非空 记录必须存在
     *              * 计划项内容 非空
     *              * 计划项时间 非空 *
     *             2. 设置参数默认值
     *             * is_valid
     *             * crateDate
     *             * updateDate
     *             3. 执行添加，判断结果
     */
    @Transactional
    public void addCusDevPlan(CusDevPlan cusDevPlan) {
        checkParams(cusDevPlan.getSaleChanceId(), cusDevPlan.getPlanItem(), cusDevPlan.getPlanDate());
        cusDevPlan.setIsValid(1);
        cusDevPlan.setCreateDate(new Date());
        cusDevPlan.setUpdateDate(new Date());
        AssertUtil.isTrue(insertSelective(cusDevPlan)<1,"计划项记录添加失败");
    }

    /**
     * 更新计划项
     * @param cusDevPlan
     */
    @Transactional
    public void updateCusDevPlan(CusDevPlan cusDevPlan) {
        AssertUtil.isTrue(null == cusDevPlan.getId() || null == saleChanceMapper.selectByPrimaryKey(cusDevPlan.getSaleChanceId()),"待更新记录不存在");
        checkParams(cusDevPlan.getSaleChanceId(), cusDevPlan.getPlanItem(), cusDevPlan.getPlanDate());

        cusDevPlan.setUpdateDate(new Date());
        AssertUtil.isTrue(updateByPrimaryKeySelective(cusDevPlan)<1,"计划项记录更新失败");
    }

    /**
     * 校验添加和修改的数据
     * @param saleChanceId
     * @param planItem
     * @param planDate
     */
    private void checkParams(Integer saleChanceId, String planItem, Date planDate) {
        AssertUtil.isTrue(null == saleChanceId || saleChanceMapper.selectByPrimaryKey(saleChanceId)==null,"营销机会ID不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(planItem),"计划项内容不能为空");
        AssertUtil.isTrue(planDate == null,"计划项日期不能为空");
    }

    /**
     * 删除计划项
     * @param id
     */
    @Transactional
    public void delete(Integer id) {
        CusDevPlan cusDevPlan = cusDevPlanMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(id == null || null == cusDevPlan,"待删除计划项不存在 ");
        cusDevPlan.setIsValid(0);
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan)<1,"计划项删除失败");
    }
}
