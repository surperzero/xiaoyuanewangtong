package com.example.firstdome.service.Imp;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.firstdome.entitys.LoginUser;
import com.example.firstdome.entitys.User;
import com.example.firstdome.mapper.UserMapper;
import com.example.firstdome.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServicelmp extends ServiceImpl<UserMapper,User> implements UserService {

    @Override
    public SaResult login(LoginUser loginUser) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>();
        queryWrapper.eq(User::getPhone,loginUser.getPhone());
        User user = baseMapper.selectOne(queryWrapper);
        if(user==null) return SaResult.error("没有此用户");
        if(user.getStatus()!=0){
            return SaResult.error("账户处于异常状态");
        }else if(user.getPassword().equals(loginUser.getPassword())) {
            //登录成功
            StpUtil.login(loginUser.getPhone());
            return  SaResult.data(StpUtil.getTokenInfo());
        }else {
            return SaResult.error("密码错误");
        }
    }
}
