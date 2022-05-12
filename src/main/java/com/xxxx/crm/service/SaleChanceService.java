package com.xxxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.SaleChanceMapper;
import com.xxxx.crm.query.SaleChanceQuery;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.utils.PhoneUtil;
import com.xxxx.crm.vo.SaleChance;
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
public class SaleChanceService extends BaseService<SaleChance,Integer> {
    @Resource
    private SaleChanceMapper saleChanceMapper;

    /**
     * 多条件分页查询营销机会 (BaseService 中有对应的方法)
     * @param saleChanceQuery
     * @return
     */
    public Map<String, Object> queryByParams(SaleChanceQuery saleChanceQuery) {
        Map<String, Object> map = new HashMap<>();
        //开启分页
        PageHelper.startPage(saleChanceQuery.getPage(), saleChanceQuery.getLimit());
        List<SaleChance> saleChances = saleChanceMapper.queryByParams(saleChanceQuery);
        //按照分页条件格式化数据
        PageInfo<SaleChance> saleChancePageInfo = new PageInfo<>(saleChances);
        map.put("code",0);
        map.put("msg", "");
        map.put("count", saleChancePageInfo.getTotal());
        map.put("data", saleChancePageInfo.getList());
        return map;
    }

    /**
     * 更新开发状态
     * @param id
     * @param devResult
     */
    public void updateDevResult(Integer id,Integer devResult) {
        SaleChance saleChance = saleChanceMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(id == null || null == saleChance,"更新数据不存在");
        AssertUtil.isTrue(null == devResult,"更新状态不存在");
        saleChance.setDevResult(devResult);
        saleChance.setUpdateDate(new Date());
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance) < 1,"状态修改失败");
    }

    /**
     *
     * * 营销机会数据添加
     * * 1.参数校验
     * * customerName:非空
     * * linkMan:非空
     * * linkPhone:非空 11位手机号
     * * 2.设置相关参数默认值
     * * state:默认未分配 如果选择分配人 state 为已分配
     * * assignTime:如果 如果选择分配人 时间为当前系统时间
     * * devResult:默认未开发 如果选择分配人devResult为开发中 0-未开发 1-开发中 2-开发成功
     * 3-开发失败
     * * isValid:默认有效数据(1-有效 0-无效)
     * * createDate updateDate:默认当前系统时间
     * * 3.执行添加 判断结果
     *  营销机会数据添加
     * @param saleChance
     */
    public void saveSaleChance(SaleChance saleChance) {
        // 1.参数校验
        checkParams(saleChance.getCustomerName(), saleChance.getLinkMan(), saleChance.getLinkPhone());
        // 2.设置相关参数默认值

        saleChance.setIsValid(1);
        saleChance.setUpdateDate(new Date());
        saleChance.setCreateDate(new Date());
        //判断用户是否设置了分配人
        if(StringUtils.isBlank(saleChance.getAssignMan())) {
            // 未选择分配人
            saleChance.setState(0);
            saleChance.setDevResult(0);
        } else {
            // 选择分配人
            saleChance.setAssignTime(new Date());
            saleChance.setState(1);
            saleChance.setDevResult(1);
        }
        // 3.执行添加 判断结果
        AssertUtil.isTrue(saleChanceMapper.insertSelective(saleChance)<1,"营销机会数据添加失败");
    }

    /**
     * 参数校验
     * @param customerName
     * @param linkMan
     * @param linkPhone
     */
    private void checkParams(String customerName, String linkMan, String linkPhone) {
        AssertUtil.isTrue(StringUtils.isBlank(customerName),"请输入客户名");
        AssertUtil.isTrue(StringUtils.isBlank(linkMan),"请输入联系人");
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone),"请输入手机号");
        AssertUtil.isTrue(!PhoneUtil.isMobile(linkPhone),"手机号格式不正确");
    }

    /**
     * 营销机会数据更新
     * @param saleChance
     */
    public void updateSaleChance(SaleChance saleChance) {
        //判断id是否存在
        AssertUtil.isTrue(saleChance.getId() == null,"数据异常，请重试");
        //校验非空参数
        checkParams(saleChance.getCustomerName(), saleChance.getLinkMan(), saleChance.getLinkPhone());
        //设置默认值
        saleChance.setUpdateDate(new Date());

        //通过现有的id查询修改前的数据
        SaleChance dbSaleChance = saleChanceMapper.selectByPrimaryKey(saleChance.getId());
        AssertUtil.isTrue(dbSaleChance == null, "数据异常，请重试");

        //判断原有数据中是否有分配人
        if(StringUtils.isBlank(dbSaleChance.getAssignMan())) {
            //进入当前判断说明修改前没有分配人
            //判断修改后是否有分配人
            if(!StringUtils.isBlank(dbSaleChance.getAssignMan())) {
                //修改后有分配人
                saleChance.setAssignTime(new Date());
                saleChance.setState(1);
                saleChance.setDevResult(1);
            }
            //修改后没有分配人，什么都不干
        } else {
            //进入当前判断说明修改前没有分配人
            //判断修改后是否有分配人
            if(StringUtils.isBlank(saleChance.getAssignMan())) {
                //修改后没有分配人
                saleChance.setAssignTime(null);
                saleChance.setState(0);
                saleChance.setDevResult(0);
            } else {
                //修改后有分配人
                //判断前后的分配人是否有变化
                if(!dbSaleChance.getAssignMan().equals(saleChance.getAssignMan())) {
                    //不是一个人
                    saleChance.setAssignTime(new Date());
                } else {
                    //相同分配人
                    saleChance.setAssignTime(new Date());
                }
            }
        }
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance)<1,"营销数据修改失败");
    }

    /**
     * 查询所有销售人员
     * @return
     */
    public List<Map<String,Object>> queryAllSales() {
        return saleChanceMapper.queryAllSales();
    }

}
