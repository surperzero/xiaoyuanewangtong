package com.example.firstdome.entitys.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;
import java.util.List;

/**
 * Sa-Token 配置属性类
 * 用于读取 satoken-config.yml 中的配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "satoken-config")
@AllArgsConstructor
@NoArgsConstructor
public class SaTokenProperties implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 白名单路径列表
     */
    private List<String> whitelist;
    
    /**
     * 验证密钥
     */
    private String verifyKey;
}