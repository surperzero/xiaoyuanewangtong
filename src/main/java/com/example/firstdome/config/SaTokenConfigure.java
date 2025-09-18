package com.example.firstdome.config;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.serializer.impl.SaSerializerTemplateForJdkUseBase64;
import cn.dev33.satoken.stp.StpUtil;
import com.example.firstdome.entitys.properties.SaTokenProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;

@Configuration
@RequiredArgsConstructor
public class SaTokenConfigure implements WebMvcConfigurer {
    private final SaTokenProperties saTokenProperties;
    @PostConstruct
    public void rewriteComponent() {
        // 设置序列化方案: JDK序列化 + Base64
        SaManager.setSaSerializerTemplate(new SaSerializerTemplateForJdkUseBase64());
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 创建 SaInterceptor 并配置路径匹配
        registry.addInterceptor(new SaInterceptor(handler -> {
                    SaRouter.match("/**")  // 匹配所有路径
                            .notMatch(saTokenProperties.getWhitelist()) // 排除路径
                            .check(r -> StpUtil.checkLogin()); // 执行登录检查
                }))
                .addPathPatterns("/**"); // 全局拦截所有请求路径
    }
    //配置跨域请求
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*") //指定跨域地址  .allowedOrigins("http://localhost:3000", "https://yourdomain.com")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
//    /**
//     * 重写Sa-Token框架内部算法策略
//     */
//    @PostConstruct
//    public void rewiteSaStrategy(){
//        // 重写 Token 生成策略
//        SaStrategy.instance.createToken = (loginId, loginType) -> {
//            return JwtUtil.createJWT(loginId.toString());    // 随机60位长度字符串
//        };
//    }
}