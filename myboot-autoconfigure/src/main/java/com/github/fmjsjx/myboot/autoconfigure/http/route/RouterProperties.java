package com.github.fmjsjx.myboot.autoconfigure.http.route;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.github.fmjsjx.libnetty.http.server.middleware.Router;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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

}
