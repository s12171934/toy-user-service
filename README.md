### application.yml
```
server:
  port: [PORT]
  servlet:
    session:
      timeout: 90m

spring:
  application:
    name: user-service

  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: [DB_SERVER_URL]
    username: [DB_USER]
    password: [DB_PASSWORD]

  jpa:
    hibernate:
      ddl-auto: update
      naming:
        physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl

eureka:
  client:
    register-with-eureka: true
    fetch-registry: true
    service-url:
      defaultZone: [EUREKA_SERVER_URL]
```
