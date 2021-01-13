package com.github.fmjsjx.myboot.autoconfigure.redis;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Properties class for {@code REDIS/Lettuce} pool.
 */
@Getter
@Setter
@ToString(callSuper = true)
public class RedisPoolProperties extends RedisConnectionProperties {

    /**
     * Default is {@code sync}.
     */
    private RedisPoolMode mode = RedisPoolMode.SYNC;

    /**
     * The maximum connections of the pool.
     */
    private int maxTotal;
    /**
     * The maximum idle connections of the pool.
     */
    private int maxIdle;
    /**
     * The minimum idle connections of the pool.
     */
    private int minIdle;
    
    /**
     * If using wrapped connections or not.
     * <p>
     * The default is {@code true}
     */
    private boolean wrapConnections = true;

}
