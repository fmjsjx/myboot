package com.github.fmjsjx.myboot.autoconfigure.redis;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.support.AsyncConnectionPoolSupport;
import io.lettuce.core.support.AsyncPool;
import io.lettuce.core.support.BoundedPoolConfig;

class AsyncPoolRegistry {

    static void registerAsyncPoolBean(BeanDefinitionRegistry registry, String clientBeanName, RedisClient client,
            RedisPoolProperties properties, String beanName, RedisURI uri, RedisCodec<?, ?> codec) {
        var config = buildBoundedPoolConfig(properties);
        BeanDefinitionBuilder beanDefinitionBuilder;
        if (properties.getMode() == RedisPoolMode.ASYNC_PLUS) {
            beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(AsyncPoolPlus.class,
                    () -> new AsyncPoolPlusImpl<>(AsyncConnectionPoolSupport.createBoundedObjectPool(
                            () -> client.connectAsync(codec, uri), config, properties.isWrapConnections())));
        } else {
            beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(AsyncPool.class,
                    () -> AsyncConnectionPoolSupport.createBoundedObjectPool(() -> client.connectAsync(codec, uri),
                            config, properties.isWrapConnections()));
        }
        var beanDefinition = beanDefinitionBuilder.addDependsOn(clientBeanName).setPrimary(properties.isPrimary())
                .getBeanDefinition();
        registry.registerBeanDefinition(beanName, beanDefinition);
    }

    static final BoundedPoolConfig buildBoundedPoolConfig(RedisPoolProperties properties) {
        var builder = BoundedPoolConfig.builder();
        if (properties.getMaxTotal() > 0) {
            builder.maxTotal(properties.getMaxTotal());
        }
        if (properties.getMaxIdle() > 0) {
            builder.maxIdle(properties.getMaxIdle());
        }
        if (properties.getMinIdle() > 0) {
            builder.minIdle(properties.getMinIdle());
        }
        var config = builder.build();
        return config;
    }

}
