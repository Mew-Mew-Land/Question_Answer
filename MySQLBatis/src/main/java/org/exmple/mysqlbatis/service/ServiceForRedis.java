package org.exmple.mysqlbatis.service;

import java.util.Random;

public interface ServiceForRedis {

    long Cache_Time=3600;
    String Catch_NULL="{ }";
    default long random(int a/*10的倍数*/){
        Random random = new Random();
        return random.nextInt(a);
    }
}
