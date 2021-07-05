package com.github.fmjsjx.myboot.autoconfigure.http.route;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.fmjsjx.libnetty.http.server.middleware.Router;
import com.github.fmjsjx.myboot.http.route.annotation.RouteController;

import lombok.extern.slf4j.Slf4j;

/**
 * Auto-Configuration class for {@code Router}.
 *
 * @since 1.1
 */
@Slf4j
@Configuration
@ConditionalOnClass(Router.class)
@EnableConfigurationProperties(RouterProperties.class)
public class RouterAutoConfiguration {

    @Bean(destroyMethod = "close")
    public Router router(RouterProperties properties, ListableBeanFactory beanFactory) {
        var router = new Router();
        var beans = beanFactory.getBeansWithAnnotation(RouteController.class);
        log.debug("Route controller beans: {}", beans);
        beans.values().forEach(router::register);
        if (properties.isAutoInit()) {
            log.debug("Call init() on {}", router);
            return router.init();
        }
        return router;
    }

}
