package com.github.fmjsjx.myboot.autoconfigure.http.route;

import com.github.fmjsjx.libnetty.http.server.middleware.Router;
import com.github.fmjsjx.myboot.http.route.annotation.RouteController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Auto-Configuration class for {@code Router}.
 *
 * @since 1.1
 */
@Slf4j
@AutoConfiguration
@ConditionalOnClass(Router.class)
@EnableConfigurationProperties(RouterProperties.class)
public class RouterAutoConfiguration {

    /**
     * The {@code @Bean} method for {@link Router}.
     *
     * @param properties  the router properties
     * @param beanFactory the bean factory
     * @return a {@code Router}
     */
    @Bean(destroyMethod = "close")
    @ConditionalOnMissingBean
    public Router router(RouterProperties properties, ListableBeanFactory beanFactory) {
        var router = new Router();
        var policy = properties.getRoutingPolicy();
        if (policy == null) {
            policy = RouterProperties.RoutingPolicy.AUTO;
        }
        switch (policy) {
            case AUTO -> router.routingPolicy(null);
            case SIMPLE -> router.routingPolicy(Router.RoutingPolicy.SIMPLE);
            case TREE_MAP -> router.routingPolicy(Router.RoutingPolicy.TREE_MAP);
        }
        var beans = beanFactory.getBeansWithAnnotation(RouteController.class);
        log.debug("Route controller beans: {}", beans);
        beans.values().forEach(router::register);
        if (properties.isAutoInit()) {
            log.debug("Call init() on {}", router);
            return router.init();
        }
        return router;
    }

    /**
     * Constructs a new {@link RouterAutoConfiguration} instance.
     */
    public RouterAutoConfiguration() {
    }

}
