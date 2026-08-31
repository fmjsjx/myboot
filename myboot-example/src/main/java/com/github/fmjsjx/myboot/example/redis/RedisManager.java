package com.github.fmjsjx.myboot.example.redis;

import com.github.fmjsjx.libcommon.redis.core.RedisConnectionAdapter;
import com.github.fmjsjx.libcommon.redis.core.RedisPubSubConnectionAdapter;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import lombok.extern.slf4j.Slf4j;
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
    private final StatefulRedisPubSubConnection<String, String> helloRedisPubSubConnection;
    private final RedisConnectionAdapter<String, String> testRedisConnectionAdapter;
    private final RedisPubSubConnectionAdapter<String, String> helloRedisPubSubConnectionAdapter;

    /**
     * Constructs a new {@link RedisManager} instance.
     *
     * @param testRedisConnection        the testRedisConnection
     * @param helloRedisPubSubConnection the helloRedisPubSubConnection
     * @param testRedisConnectionAdapter        the testRedisConnectionAdapter
     * @param helloRedisPubSubConnectionAdapter the helloRedisPubSubConnectionAdapter
     */
    public RedisManager(@Qualifier("testRedisConnection") StatefulRedisConnection<String, String> testRedisConnection,
                        @Qualifier("helloRedisPubSubConnection") StatefulRedisPubSubConnection<String, String> helloRedisPubSubConnection,
                        @Qualifier("testRedisConnectionAdapter") RedisConnectionAdapter<String, String> testRedisConnectionAdapter,
                        @Qualifier("helloRedisPubSubConnectionAdapter") RedisPubSubConnectionAdapter<String, String> helloRedisPubSubConnectionAdapter) {
        this.testRedisConnection = testRedisConnection;
        this.helloRedisPubSubConnection = helloRedisPubSubConnection;
        this.testRedisConnectionAdapter = testRedisConnectionAdapter;
        this.helloRedisPubSubConnectionAdapter = helloRedisPubSubConnectionAdapter;
    }

    @Override
    public void afterPropertiesSet() {
        log.debug("test connection: {}", testRedisConnection);
        log.debug("hello connection: {}", helloRedisPubSubConnection);
        log.debug("test connection adapter: {}", testRedisConnectionAdapter);
        log.debug("hello connection adapter: {}", helloRedisPubSubConnectionAdapter);
        log.debug("GET test: {}", testRedisConnection.sync().get("test"));

        helloRedisPubSubConnectionAdapter.reactive().observeChannels().subscribe(message ->
                log.debug("Received from hello pubsub connection: {} <<< {}", message.getChannel(), message.getMessage()));
        helloRedisPubSubConnectionAdapter.async().subscribe("test").whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Error subscribing to channel 'test'", ex);
            } else {
                log.debug("Subscribed to channel 'test' with result: {}", result);
            }
        }).toCompletableFuture().join();

    }

}
