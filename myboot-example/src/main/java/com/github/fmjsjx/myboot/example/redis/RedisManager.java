package com.github.fmjsjx.myboot.example.redis;

import javax.annotation.PostConstruct;

import org.apache.commons.pool2.impl.GenericObjectPool;
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

    @Autowired
    @Qualifier("blockingRedisPool")
    private GenericObjectPool<StatefulRedisConnection<String, String>> blockingRedisPool;

    @PostConstruct
    public void test() throws Exception {
        log.debug("test connection: {}", testConnection);
        log.debug("hello connection: {}", helloConnection);
        log.debug("GET test: {}", testConnection.sync().get("test"));
        var blockingRedisPool = this.blockingRedisPool;
        try (var conn = blockingRedisPool.borrowObject()) {
            var sync = conn.sync();
            log.debug("TIME blocking pool: {}", sync.time());
            log.debug("DBSIZE blocking pool: {}", sync.dbsize());
            log.debug("BLPOP 1 nosucnkey blocking pool: {}", sync.blpop(1, "nosuchkey"));
        }
    }

}
