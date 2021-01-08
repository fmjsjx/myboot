package com.github.fmjsjx.myboot.autoconfigure.redis;

import java.util.Optional;

import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.github.fmjsjx.myboot.autoconfigure.redis.LettuceProperties.RedisClientProperties;
import com.github.fmjsjx.myboot.autoconfigure.redis.LettuceProperties.RedisClusterClientProperties;
import com.github.fmjsjx.myboot.autoconfigure.redis.LettuceProperties.RedisConnectionCodec;
import com.github.fmjsjx.myboot.autoconfigure.redis.LettuceProperties.RedisConnectionProperties;
import com.github.fmjsjx.myboot.autoconfigure.redis.LettuceProperties.RedisConnectionType;
import com.github.fmjsjx.myboot.autoconfigure.redis.LettuceProperties.RedisPoolMode;
import com.github.fmjsjx.myboot.autoconfigure.redis.LettuceProperties.RedisPoolProperties;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.pubsub.StatefulRedisClusterPubSubConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.sentinel.api.StatefulRedisSentinelConnection;
import io.lettuce.core.support.AsyncConnectionPoolSupport;
import io.lettuce.core.support.AsyncPool;
import io.lettuce.core.support.BoundedPoolConfig;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Auto configuration class for {@code REDIS/Lettuce}.
 */
@Configuration
@ConditionalOnClass(RedisClient.class)
@ConditionalOnMissingBean(RedisClient.class)
@EnableConfigurationProperties(LettuceProperties.class)
public class LettuceAutoConfiguration {

    /**
     * Returns a new {@link LettuceRegistryProcessor} instance.
     * 
     * @return a new {@code LettuceRegisteryProcessor} instance
     */
    @Bean
    public static final LettuceRegistryProcessor lettuceRegisteryProcessor() {
        return new LettuceRegistryProcessor();
    }

    /**
     * Registry processor for {@code REDIS/Lettuce}.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class LettuceRegistryProcessor
            implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {

        private Environment environment;
        private BeanDefinitionRegistry registry;

        @Override
        public void setEnvironment(Environment environment) {
            this.environment = environment;
        }

        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
            // ignore
        }

        @Override
        public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
            this.registry = registry;
            var bindResult = Binder.get(environment).bind(LettuceProperties.CONFIG_PREFIX, LettuceProperties.class);
            bindResult.ifBound(props -> {
                if (props.getClient() != null) {
                    registerBeans(props.getClient());
                }
                props.getClusterClients().forEach(this::registerBeans);
            });
        }

        private void registerBeans(RedisClientProperties properties) throws BeansException {
            if (properties instanceof RedisClusterClientProperties) {
                registerBeans((RedisClusterClientProperties) properties);
            } else {
                var client = registerClientBean(properties);
                if (properties.getConnections().size() == 1) {
                    properties.getConnections().get(0).setPrimary(true);
                }
                for (var connectionProperties : properties.getConnections()) {
                    registerConnectionBean(client, connectionProperties);
                }
                if (properties.getPools().size() == 1) {
                    properties.getPools().get(0).setPrimary(true);
                }
                for (var poolProperties : properties.getPools()) {
                    registerPoolBean(client, poolProperties);
                }
            }
        }

        private String clientBeanName;

        private RedisClient registerClientBean(RedisClientProperties properties) {
            var builder = ClientResources.builder();
            if (properties.getIoThreads() > 0) {
                builder.ioThreadPoolSize(properties.getIoThreads());
            }
            if (properties.getComputationThreads() > 0) {
                builder.computationThreadPoolSize(properties.getComputationThreads());
            }
            var client = RedisClient.create(builder.build());
            clientBeanName = properties.getBeanName();
            registry.registerBeanDefinition(clientBeanName,
                    BeanDefinitionBuilder.genericBeanDefinition(RedisClient.class, () -> client).setPrimary(true)
                            .setDestroyMethodName("shutdown").getBeanDefinition());
            return client;
        }

        private void registerConnectionBean(RedisClient client, RedisConnectionProperties properties)
                throws BeansException {
            var beanName = Optional.ofNullable(properties.getBeanName())
                    .orElseGet(() -> properties.getName() + "RedisConnection");
            var uri = createUri(properties);
            var codec = getRedisCodec(properties.getCodec());
            if (properties.getType() == RedisConnectionType.NORMAL) {
                var beanDefinition = BeanDefinitionBuilder
                        .genericBeanDefinition(StatefulRedisConnection.class, () -> client.connect(codec, uri))
                        .addDependsOn(clientBeanName).setPrimary(properties.isPrimary()).getBeanDefinition();
                registry.registerBeanDefinition(beanName, beanDefinition);
            } else if (properties.getType() == RedisConnectionType.PUBSUB) {
                var beanDefinition = BeanDefinitionBuilder
                        .genericBeanDefinition(StatefulRedisPubSubConnection.class,
                                () -> client.connectPubSub(codec, uri))
                        .addDependsOn(clientBeanName).setPrimary(properties.isPrimary()).getBeanDefinition();
                registry.registerBeanDefinition(beanName, beanDefinition);
            } else if (properties.getType() == RedisConnectionType.SENTINEL) {
                var beanDefinition = BeanDefinitionBuilder
                        .genericBeanDefinition(StatefulRedisSentinelConnection.class,
                                () -> client.connectSentinel(codec, uri))
                        .addDependsOn(clientBeanName).setPrimary(properties.isPrimary()).getBeanDefinition();
                registry.registerBeanDefinition(beanName, beanDefinition);
            }
        }

        private static final RedisURI createUri(RedisConnectionProperties properties) {
            var uri = properties.getUri();
            if (uri != null) {
                return RedisURI.create(uri);
            }
            var redisUri = RedisURI.create(properties.getHost(), properties.getPort());
            redisUri.setDatabase(properties.getDb());
            var password = properties.getPassword();
            if (password != null && !password.isBlank()) {
                redisUri.setPassword((CharSequence) password.trim());
                var username = properties.getUsername();
                if (username != null && !username.isBlank()) {
                    redisUri.setUsername(username);
                }
            }
            return redisUri;
        }

        private static final RedisCodec<?, ?> getRedisCodec(RedisConnectionCodec codec) {
            switch (codec) {
            case ASCII:
                return StringCodec.ASCII;
            case BYTE_ARRAY:
                return ByteArrayCodec.INSTANCE;
            default:
            case UTF8:
                return StringCodec.UTF8;
            }
        }

        private void registerPoolBean(RedisClient client, RedisPoolProperties properties) throws BeansException {
            var beanName = Optional.ofNullable(properties.getBeanName())
                    .orElseGet(() -> properties.getName() + "RedisPool");
            if (properties.getType() != RedisConnectionType.NORMAL) {
                throw new FatalBeanException("Redis connection type must be normal for pools");
            }
            var uri = createUri(properties);
            var codec = getRedisCodec(properties.getCodec());
            if (properties.getMode() == RedisPoolMode.NO_BLOCKING) {
                var config = buildBoundedPoolConfig(properties);
                var beanDefinition = BeanDefinitionBuilder
                        .genericBeanDefinition(AsyncPool.class,
                                () -> AsyncConnectionPoolSupport
                                        .createBoundedObjectPool(() -> client.connectAsync(codec, uri), config))
                        .addDependsOn(clientBeanName).setPrimary(properties.isPrimary()).getBeanDefinition();
                registry.registerBeanDefinition(beanName, beanDefinition);
            } else {
                GenericPoolRegistry.registerGenericPoolBean(registry, clientBeanName, client, properties, beanName, uri,
                        codec);
            }
        }

        private static final BoundedPoolConfig buildBoundedPoolConfig(RedisPoolProperties properties) {
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

        private void registerBeans(RedisClusterClientProperties properties) {
            ClientResources.Builder builder = ClientResources.builder();
            if (properties.getIoThreads() > 0) {
                builder.ioThreadPoolSize(properties.getIoThreads());
            }
            if (properties.getComputationThreads() > 0) {
                builder.computationThreadPoolSize(properties.getComputationThreads());
            }
            RedisURI uri = createUri(properties);
            String name = properties.getName();
            String beanName = name + "RedisClusterClient";
            RedisClusterClient client = RedisClusterClient.create(builder.build(), uri);
            registry.registerBeanDefinition(beanName,
                    BeanDefinitionBuilder.genericBeanDefinition(RedisClusterClient.class, () -> client)
                            .setDestroyMethodName("shutdown").setPrimary(properties.isPrimary()).getBeanDefinition());
            if (properties.getConnections().size() == 1) {
                properties.getConnections().get(0).setPrimary(true);
            }
            for (RedisConnectionProperties connectionProperties : properties.getConnections()) {
                registerConnectionBean(beanName, client, connectionProperties);
            }
        }

        private static final RedisURI createUri(RedisClusterClientProperties properties) {
            var uri = properties.getUri();
            if (uri != null) {
                return RedisURI.create(uri);
            }
            var redisUri = RedisURI.create(properties.getHost(), properties.getPort());
            var password = properties.getPassword();
            if (password != null && !password.isBlank()) {
                redisUri.setPassword((CharSequence) password.trim());
                var username = properties.getUsername();
                if (username != null && !username.isBlank()) {
                    redisUri.setUsername(username);
                }
            }
            return redisUri;
        }

        private void registerConnectionBean(String clientBeanName, RedisClusterClient client,
                RedisConnectionProperties properties) {
            var beanName = properties.getName() + "RedisClusterConnection";
            var codec = getRedisCodec(properties.getCodec());
            if (properties.getType() == RedisConnectionType.NORMAL) {
                var beanDefinition = BeanDefinitionBuilder
                        .genericBeanDefinition(StatefulRedisClusterConnection.class, () -> client.connect(codec))
                        .addDependsOn(clientBeanName).setPrimary(properties.isPrimary()).getBeanDefinition();
                registry.registerBeanDefinition(beanName, beanDefinition);
            } else if (properties.getType() == RedisConnectionType.PUBSUB) {
                var beanDefinition = BeanDefinitionBuilder
                        .genericBeanDefinition(StatefulRedisClusterPubSubConnection.class,
                                () -> client.connectPubSub(codec))
                        .addDependsOn(clientBeanName).setPrimary(properties.isPrimary()).getBeanDefinition();
                registry.registerBeanDefinition(beanName, beanDefinition);
            } else if (properties.getType() == RedisConnectionType.SENTINEL) {
                throw new IllegalArgumentException("SENTINEL is unsupported in RedisCluster");
            }
        }

    }

}
