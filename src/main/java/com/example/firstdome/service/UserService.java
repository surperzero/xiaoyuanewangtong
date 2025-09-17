package com.example.firstdome.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.firstdome.entitys.LoginUser;
import com.example.firstdome.entitys.User;
import com.example.firstdome.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface UserService extends IService<User> {
    SaResult login(LoginUser loginUser);
}
