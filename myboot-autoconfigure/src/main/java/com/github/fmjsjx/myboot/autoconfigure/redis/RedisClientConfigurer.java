package com.github.fmjsjx.myboot.autoconfigure.redis;

/**
 * Defines callback methods to customize the Java-based configuration
 * for RedisClient/RedisClusterClient.
 *
 * @author MJ Fang
 * @since 3.7
 */
public interface RedisClientConfigurer {

    /**
     * Configure {@code ClientOptions}.
     *
     * @param configurer the {@link ClientOptionsConfigurer}
     */
    default void configureClientOptions(ClientOptionsConfigurer configurer) {
    }

    /**
     * Configure {@code ClusterClientOptions}.
     *
     * @param configurer the {@link ClusterClientOptionsConfigurer}
     */
    default void configureClusterClientOptions(ClusterClientOptionsConfigurer configurer) {
    }

}
