package com.example.firstdome.controller;


import cn.dev33.satoken.util.SaResult;
import com.example.firstdome.entitys.LoginUser;
import com.example.firstdome.mapper.UserMapper;
import com.example.firstdome.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/")
@RequiredArgsConstructor
public class UserController {
private  final UserService userService;
private   final UserMapper userMapper;

@GetMapping("login")
    public SaResult login(LoginUser loginUser) {
    SaResult result = userService.login(loginUser);
    return result;
}



}
