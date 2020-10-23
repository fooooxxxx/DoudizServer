package com.clx.doudiz;

import com.clx.doudiz.domain.enums.RoomStatus;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.clx.doudiz.domain.mapper")

public class DoudizApplication {

    public static void main(String[] args) {
        SpringApplication.run(DoudizApplication.class, args);
    }
}
