package com.example.firstdome.entitys;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;


import java.io.Serializable;
import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_user")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    //id
    private Long id;
    //名字
    private String name;
    //电话
    private String phone;
    //密码
    private String password;
    //简介
    private String remark;
    //状态
    private Integer status;
    //提交时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime submitTime;
}