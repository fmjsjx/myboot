package com.github.fmjsjx.myboot.autoconfigure.redis;

import java.net.URI;
import java.time.Duration;
import java.util.List;

import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

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
    private String name;
    /**
     * The REDIS URI.
     */
    private URI uri;
    /**
     * The REDIS URI list.
     * <p>
     * If exists, the {@code uri} parameter will be ignored.
     *
     * @since 3.5
     */
    private List<URI> uris;
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

    /**
     * The topology refresh options.
     *
     * @since 3.5
     */
    @NestedConfigurationProperty
    private TopologyRefreshProperties topologyRefresh;

    /**
     * Properties class for topology refresh options of the redis cluster client.
     *
     * @author MJ Fang
     * @since 3.5
     */
    @Getter
    @Setter
    @ToString
    public static class TopologyRefreshProperties {

        /**
         * Whether regular cluster topology updates are updated.
         * <p>
         * The default is {@code false}.
         */
        private boolean periodicRefreshEnabled;
        /**
         * Period between the regular cluster topology updates.
         * <p>
         * The default is {@code 60s}.
         */
        private Duration refreshPeriod;
        /**
         * The array of the enabled adaptive topology refresh triggers.
         *
         * @deprecated Starting from 3.8, this configuration has no effect
         * as all adaptive triggers are enabled by default
         */
        @Deprecated
        private ClusterTopologyRefreshOptions.RefreshTrigger[] adaptiveRefreshTriggers;
        /**
         * Weather to enable all adaptive topology refresh triggers or not.
         * <p>
         * The default is {@code false}
         *
         * @deprecated Starting from 3.8, this configuration has no effect
         * as all adaptive triggers are enabled by default
         */
        @Deprecated
        private boolean enableAllAdaptiveRefreshTriggers;
        /**
         * The list of the disabled adaptive topology refresh triggers.
         */
        private List<ClusterTopologyRefreshOptions.RefreshTrigger> disabledAdaptiveRefreshTriggers;
        /**
         * Weather to disable all adaptive topology refresh triggers or
         * not.
         * <p>
         * The default is {@code false}
         */
        private boolean disableAllAdaptiveRefreshTriggers;
        /**
         * Set the timeout for adaptive topology updates.
         */
        private Duration adaptiveRefreshTriggersTimeout;

    }

}
