package com.zoctan.api;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import com.zoctan.api.core.ProjectConstant;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableEncryptableProperties
@SpringBootApplication
@EnableCaching
@EnableTransactionManagement
@MapperScan(basePackages = ProjectConstant.MAPPER_PACKAGE)
public class Application {
    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
