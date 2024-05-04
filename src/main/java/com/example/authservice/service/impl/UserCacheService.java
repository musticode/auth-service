package com.example.authservice.service.impl;

import com.example.authservice.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class UserCacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    public UserCacheService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void addUserToCache(String username, User user){
        redisTemplate.opsForHash().put("USER", username, user);
    }

    public User getUserFromCache(String username){
        return (User)redisTemplate.opsForHash().get("USER", username);
    }

    public Map<Object, Object> getAllUsersFromCache(){
        return redisTemplate.opsForHash().entries("USER");
    }



}
