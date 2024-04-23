package org.exmple.mysqlbatis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class MySqlBatisApplication {

    public static void main(String[] args) {
        SpringApplication.run(MySqlBatisApplication.class, args);
    }

}
