package com.github.fmjsjx.myboot.example.redis;

import com.github.fmjsjx.myboot.autoconfigure.redis.AsyncPoolPlus;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.support.AsyncPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


/**
 * Redis manager.
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
@Service
public class RedisManager implements InitializingBean {

    private final StatefulRedisConnection<String, String> testRedisConnection;
    private final StatefulRedisPubSubConnection<String, String> helloRedisConnection;
    private final GenericObjectPool<StatefulRedisConnection<String, String>> blockingRedisPool;
    private final AsyncPool<StatefulRedisConnection<String, String>> nonblockingRedisPool;
    private final AsyncPoolPlus<String, String, StatefulRedisConnection<String, String>> nonblockingPlusRedisPool;

    /**
     * Constructs a new {@link RedisManager} instance.
     *
     * @param testRedisConnection      the testRedisConnection
     * @param helloRedisConnection     the helloRedisConnection
     * @param blockingRedisPool        the blockingRedisPool
     * @param nonblockingRedisPool     the nonblockingRedisPool
     * @param nonblockingPlusRedisPool the nonblockingPlusRedisPool
     */
    public RedisManager(@Qualifier("testRedisConnection") StatefulRedisConnection<String, String> testRedisConnection,
                        @Qualifier("helloRedisConnection") StatefulRedisPubSubConnection<String, String> helloRedisConnection,
                        @Qualifier("blockingRedisPool") GenericObjectPool<StatefulRedisConnection<String, String>> blockingRedisPool,
                        @Qualifier("nonblockingRedisPool") AsyncPool<StatefulRedisConnection<String, String>> nonblockingRedisPool,
                        @Qualifier("nonblockingPlusRedisPool") AsyncPoolPlus<String, String, StatefulRedisConnection<String, String>> nonblockingPlusRedisPool) {
        this.testRedisConnection = testRedisConnection;
        this.helloRedisConnection = helloRedisConnection;
        this.blockingRedisPool = blockingRedisPool;
        this.nonblockingRedisPool = nonblockingRedisPool;
        this.nonblockingPlusRedisPool = nonblockingPlusRedisPool;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
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
        var nonblockingRedisPool = this.nonblockingRedisPool;
        var time = nonblockingRedisPool.acquire()
                .thenCompose(conn -> conn.async().time().whenComplete((nil, e) -> conn.close())).join();
        log.debug("TIME nonblocking pool: {}", time);
        var dbsize = nonblockingRedisPool.acquire()
                .thenCompose(conn -> conn.async().dbsize().whenComplete((nil, e) -> conn.close())).join();
        log.debug("DBSIZE nonblocking pool: {}", dbsize);
        var nonblockingPlusRedisPool = this.nonblockingPlusRedisPool;
        time = nonblockingPlusRedisPool.apply(conn -> conn.async().time()).join();
        log.debug("TIME nonblocking pool plus: {}", time);
        dbsize = nonblockingPlusRedisPool.apply(conn -> conn.async().dbsize()).join();
        log.debug("DBSIZE nonblocking pool plus: {}", dbsize);
    }

}
