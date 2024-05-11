package org.exmple.mysqlbatis;

import org.exmple.mysqlbatis.config.InitDataBase;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@ServletComponentScan
@SpringBootApplication
public class MySqlBatisApplication {

    public static void main(String[] args) {
        InitDataBase aa=new InitDataBase();
        try{
            aa.init();
        }catch (Exception ignored){

        }

        SpringApplication.run(MySqlBatisApplication.class, args);
    }

}
