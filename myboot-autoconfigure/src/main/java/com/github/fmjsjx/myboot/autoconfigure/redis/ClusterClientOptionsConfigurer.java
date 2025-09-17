package com.github.fmjsjx.myboot.autoconfigure.redis;

import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.RedisClusterClient;

/**
 * Helps with configuring {@link ClusterClientOptions} for
 * {@link RedisClusterClient} creation processing.
 *
 * @author MJ Fang
 * @since 3.7
 */
public class ClusterClientOptionsConfigurer {

    private final RedisClusterClient client;

    /**
     * Package protected constructor,
     *
     * @param client the {@link RedisClusterClient}
     */
    ClusterClientOptionsConfigurer(RedisClusterClient client) {
        this.client = client;
    }

    /**
     * Sets the {@link ClusterClientOptions} for the
     * {@link RedisClusterClient}.
     *
     * @param options the {@link ClusterClientOptions}
     */
    public void setOptions(ClusterClientOptions options) {
        client.setOptions(options);
    }

}
