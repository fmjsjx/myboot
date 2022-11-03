package com.github.fmjsjx.myboot.autoconfigure.redis;

import java.util.Collections;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Properties class for {@code REDIS/Lettuce} client.
 */
@Getter
@Setter
@ToString
public class RedisClientProperties {

    /**
     * The default is {@code "redisClient"},
     * <p>
     * or <code>"${name}RedisClusterClient"</code> for cluster client.
     */
    private String beanName = "redisClient";

    /**
     * The number of IO threads.
     */
    private int ioThreads;

    /**
     * The number of computation threads.
     */
    private int computationThreads;

    /**
     * The connections.
     */
    private List<RedisConnectionProperties> connections = Collections.emptyList();

    /**
     * The pools.
     */
    private List<RedisPoolProperties> pools = Collections.emptyList();

}
