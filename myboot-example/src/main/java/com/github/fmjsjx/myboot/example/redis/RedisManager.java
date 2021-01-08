package com.github.fmjsjx.myboot.example.redis;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RedisManager {

    @Autowired
    @Qualifier("testRedisConnection")
    private StatefulRedisConnection<String, String> testConnection;

    @Autowired
    @Qualifier("helloRedisConnection")
    private StatefulRedisPubSubConnection<String, String> helloConnection;

    @PostConstruct
    public void test() {
        log.debug("test connection: {}", testConnection);
        log.debug("hello connection: {}", helloConnection);
        log.debug("GET test: {}", testConnection.sync().get("test"));
    }

}
