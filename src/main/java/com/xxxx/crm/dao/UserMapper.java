package com.xxxx.crm.dao;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.query.UserQuery;
import com.xxxx.crm.vo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


public interface UserMapper extends BaseMapper<User,Integer> {
    /**
     * 通过用户名称查询数据
     * @param name
     * @return
     */
    public User queryUserByName(String name);

    /**
     * 多条件分页查询
     * @param query
     * @return
     */
    public List<User> queryUserByParams(UserQuery query);

    /**
     * 批量删除
     * @param ids
     * @return
     */
    public Integer deleteUsers(Integer[] ids);
}