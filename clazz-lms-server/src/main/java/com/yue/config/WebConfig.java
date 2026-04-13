package com.yue.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Custom Web Config class
 * Not in use, this is an example
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

//    @Autowired
//    private DemoInterceptor demoInterceptor;

//    @Autowired
//    private TokenInterceptor tokenInterceptor;

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(demoInterceptor).addPathPatterns("/**");

//        registry.addInterceptor(tokenInterceptor)
//                .addPathPatterns("/*") // 拦截所有请求
//                .excludePathPatterns("/login"); // 登录接口不拦截
//    }
}
