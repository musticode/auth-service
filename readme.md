# Read Me First
The following was discovered as part of building this project:

* The original package name 'com.example.auth-service' is invalid and this project uses 'com.example.authservice' instead.

# Getting Started

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