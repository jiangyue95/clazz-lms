package com.jiangyue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan // 开启了@WebServlet注解的类，会自动注册Servlet、Filter、Listener
@SpringBootApplication
public class ClazzLmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClazzLmsApplication.class, args);
    }

}
