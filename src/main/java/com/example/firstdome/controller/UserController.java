package com.example.firstdome.controller;


import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.example.firstdome.comment.Valida;
import com.example.firstdome.entitys.LoginUser;
import com.example.firstdome.entitys.User;
import com.example.firstdome.mapper.UserMapper;
import com.example.firstdome.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

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

    @GetMapping("logout")
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok("退出成功");
    }

    @GetMapping("id")
    public SaResult selectUser(@RequestParam Long id) {
    return userService.selectUser(id);
    }
    @GetMapping("update")
    public SaResult updateUser(@Validated(Valida.Update.class) @RequestBody User user) {

       return userService.updateUser(user);
    }
    @GetMapping("save")
    public SaResult saveUser(@Validated(Valida.Create.class) @RequestBody User user) {
        return userService.savaUser(user);
    }
    @GetMapping("registerUser")
    public SaResult registerUser(@Validated(Valida.Create.class) @RequestBody User user) {
    return userService.registerUser(user);
    }

    /**
     * 删除用户接口
     * @param id
     * @return
     */
    @GetMapping("delete")
    public SaResult deleteUser(@RequestParam Long id) {

    return userService.deleteUser(id);
    }
 @GetMapping("page")
    public SaResult page(@RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer size,@RequestParam  Map<String, Object> seachMap )

   {
        return userService.selectPage(current,size,seachMap);
 }
}
