package com.github.fmjsjx.myboot.autoconfigure.redis;

import java.util.List;

import com.github.fmjsjx.libcommon.redis.core.RedisConnectionAdapter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

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
    @NestedConfigurationProperty
    private RedisClientProperties client;

    /**
     * The cluster clients.
     */
    private List<RedisClusterClientProperties> clusterClients = List.of();

    /**
     * Weather fill {@link RedisConnectionAdapter}s automatically or not.
     * <p>
     * The default is {@code true}.
     *
     * @since 3.6
     */
    private boolean autoFillConnectionAdapters = true;

}
