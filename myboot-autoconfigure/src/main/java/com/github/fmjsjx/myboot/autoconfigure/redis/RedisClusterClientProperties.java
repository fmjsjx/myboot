package com.github.fmjsjx.myboot.autoconfigure.redis;

import java.net.URI;

import org.springframework.lang.NonNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Properties class for {@code REDIS/Lettuce} cluster client.
 */
@Getter
@Setter
@ToString(callSuper = true)
public class RedisClusterClientProperties extends RedisClientProperties {

    /**
     * The name of the {@code REDIS/Lettuce} cluster client
     */
    @NonNull
    private String name;
    /**
     * The REDIS URI.
     */
    private URI uri;
    /**
     * Weather this cluster client is primary or not.
     */
    private boolean primary;
    /**
     * The host.
     * <p>
     * Can't be set with {@code uri}.
     */
    private String host;
    /**
     * The default is 6379
     * <p>
     * Can't be set with {@code uri}.
     */
    private int port = 6379;
    /**
     * The user name to AUTH.
     * <p>
     * Can't be set with {@code uri}.
     * <p>
     * Will be ignored when {@code password} no be set.
     */
    private String username;
    /**
     * The password to AUTH.
     * <p>
     * Can't be set with {@code uri}.
     */
    private String password;

}
