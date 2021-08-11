package com.chw.kill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan("com.chw.kill.mapper")
public class KillApplication {
    public static void main(String[] args) {
        SpringApplication.run(KillApplication.class, args);
    }
}
