package com.example.firstdome.service.Imp;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.firstdome.config.SaTokenConfigure;
import com.example.firstdome.entitys.LoginUser;
import com.example.firstdome.entitys.User;
import com.example.firstdome.entitys.properties.SaTokenProperties;
import com.example.firstdome.mapper.UserMapper;
import com.example.firstdome.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserServicelmp extends ServiceImpl<UserMapper,User> implements UserService {

    private final UserMapper userMapper;
    private final SaTokenConfigure saTokenConfigure;
    private final SaTokenProperties saTokenProperties;

    public UserServicelmp(UserMapper userMapper, SaTokenConfigure saTokenConfigure, SaTokenProperties saTokenProperties) {
        this.userMapper = userMapper;
        this.saTokenConfigure = saTokenConfigure;
        this.saTokenProperties = saTokenProperties;
    }

    @Override
//    public SaResult login(LoginUser loginUser) {
//        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>();
//        queryWrapper.eq(User::getPhone,loginUser.getPhone());
//        User user = baseMapper.selectOne(queryWrapper);
//        if(user==null) return SaResult.error("没有此用户");
//        if(user.getStatus()!=0){
//            return SaResult.error("账户处于异常状态");
//        }else if(user.getPassword().equals(loginUser.getPassword())) {
//            //登录成功
//            StpUtil.login(loginUser.getPhone());
//            return  SaResult.data(StpUtil.getTokenInfo());
//        }else {
//            return SaResult.error("密码错误");
//        }
//    }
    //对应的当我们注册会将密码加密后，与需要将登录逻辑修改一下
    public SaResult login(LoginUser loginUser) {
        //查询用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>();
        queryWrapper.eq(User::getPhone,loginUser.getPhone());
        User user = userMapper.selectOne(queryWrapper);
        if(user==null) return SaResult.error("没有此用户");
        String password = SaSecureUtil.aesDecrypt(saTokenProperties.getVerifyKey(), user.getPassword());
        if(user.getStatus()!=0){
            return SaResult.error("账户处于异常状态");
        }else if(loginUser.getPassword().equals(password)) {
            //登录成功
            StpUtil.login(loginUser.getPhone());
            return  SaResult.data(StpUtil.getTokenInfo());
        }else {
            return SaResult.error("密码错误");
        }
    }

    @Override
    public SaResult selectUser(Long id){
        //查询信息
        User user = userMapper.selectById(id);
        if (user == null) return SaResult.error("查询不出此用户");
        user.setPassword(null);
        return SaResult.data(user);
    }

    @Override
    public SaResult updateUser(User user) {
        //查询信息
        User user1 = userMapper.selectById(user.getId());
        if (user1==null) return SaResult.error("查无此人");

        if (user.getPassword()!=null&&!"".equals(user.getPassword().trim())){
            String s = SaSecureUtil.aesEncrypt(saTokenProperties.getVerifyKey(), user.getPassword());
            user.setPassword(s);
        }

        return userMapper.updateById(user)>0?SaResult.ok("修改成功"):SaResult.error("修改失败");
    }

    @Override
    public SaResult registerUser(User user) {
        // 先查看电话号码是否已注册
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, user.getPhone());
        User existUser = userMapper.selectOne(wrapper);
        if (existUser != null) {
            return SaResult.error("电话号码已注册");
        }
        
        // 加密密码
        String encryptedPassword = SaSecureUtil.aesEncrypt(saTokenProperties.getVerifyKey(), user.getPassword());
        user.setPassword(encryptedPassword);
        
        // 设置默认状态
        if (user.getStatus() == null) {
            user.setStatus(0); // 默认正常状态
        }
        
        // 插入用户
        return userMapper.insert(user) > 0 ? SaResult.ok("注册成功") : SaResult.error("注册失败");
    }
    @Override
    public SaResult savaUser(User user) {
        //1.先查看电话号码是否有注册过
        LambdaQueryWrapper<User> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone,user.getPhone());
        User user1 = userMapper.selectOne(wrapper);
        if (user1!=null) return SaResult.error("电话号码已注册");
//        2.密码加密
        String ciphertext = SaSecureUtil.aesEncrypt(saTokenProperties.getVerifyKey(), user.getPassword());
        user.setPassword(ciphertext);
        //保存数据
        return userMapper.insert(user)>0?SaResult.ok("注册成功"):SaResult.error("注册失败");
    }

    @Override
    public SaResult deleteUser(Long id) {
        User user = userMapper.selectById(id);
        if (user ==null) return SaResult.error("查无此人");
        userMapper.deleteById(id);

        return SaResult.ok("删除成功");
    }

    @Override
    public SaResult selectPage(Integer current, Integer size, Map<String, Object> seachMap) {
        Page<User> page = new Page<>(current, size);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (seachMap.get("starTime")!=null&&!"".equals(seachMap.get("starTime"))){
            wrapper.ge(User::getSubmitTime,seachMap.get("starTime"));
        }
        if (seachMap.get("endTime")!=null&&!"".equals(seachMap.get("endTime"))){
            wrapper.le(User::getSubmitTime,seachMap.get("endTime"));
        }
        Page<User> page1 = userMapper.selectPage(page,wrapper);
        for (User record : page1.getRecords()) {
            record.setPassword(null);
            if (record.getStatus() == 0) {
                record.setStatuname("生效");
            }else {
                record.setStatuname("无效");
            }
        }
        return SaResult.data(page1);
    }

}
