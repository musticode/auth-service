# Spring Security Architecture

![img.png](img.png)

- When the request from the user accesses our application, AuthenticationFilter takes the username and password from the request and an object is created.
- Using the created Authentication Object comes to Authentication Manager after the filter. The created object contains Granted Authority, Roles and Principal information. Authentication Manager is an interface and the authentication method is run. Authentication Manager is an interface and sends data to the Authentication Provider.
- It informs the Authentication Provider which type of verification will be performed in authentication transactions.
- Authentication Provider has different providers such as LdapAuthenticationProvider, CasAuthenticationProvider, DaoAuthenticationProvider. Authentication Provider selects the most suitable provider.
- The Authentication Provider calls the User Details Service and retrieves the user information for the corresponding user. Using the service, it retrieves the User Object corresponding to the username.
- In the User Details Service, it accesses the corresponding in-memory or database or from whatever sources it needs to access by looking at information such as the loadUserByUsername method (whether the account is locked or active, whether the credentials have expired) and brings the incoming user information it finds and if the correct user is used. If found and returns the object of the found user. This service includes Password Encoder, which verifies the user password. Password Encoder is the interface that tells the user password to be encoded and decrypted.
- Security Context contains information about the authenticated user. We can use this user information throughout our application thanks to SecurityContextHolder.
- When an unauthenticated user is encountered, an Authentication Exception is thrown.
- The authenticated user is kept in the Security Context within our application. When the user who has logged in later requests access again, Security Context decides whether to log in or not. If the user information is available in the Security Context, there will be no need to log in again.

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.2.5/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.2.5/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.2.5/reference/htmlsingle/index.html#web)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/3.2.5/reference/htmlsingle/index.html#using.devtools)
* [Docker Compose Support](https://docs.spring.io/spring-boot/docs/3.2.5/reference/htmlsingle/index.html#features.docker-compose)
* [Spring Security](https://docs.spring.io/spring-boot/docs/3.2.5/reference/htmlsingle/index.html#web.security)
* [OAuth2 Client](https://docs.spring.io/spring-boot/docs/3.2.5/reference/htmlsingle/index.html#web.security.oauth2.client)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.2.5/reference/htmlsingle/index.html#data.sql.jpa-and-spring-data)
* [Spring Data MongoDB](https://docs.spring.io/spring-boot/docs/3.2.5/reference/htmlsingle/index.html#data.nosql.mongodb)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
* [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Accessing Data with MongoDB](https://spring.io/guides/gs/accessing-data-mongodb/)

### Docker Compose support
This project contains a Docker Compose file named `compose.yaml`.
In this file, the following services have been defined:

* mongodb: [`mongo:latest`](https://hub.docker.com/_/mongo)
* postgres: [`postgres:latest`](https://hub.docker.com/_/postgres)

Please review the tags of the used images and set them to the same as you're running in production.



# Documents

### Data JPA 

Things to check:

- You have the correct dependencies in the POM
- You have configured the URL correctly in application properties
- You have the rights to create tables in the database
- You have Hibernate setup correctly in application properties
- The classes are POJOs and have the '@entity
- If all else fails, turn on JPA show SQl: spring.jpa.show-sql=true
- And turn on debug level logging - yes, it is long, but it will show you what JPA and Hibernate are trying to do.



## Requests

**register**

request:
```
curl --location 'http://localhost:8080/api/auth/register' \
--header 'Content-Type: application/json' \
--data '{
    "firstName" : "firstName1",
    "lastName" : "last__",
    "username" : "username",
    "email" : "password",
    "password" : "1234password"
}'
```

response: 
```
{
    "token": "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ1c2VybmFtZSIsImlhdCI6MTcxNDU4NjEzNiwiZXhwIjoxNzE0NjcyNTM2fQ.2AynhVOzdDQLzRPNCY3923jknlXM3GOj_N6PaHPdqSQWp0t_ic2qhdMM-MrDXrbU",
    "message": "User is created"
}
```



**login**
```
```

**demo**

request:
- Note that Bearer token should be taken from register request's response
```
curl --location 'http://localhost:8080/api/test/demo' \
--header 'Authorization: Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ1c2VybmFtZSIsImlhdCI6MTcxNDU4NjEzNiwiZXhwIjoxNzE0NjcyNTM2fQ.2AynhVOzdDQLzRPNCY3923jknlXM3GOj_N6PaHPdqSQWp0t_ic2qhdMM-MrDXrbU'
```

response:
```
Hello from secured url
```


# Redis Configuration

compose yml: 
```
  redis:
    image: redis:latest
    restart: always
    ports:
      - "6379:6379"
#    volumes:
#      - /path/to/local/dаta:/root/redis
#      - /path/to/local/redis.conf:/usr/local/etc/redis/redis.conf
    environment:
      - REDIS_PASSWORD=my-password
      - REDIS_PORT=6379
      - REDIS_DATABASES=16
```
- [Link](https://zomro.com/blog/faq/301-kak-ustanovit-redis-v-docker)

### Redis Config

```
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
}
```

### Redis Template in service

```

@Service
@Slf4j
public class CacheTokenService {

    public static final Duration CACHE_DURATION = Duration.ofSeconds(30);

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
            stringRedisTemplate.opsForValue().set("token:" + username, token, CACHE_DURATION);
            log.info("Username : {} , token : {}", username, token);
        }
        log.info("Username empty or null");
    }

    public String getCachedToken(String username) {
        // Retrieve the token from Redis cache
        return stringRedisTemplate.opsForValue().get("token:" + username);
    }

}
```

#### Usage:

```
cacheTokenService.cacheToken(request.getUsername(), jwt);
cacheTokenService.getCachedToken(request.getUsername());
```

#### Redis Template 

```
    final Logger logger = LoggerFactory.getLogger(UserRepository.class);
    private HashOperations hashOperations;
    
    public UserRepository(RedisTemplate redisTemplate) {
        this.hashOperations = redisTemplate.opsForHash();
    }
    
    public void create(User user) {
        hashOperations.put("USER", user.getUserId(), user);
        logger.info(String.format("User with ID %s saved", user.getUserId()));
    }
    
    public User get(String userId) {
        return (User) hashOperations.get("USER", userId);
    }

    public Map<String, User> getAll(){
        return hashOperations.entries("USER");
    }
    
    public void update(User user) {
        hashOperations.put("USER", user.getUserId(), user);
        logger.info(String.format("User with ID %s updated", user.getUserId()));
    }
    
    public void delete(String userId) {
        hashOperations.delete("USER", userId);
        logger.info(String.format("User with ID %s deleted", userId));
    }
```

# Cache Yazı
- https://musticode.gitbook.io/springboot-configurations



### REF
- [Multiple Authentication - Spring Security](https://medium.com/@fatih.yurdagul/spring-security-multiple-authentication-81e2c8b8ba13)
- [Spring Security - Temel Kavramlar](https://medium.com/@fatih.yurdagul/spring-security-authentication-ve-temel-kavramlar-350afbe45ef7)
- [Spring Security - Mimari](https://blog.burakkutbay.com/spring-security-mimarisi-akisi.html/)
- [Spring Security - Auth](https://erayerdem.medium.com/spring-security-i%CC%87le-authentication-ve-authorization-1-5d88ef208ebc)

## Redis - Redis Insight docker compose

```
version: '3.8'
services:

  redis:
    container_name: redis
    image: redis
    command: redis-server --appendonly yes
    ports:
      - "6379:6379"

  redis-insight:
    image: redislabs/redisinsight
    container_name: redis-insight
    ports:
      - 8001:8001
```

- [Reacive Redis](https://blog.devgenius.io/spring-reactive-redis-messaging-0cff238e159c)

## PUB/SUB Messaging

- https://www.baeldung.com/spring-data-redis-pub-sub
- https://www.youtube.com/watch?v=hr8IaQphWFA&ab_channel=Jskool
- https://medium.com/@bhanuchaddha/using-redis-pub-sub-with-spring-boot-ea0d7a8c27af
- 