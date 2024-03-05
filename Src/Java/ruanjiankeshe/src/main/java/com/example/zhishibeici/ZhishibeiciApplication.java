package com.example.zhishibeici;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import java.io.*;
import java.util.*;

@SpringBootApplication
public class ZhishibeiciApplication {

    @Autowired
    private RedisTemplate redisTemplate = null;

    @Autowired
    private StringRedisTemplate stringRedisTemplate = null;

    public static void main(String[] args) {
        SpringApplication.run(ZhishibeiciApplication.class, args);
    }

    private void initRedisTemplate() {
        RedisSerializer stringSerializer = redisTemplate.getStringSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
    }

}
