package com.example.authservice.config;

import com.example.authservice.constant.CacheConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

import java.time.Duration;

@Configuration
@EnableCaching
@Slf4j
public class RedisConfig {

    @Value("${spring.cache.redis.host}")
    private String redisHost;

    @Value("${spring.cache.redis.port}")
    private int redisPort;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisHost, redisPort);
        configuration.setPassword("my-password");
        log.info("Connected to : {} {}", redisHost, redisPort);


        return new LettuceConnectionFactory(configuration);
    }



    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        return RedisCacheManager.create(connectionFactory);
    }
    @Bean
    public RedisTemplate<String, Object> redisTemplate(){
        final RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new GenericToStringSerializer<Object>(Object.class));
        redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }



//    @Bean
//    public RedisConnectionFactory redisConnectionFactory(){
//        return new LettuceConnectionFactory("localhost" , 6379);
//    }
//
//    @Bean
//    public RedisTemplate<String, Object> redisTemplate(){
//        final RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setHashKeySerializer(new GenericToStringSerializer<Object>(Object.class));
//        redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
//        redisTemplate.setValueSerializer(new StringRedisSerializer());
//        redisTemplate.setConnectionFactory(redisConnectionFactory());
//        return redisTemplate;
//    }
//
//    @Bean(value = "cacheManager")
//    public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory){
//        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
//                .disableCachingNullValues()
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()));
//        redisCacheConfiguration.usePrefix();
//
//        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory).build();
//    }






//    @Value("${cache.config.entryTtl:60}")
//    private int entryTtl;
//
//    @Value("${cache.config.jwtToken.entryTtl:30}")
//    private int jwtTokenEntryTtl; // jwtTokenEntryTtl
//
//    @Bean
//    public RedisCacheConfiguration cacheConfiguration() {
//        return RedisCacheConfiguration
//                .defaultCacheConfig()
//                .entryTtl(Duration.ofMinutes(entryTtl))
//                .disableCachingNullValues()
//                .serializeValuesWith( RedisSerializationContext.SerializationPair
//                        .fromSerializer(new GenericJackson2JsonRedisSerializer()));
//    }
//
//    @Bean
//    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
//        return builder -> {
//            var jwtCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
//                    .entryTtl(Duration.ofMinutes(jwtTokenEntryTtl));
//
//            builder.withCacheConfiguration(CacheConstant.JWT_CACHE_NAME, jwtCacheConfiguration);
//        };
//    }

}
