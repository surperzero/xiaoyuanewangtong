package com.example.firstdome.entitys;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.firstdome.comment.Valida;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @NotNull(message = "名字不能为空",groups ={Valida.Create.class})
    private Long id;
    //名字
    @NotNull(message = "用户名不等于空",groups ={Valida.Create.class})
    private String name;
    //电话
    @NotNull(message = "电话号码不等于空",groups ={Valida.Create.class})
    private String phone;
    //密码
    @NotBlank(message = "密码不等于空",groups ={Valida.Create.class})
    @Size(min = 6,max = 16)
    private String password;
    //简介
    private String remark;
    //状态
    private Integer status;
    //提交时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime submitTime;
    @TableField(exist = false)
    private String statuname;
}