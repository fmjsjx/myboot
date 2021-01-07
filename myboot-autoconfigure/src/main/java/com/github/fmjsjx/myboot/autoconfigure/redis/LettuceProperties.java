package com.github.fmjsjx.myboot.autoconfigure.redis;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.NonNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Properties class for {@code REDIS/Lettuce}.
 */
@Getter
@Setter
@ToString
@ConfigurationProperties("myboot.redis.lettuce")
public class LettuceProperties {

    /**
     * The client.
     */
    private RedisClientProperties client;

    /**
     * The cluster clients.
     */
    private List<RedisClusterClientProperties> clusterClients = Collections.emptyList();

    /**
     * Properties class for {@code REDIS/Lettuce} client.
     */
    @Getter
    @Setter
    @ToString
    public static class RedisClientProperties {

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

    /**
     * Properties class for {@code REDIS/Lettuce} cluster client.
     */
    @Getter
    @Setter
    @ToString(callSuper = true)
    public static class RedisClusterClientProperties extends RedisClientProperties {

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

    /**
     * Properties class for {@code REDIS/Lettuce} connection.
     */
    @Getter
    @Setter
    @ToString
    public static class RedisConnectionProperties {

        /**
         * The name of the connection.
         */
        @NonNull
        private String name;

        /**
         * The default is <code>"${name}RedisConnection"</code>,
         * <p>
         * or <code>"${name}RedisPool"</code> for pool,
         * <p>
         * or <code>"${name}RedisClusterConnection"</code> for cluster connection.
         */
        private String beanName;
        /**
         * Weather this connection is primary or not.
         */
        private boolean primary;
        /**
         * The REDIS URI.
         */
        private URI uri;
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
         * The default is {@code 0}.
         * <p>
         * Can't be set with {@code uri}.
         */
        private int db;
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

        /**
         * Default is normal
         */
        private RedisConnectionType type = RedisConnectionType.NORMAL;
        /**
         * Default is utf8
         */
        private RedisConnectionCodec codec = RedisConnectionCodec.UTF8;

    }

    /**
     * Properties class for {@code REDIS/Lettuce} pool.
     */
    @Getter
    @Setter
    @ToString(callSuper = true)
    public static class RedisPoolProperties extends RedisConnectionProperties {

        /**
         * Default is {@code blocking}.
         */
        private RedisPoolMode mode = RedisPoolMode.BLOCKING;

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

    }

    /**
     * Enumeration for {@code REDIS/Lettuce} connection types.
     */
    public enum RedisConnectionType {

        /**
         * Normal connection.
         */
        NORMAL,
        /**
         * PUBSUB connection.
         */
        PUBSUB,
        /**
         * SENTINEL connection.
         */
        SENTINEL

    }

    /**
     * Enumeration for {@code REDIS/Lettuce} connection encode/decode modes.
     */
    public enum RedisConnectionCodec {

        /**
         * String on {@code UTF-8}.
         */
        UTF8,
        /**
         * String on {@code US-ASCII}.
         */
        ASCII,
        /**
         * Byte array.
         */
        BYTE_ARRAY

    }

    /**
     * Enumeration for {@code REDIS/Lettuce} pool modes.
     */
    public enum RedisPoolMode {

        /**
         * Blocking mode.
         */
        BLOCKING,
        /**
         * No blocking mode.
         */
        NO_BLOCKING

    }

}
