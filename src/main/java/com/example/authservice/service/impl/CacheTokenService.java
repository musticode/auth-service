package com.example.authservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@Slf4j
public class CacheTokenService {

    private final StringRedisTemplate stringRedisTemplate;
    private final RedisTemplate<String, Object> redisTemplate;


    public CacheTokenService(StringRedisTemplate stringRedisTemplate, RedisTemplate<String, Object> redisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisTemplate = redisTemplate;
    }


    public void saveData(String key, Object data) {
        redisTemplate.opsForValue().set(key, data);
    }

    public Object getData(String key) {
        return redisTemplate.opsForValue().get(key);
    }



    public void cacheToken(String username, String token) {
        // Cache the token in Redis with a TTL (e.g., 60 minutes)
        if (!username.isEmpty() || !username.isBlank()){
            stringRedisTemplate.opsForValue().set("token:" + username, token, Duration.ofMinutes(60));
            log.info("Username : {} , token : {}", username, token);
        }
        log.info("Username empty or null");
    }

    public String getCachedToken(String username) {
        // Retrieve the token from Redis cache
        return stringRedisTemplate.opsForValue().get("token:" + username);
    }


}
