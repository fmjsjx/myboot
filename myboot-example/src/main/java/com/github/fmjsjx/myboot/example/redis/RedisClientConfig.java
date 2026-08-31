package com.github.fmjsjx.myboot.example.redis;

import java.time.Duration;

import com.github.fmjsjx.myboot.autoconfigure.redis.ClientOptionsConfigurer;
import com.github.fmjsjx.myboot.autoconfigure.redis.RedisClientConfigurer;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.TimeoutOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Configuration for redis client.
 *
 * @author MJ Fang
 * @since 3.7
 */
@Component
public class RedisClientConfig implements RedisClientConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(RedisClientConfig.class);

    /**
     * Constructs a new {@link RedisClientConfig} instance.
     */
    public RedisClientConfig() {
    }

    @Override
    public void configureClientOptions(ClientOptionsConfigurer configurer) {
        var options = ClientOptions.builder()
                .timeoutOptions(TimeoutOptions.builder().fixedTimeout(Duration.ofSeconds(30)).build())
                .build();
        logger.debug("redis configuration client options: {}", options);
        configurer.setOptions(options);
    }

}
