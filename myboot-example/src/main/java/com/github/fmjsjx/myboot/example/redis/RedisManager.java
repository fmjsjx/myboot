package com.github.fmjsjx.myboot.example.redis;

import javax.annotation.PostConstruct;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.github.fmjsjx.myboot.autoconfigure.redis.AsyncPoolPlus;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.support.AsyncPool;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RedisManager {

    @Autowired
    @Qualifier("testRedisConnection")
    private StatefulRedisConnection<String, String> testRedisConnection;

    @Autowired
    @Qualifier("helloRedisConnection")
    private StatefulRedisPubSubConnection<String, String> helloRedisConnection;

    @Autowired
    @Qualifier("blockingRedisPool")
    private GenericObjectPool<StatefulRedisConnection<String, String>> blockingRedisPool;

    @Autowired
    @Qualifier("noblockingRedisPool")
    private AsyncPool<StatefulRedisConnection<String, String>> noblockingRedisPool;

    @Autowired
    @Qualifier("noblockingPlusRedisPool")
    private AsyncPoolPlus<String, String, StatefulRedisConnection<String, String>> noblockingPlusRedisPool;

    @PostConstruct
    public void test() throws Exception {
        log.debug("test connection: {}", testRedisConnection);
        log.debug("hello connection: {}", helloRedisConnection);
        log.debug("GET test: {}", testRedisConnection.sync().get("test"));
        var blockingRedisPool = this.blockingRedisPool;
        try (var conn = blockingRedisPool.borrowObject()) {
            var sync = conn.sync();
            log.debug("TIME blocking pool: {}", sync.time());
            log.debug("DBSIZE blocking pool: {}", sync.dbsize());
            log.debug("BLPOP 1 nosucnkey blocking pool: {}", sync.blpop(1, "nosuchkey"));
        }
        var noblockingRedisPool = this.noblockingRedisPool;
        var time = noblockingRedisPool.acquire()
                .thenCompose(conn -> conn.async().time().whenComplete((nil, e) -> conn.close())).join();
        log.debug("TIME noblocking pool: {}", time);
        var dbsize = noblockingRedisPool.acquire()
                .thenCompose(conn -> conn.async().dbsize().whenComplete((nil, e) -> conn.close())).join();
        log.debug("DBSIZE noblocking pool: {}", dbsize);
        var noblockingPlusRedisPool = this.noblockingPlusRedisPool;
        time = noblockingPlusRedisPool.apply(conn -> conn.async().time()).join();
        log.debug("TIME noblocking pool plus: {}", time);
        dbsize = noblockingPlusRedisPool.apply(conn -> conn.async().dbsize()).join();
        log.debug("DBSIZE noblocking pool plus: {}", dbsize);
    }

}
