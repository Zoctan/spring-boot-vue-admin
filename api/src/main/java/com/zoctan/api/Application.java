package com.zoctan.api;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

import static com.zoctan.api.core.ProjectConstant.MAPPER_PACKAGE;

/**
 * @author Zoctan
 * @date 2018/06/09
 */
@EnableEncryptableProperties
@SpringBootApplication
@EnableTransactionManagement(proxyTargetClass = true)
@MapperScan(basePackages = MAPPER_PACKAGE)
public class Application {
    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}
