package com.github.fmjsjx.myboot.autoconfigure.redis;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.RedisClient;

/**
 * Helps with configuring {@link ClientOptions} for {@link RedisClient}
 * creation processing.
 *
 * @author MJ Fang
 * @since 3.7
 */
public class ClientOptionsConfigurer {

    private final RedisClient client;

    /**
     * Package protected constructor,
     *
     * @param client the {@link RedisClient}
     */
    ClientOptionsConfigurer(RedisClient client) {
        this.client = client;
    }

    /**
     * Sets the {@link ClientOptions} for the {@link RedisClient}.
     *
     * @param options the {@link ClientOptions}
     */
    public void setOptions(ClientOptions options) {
        client.setOptions(options);
    }

}
