package com.github.fmjsjx.myboot.example.redis;

import com.github.fmjsjx.myboot.autoconfigure.redis.ClientOptionsConfigurer;
import com.github.fmjsjx.myboot.autoconfigure.redis.RedisClientConfigurer;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.TimeoutOptions;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Configuration for redis client.
 *
 * @author MJ Fang
 * @since 3.7
 */
@Component
public class RedisClientConfig implements RedisClientConfigurer {

    @Override
    public void configureClientOptions(ClientOptionsConfigurer configurer) {
        var options = ClientOptions.builder()
                .timeoutOptions(TimeoutOptions.builder().fixedTimeout(Duration.ofSeconds(30)).build())
                .build();
        System.err.println("redis configuration client options: " + options);
        configurer.setOptions(options);
    }

}
