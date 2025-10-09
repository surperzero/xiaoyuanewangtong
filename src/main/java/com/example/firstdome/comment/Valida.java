package com.example.firstdome.comment;

import javax.validation.groups.Default;

public interface Valida {
    interface Login extends Default {} //登录校验组
    interface Create extends Default{} //新增校验组
    interface Update extends Default{} //修改校验组
    interface Delete extends Default{} //删除校验组
    interface Query extends Default{}  //查询校验组
}
