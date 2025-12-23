package com.github.fmjsjx.myboot.autoconfigure.http.route;

import com.github.fmjsjx.libnetty.http.server.middleware.Router;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties class for Router.
 */
@Getter
@Setter
@ToString
@ConfigurationProperties(RouterProperties.CONFIG_PREFIX)
public class RouterProperties {

    static final String CONFIG_PREFIX = "myboot.http.router";

    /**
     * Call {@link Router#init()} automatically when this value is {@code true}.
     * 
     * <p>
     * The default value is {@code false}.
     */
    private boolean autoInit;

    /**
     * Call {@link Router#routingPolicy(Router.RoutingPolicy) to set the routing policy}.
     *
     * <p>
     * The default value is {@code simple}.
     *
     * @since 3.1
     */
    private RoutingPolicy routingPolicy;

    /**
     * Constructs a new {@link RouterProperties} instance.
     */
    public RouterProperties() {
    }

    /**
     * Enumeration of routing policies.
     *
     * @since 3.1
     */
    public enum RoutingPolicy {

        /**
         * {@code auto}
         */
        AUTO,
        /**
         * {@code simple}
         */
        SIMPLE,
        /**
         * {@code tree-map}
         */
        TREE_MAP

    }

}
