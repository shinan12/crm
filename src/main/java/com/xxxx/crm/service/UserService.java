package com.xxxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.dao.RoleMapper;
import com.xxxx.crm.dao.UserMapper;
import com.xxxx.crm.dao.UserRoleMapper;
import com.xxxx.crm.query.UserModel;
import com.xxxx.crm.query.UserQuery;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.utils.Md5Util;
import com.xxxx.crm.utils.PhoneUtil;
import com.xxxx.crm.utils.UserIDBase64;
import com.xxxx.crm.vo.User;
import com.xxxx.crm.vo.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.*;

/**
 * @author shinan
 * @version 1.0
 */
@Service
public class UserService extends BaseService<User,Integer> {
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserRoleMapper userRoleMapper;
    /** 用户登录
     2.校验参数是否为空
     如果为空，抛异常
     3.调用dao层查询通过用户名查询数据库数据
     如果未查到，抛异常(用户不存在)
     4.校验前台传来的密码和数据库中的密码是否一致 (前台密码加密后再校验)
     如果不一致，抛异常(密码错误)
     5.封装ResultInfo对象给前台（根据前台需求：usermodel对象封装后传到前台使用）
     */
    public ResultInfo loginCheck(String userName,String userPwd) {
        //校验参数是否为空
        checkLoginData(userName,userPwd);
        //调用dao层查询通过用户名查询数据库数据
        User user = userMapper.queryUserByName(userName);
        //判断账号是否存在
        AssertUtil.isTrue(user == null,"账号不存在");
        //校验前台传来的密码和数据库中的密码是否一致 (前台密码加密后再校验)
        checkLoginPwd(user.getUserPwd(),userPwd);
        //封装ResultInfo对象给前台（根据前台需求：usermodel对象封装后传到前台使用
        ResultInfo resultInfo = buildResultInfo(user);
        return resultInfo;
    }
    /**
     * 修改密码
     */
    public void userUpdate(Integer userId,String oldPassword,String newPassword,String confirmPassword) {
        //确保用户是否是登录状态获取cookie中的id 非空 查询数据库
        AssertUtil.isTrue(userId == null,"用户未登录");
        User user = userMapper.selectByPrimaryKey(userId);
        AssertUtil.isTrue(user == null,"用户状态异常");

        //校验密码数据
        checkUpdateData(oldPassword,newPassword,confirmPassword, user.getUserPwd());
        // 执行修改操作，返回ResultInfo
        user.setUserPwd(Md5Util.encode(newPassword));
        user.setUpdateDate(new Date());
        //判断是否修改成功
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"密码修改失败");
    }

    /**
     * 密码校验
     *               1.确保用户是否是登录状态获取cookie中的id 非空 查询数据库
     *               2.校验老密码 非空  老密码必须要跟数据库中密码一致
     *               3.新密码    非空  新密码不能和原密码一致
     *               4.确认密码  非空  确认必须和新密码一致
     *               5.执行修改操作，返回ResultInfo
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     * @param dbPassword
     */
    private void checkUpdateData(String oldPassword, String newPassword, String confirmPassword, String dbPassword) {
        //校验老密码  非空  老密码必须要跟数据库中密码一致
        AssertUtil.isTrue(StringUtils.isBlank(oldPassword),"原始密码不存在");
        AssertUtil.isTrue(!dbPassword.equals(Md5Util.encode(oldPassword)),"新密码不能和原密码一致");
        //新密码    非空  新密码不能和原密码一致
        AssertUtil.isTrue(StringUtils.isBlank(newPassword),"新密码不能为空");
        AssertUtil.isTrue(oldPassword.equals(newPassword),"新密码不能和原密码一致");
        //确认密码  非空  确认必须和新密码一致
        AssertUtil.isTrue(StringUtils.isBlank(confirmPassword),"确认密码不能为空");
        AssertUtil.isTrue(!confirmPassword.equals(newPassword),"确认密码必须和新密码一致");
    }

    /**
     * 准备前台cookie需要的数  usermodel
     * @param user
     * @return
     */
    private ResultInfo buildResultInfo(User user) {
        ResultInfo resultInfo = new ResultInfo();
        //封装userModel cookie需要的数据
        UserModel userModel = new UserModel();
        //加密userid
        String id = UserIDBase64.encoderUserID(user.getId());
        userModel.setUserId(id);
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        resultInfo.setResult(userModel);
        return resultInfo;
    }


    /**
     * 校验前台传来的密码和数据库中的密码是否一致 (前台密码加密后再校验)
     * @param userPwd
     * @param dbPwd
     */
    private void checkLoginPwd(String dbPwd, String userPwd) {
        String encodePwd = Md5Util.encode(userPwd);
        AssertUtil.isTrue(!encodePwd.equals(dbPwd),"用户密码错误");
    }

    /**
     * 用户登录非空参数校验
     * @param userName
     * @param userPwd
     */
    private void checkLoginData(String userName, String userPwd) {
        AssertUtil.isTrue(StringUtil.isEmpty(userName),"用户名不能为空");
        AssertUtil.isTrue(StringUtil.isEmpty(userPwd),"密码不能为空");
    }

    /**
     * 多条件查询
     * @param query
     * @return
     */
    public Map<String,Object> queryUserByParams(UserQuery query) {
        Map<String, Object> map = new HashMap<>();
        PageHelper.startPage(query.getPage(),query.getLimit());
        List<User> users = userMapper.queryUserByParams(query);
        PageInfo<User> userPageInfo = new PageInfo<>(users);
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", userPageInfo.getTotal());
        map.put("data", userPageInfo.getList());
        return map;
    }

    /**
     * 添加用户
     * 1. 参数校验
     * * 用户名 非空 唯一性
     * * 邮箱 非空
     * * 手机号 非空 格式合法
     * * 2. 设置默认参数
     * * isValid 1
     * * creteDate 当前时间
     * * updateDate 当前时间
     * * userPwd 123456 -> md5加密
     * * 3. 执行添加，判断结果
     * @param user
     */
    public void saveUser(User user) {
        AssertUtil.isTrue(StringUtils.isBlank(user.getUserName()),"用户名不能为空");
        AssertUtil.isTrue(null != userMapper.queryUserByName(user.getUserName()),"用户名已存在");
        checkParams(user.getEmail(),user.getPhone());
        user.setUpdateDate(new Date());
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUserPwd(Md5Util.encode("123456"));
        //AssertUtil.isTrue(userMapper.insertSelective(user) < 1,"用户添加失败");

        //执行添加操作，设置对应sql属性，主键返回到user对象中
        AssertUtil.isTrue(userMapper.insertHasKey(user) < 1, "用户添加失败");
        //绑定角色给用户
        relationUserRole(user.getId(), user.getRoleIds());
    }

    /**
     * 给用户绑定角色
     * @param id 用户id
     * @param roleIds 角色id
     */
    private void relationUserRole(Integer id, String roleIds) {
        //修改角色操作：查询是否原来就有角色，如果有那么直接删除再绑定新角色
        Integer count = userRoleMapper.countUserRole(id);
        if(count > 0) {
            AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUid(id)!= count,"原有角色删除失败");
        }
       AssertUtil.isTrue(roleIds == null,"角色不存在");
        List<UserRole> urs = new ArrayList<>();
        String[] splits = roleIds.split("," );
        for(String idStr:splits) {
            UserRole userRole = new UserRole();
            userRole.setUserId(id);
            userRole.setRoleId(Integer.parseInt(idStr));
            userRole.setCreateDate(new Date());
            userRole.setUpdateDate(new Date());

            urs.add(userRole);
        }
        AssertUtil.isTrue(userRoleMapper.insertBatch(urs) != splits.length,"角色绑定失败");
    }

    /**
     * 用户添加修改参数校验
     * @param email
     * @param phone
     */
    private void checkParams(String email, String phone) {
        AssertUtil.isTrue(StringUtils.isBlank(email),"请输入邮箱地址");
        AssertUtil.isTrue(StringUtils.isBlank(phone),"手机号不能为空");
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone),"手机号格式不正确");
    }

    /**
     * 更新用户
     * * 1. 参数校验
     * * id 非空 记录必须存在
     * * 用户名 非空 唯一性
     * * email 非空
     * * 手机号 非空 格式合法
     * * 2. 设置默认参数
     * * updateDate
     * * 3. 执行更新，判断结果
     * @param user
     */
    public void updateUser(User user){
        //id     非空|存在
        AssertUtil.isTrue(null == user.getId() || null == userMapper.selectByPrimaryKey(user.getId()),"数据异常请重试");
        //用户名  非空 | 唯一
        AssertUtil.isTrue(user.getUserName() == null,"用户名不能为空");
        // 名称唯一
        User dbUser = userMapper.queryUserByName(user.getUserName());
        AssertUtil.isTrue(dbUser != null && user.getId() != dbUser.getId(),"用户名已存在");
        //校验邮箱和手机号
        checkParams(user.getEmail(),user.getPhone());
        //设置默认值
        user.setUpdateDate(new Date());
        //执行修改操作
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) < 1,"用户修改失败");
        //修改绑定的角色
        relationUserRole(user.getId(),user.getRoleIds());
    }

    /**
     * 批量删除
     * @param ids
     */
    public void deleteUsers(Integer[] ids) {
        AssertUtil.isTrue(ids == null || ids.length < 1,"未选中删除的数据");
        AssertUtil.isTrue(userMapper.deleteUsers(ids) != ids.length,"用户删除失败");
    }
}
