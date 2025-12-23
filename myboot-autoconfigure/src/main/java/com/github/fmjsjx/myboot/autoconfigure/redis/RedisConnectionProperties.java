package com.github.fmjsjx.myboot.autoconfigure.redis;

import java.net.URI;

import com.github.fmjsjx.libcommon.redis.core.RedisConnectionAdapter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jspecify.annotations.NonNull;

/**
 * Properties class for {@code REDIS/Lettuce} connection.
 */
@Getter
@Setter
@ToString
public class RedisConnectionProperties {

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

    /**
     * Weather fill {@link RedisConnectionAdapter} automatically or not.
     *
     * @since 3.6
     */
    private Boolean fillAdapter;

    /**
     * Constructs a new {@link RedisConnectionProperties} instance.
     */
    public RedisConnectionProperties() {
    }

}
