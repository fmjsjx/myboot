package com.github.fmjsjx.myboot.autoconfigure.redis;

import java.util.Collections;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Properties class for {@code REDIS/Lettuce}.
 */
@Getter
@Setter
@ToString
@ConfigurationProperties(LettuceProperties.CONFIG_PREFIX)
public class LettuceProperties {

    static final String CONFIG_PREFIX = "myboot.redis.lettuce";

    /**
     * The client.
     */
    private RedisClientProperties client;

    /**
     * The cluster clients.
     */
    private List<RedisClusterClientProperties> clusterClients = Collections.emptyList();

}
