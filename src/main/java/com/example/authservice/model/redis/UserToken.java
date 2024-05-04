package com.example.authservice.model.redis;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "usertokens")
public class UserToken {
    @Id
    @Indexed
    private int id; // "indexed" for faster retrieval,
    // @Id for marking this field as the key
    private String username;
    private String token;

}
