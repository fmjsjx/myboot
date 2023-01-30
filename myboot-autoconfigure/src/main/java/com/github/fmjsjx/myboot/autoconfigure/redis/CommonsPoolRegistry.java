package com.github.fmjsjx.myboot.autoconfigure.redis;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.commons.pool2.impl.SoftReferenceObjectPool;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.support.ConnectionPoolSupport;

class CommonsPoolRegistry {

    static void registerCommonsPoolBean(BeanDefinitionRegistry registry, String clientBeanName, RedisClient client,
            RedisPoolProperties properties, String beanName, RedisURI uri, RedisCodec<?, ?> codec) {
        switch (properties.getMode()) {
            case SYNC ->
                    registerGenericObjectPoolBean(registry, clientBeanName, client, properties, beanName, uri, codec);
            case ASYNC ->
                    registerSoftReferencePoolBean(registry, clientBeanName, client, properties, beanName, uri, codec);
            default -> throw new BeanCreationException(beanName, "Unsupported mode `" + properties.getMode() + "`");
        }
    }

    static final void registerGenericObjectPoolBean(BeanDefinitionRegistry registry, String clientBeanName,
            RedisClient client, RedisPoolProperties properties, String beanName, RedisURI uri, RedisCodec<?, ?> codec) {
        var config = CommonsPoolRegistry.buildGenericPoolConfig(properties);
        var beanDefinition = BeanDefinitionBuilder
                .genericBeanDefinition(GenericObjectPool.class,
                        () -> ConnectionPoolSupport.createGenericObjectPool(() -> client.connect(codec, uri), config,
                                properties.isWrapConnections()))
                .setPrimary(properties.isPrimary()).addDependsOn(clientBeanName).getBeanDefinition();
        registry.registerBeanDefinition(beanName, beanDefinition);
    }

    static final void registerSoftReferencePoolBean(BeanDefinitionRegistry registry, String clientBeanName,
            RedisClient client, RedisPoolProperties properties, String beanName, RedisURI uri, RedisCodec<?, ?> codec) {
        var beanDefinition = BeanDefinitionBuilder
                .genericBeanDefinition(SoftReferenceObjectPool.class,
                        () -> ConnectionPoolSupport.createSoftReferenceObjectPool(() -> client.connect(codec, uri),
                                properties.isWrapConnections()))
                .setPrimary(properties.isPrimary()).addDependsOn(clientBeanName).getBeanDefinition();
        registry.registerBeanDefinition(beanName, beanDefinition);
    }

    static final GenericObjectPoolConfig<StatefulRedisConnection<?, ?>> buildGenericPoolConfig(
            RedisPoolProperties properties) {
        GenericObjectPoolConfig<StatefulRedisConnection<?, ?>> config = new GenericObjectPoolConfig<>();
        if (properties.getMaxTotal() > 0) {
            config.setMaxTotal(properties.getMaxTotal());
        }
        if (properties.getMaxIdle() > 0) {
            config.setMaxIdle(properties.getMaxIdle());
        }
        if (properties.getMinIdle() > 0) {
            config.setMinIdle(properties.getMinIdle());
        }
        config.setJmxEnabled(false);
        return config;
    }

}
