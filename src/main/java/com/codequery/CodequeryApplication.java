package com.codequery;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication(scanBasePackages = {"com.codequery.*"})
@MapperScan("com.codequery.dao")
@Slf4j
public class CodequeryApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(CodequeryApplication.class, args);

    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(CodequeryApplication.class);
    }
}
