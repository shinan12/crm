package com.xxxx.crm.dao;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.query.SaleChanceQuery;
import com.xxxx.crm.vo.SaleChance;

import java.util.List;
import java.util.Map;

public interface SaleChanceMapper extends BaseMapper<SaleChance,Integer> {
    public List<SaleChance> queryByParams(SaleChanceQuery saleChanceQuery);

    /**
     * 查询所有销售人员的数据
     *
     * @param
     * @return
     */
    public List<Map<String, Object>> queryAllSales();
}