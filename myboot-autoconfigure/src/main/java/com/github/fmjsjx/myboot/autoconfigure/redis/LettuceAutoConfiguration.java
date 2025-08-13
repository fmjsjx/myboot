package com.github.fmjsjx.myboot.autoconfigure.redis;

import java.util.Optional;

import com.github.fmjsjx.libcommon.util.StringUtil;
import io.lettuce.core.RedisCredentials;
import io.lettuce.core.StaticCredentialsProvider;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import io.lettuce.core.cluster.RedisClusterURIUtil;
import io.lettuce.core.masterreplica.MasterReplica;
import io.lettuce.core.masterreplica.StatefulRedisMasterReplicaConnection;
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
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Auto-Configuration class for {@code REDIS/Lettuce}.
 */
@Configuration
@ConditionalOnClass(RedisClient.class)
@ConditionalOnMissingBean(RedisClient.class)
@EnableConfigurationProperties(LettuceProperties.class)
public class LettuceAutoConfiguration {

    /**
     * Returns a new {@link LettuceRegistryProcessor} instance.
     *
     * @return a new {@code LettuceRegistryProcessor} instance
     */
    @Bean
    public static final LettuceRegistryProcessor lettuceRegistryProcessor() {
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
                    registerClientBeans(props.getClient());
                }
                props.getClusterClients().forEach(this::registerClusterClientBeans);
            });
        }

        private void registerClientBeans(RedisClientProperties properties) throws BeansException {
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
            if (StringUtil.isBlank(properties.getBeanName())) {
                clientBeanName = "redisClient";
            } else {
                clientBeanName = properties.getBeanName();
            }
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
            if (properties.getType() == null) {
                properties.setType(RedisConnectionType.NORMAL);
            }
            switch (properties.getType()) {
                case NORMAL -> {
                    var beanDefinition = BeanDefinitionBuilder
                            .genericBeanDefinition(StatefulRedisConnection.class, () -> client.connect(codec, uri))
                            .addDependsOn(clientBeanName).setPrimary(properties.isPrimary()).getBeanDefinition();
                    registry.registerBeanDefinition(beanName, beanDefinition);
                }
                case PUBSUB -> {
                    var beanDefinition = BeanDefinitionBuilder
                            .genericBeanDefinition(StatefulRedisPubSubConnection.class,
                                    () -> client.connectPubSub(codec, uri))
                            .addDependsOn(clientBeanName).setPrimary(properties.isPrimary()).getBeanDefinition();
                    registry.registerBeanDefinition(beanName, beanDefinition);
                }
                case SENTINEL -> {
                    var beanDefinition = BeanDefinitionBuilder
                            .genericBeanDefinition(StatefulRedisSentinelConnection.class,
                                    () -> client.connectSentinel(codec, uri))
                            .addDependsOn(clientBeanName).setPrimary(properties.isPrimary()).getBeanDefinition();
                    registry.registerBeanDefinition(beanName, beanDefinition);
                }
                case MASTER_REPLICA -> {
                    var beanDefinition = BeanDefinitionBuilder
                            .genericBeanDefinition(StatefulRedisMasterReplicaConnection.class,
                                    () -> MasterReplica.connect(client, codec, uri))
                            .addDependsOn(clientBeanName).setPrimary(properties.isPrimary()).getBeanDefinition();
                    registry.registerBeanDefinition(beanName, beanDefinition);
                }
            }
        }

        private static final RedisURI createUri(RedisConnectionProperties properties) {
            var uri = properties.getUri();
            if (uri != null) {
                return RedisURI.create(uri);
            }
            var redisUri = RedisURI.create(properties.getHost(), properties.getPort());
            redisUri.setDatabase(properties.getDb());
            var credentials = RedisCredentials.just(properties.getUsername(), properties.getPassword());
            redisUri.setCredentialsProvider(new StaticCredentialsProvider(credentials));
            return redisUri;
        }

        private static final RedisCodec<?, ?> getRedisCodec(RedisConnectionCodec codec) {
            return switch (codec) {
                case ASCII -> StringCodec.ASCII;
                case BYTE_ARRAY -> ByteArrayCodec.INSTANCE;
                default -> StringCodec.UTF8;
            };
        }

        private void registerPoolBean(RedisClient client, RedisPoolProperties properties) throws BeansException {
            var beanName = Optional.ofNullable(properties.getBeanName())
                    .orElseGet(() -> properties.getName() + "RedisPool");
            if (properties.getType() != RedisConnectionType.NORMAL) {
                throw new FatalBeanException("Redis connection type must be normal for pools");
            }
            var uri = createUri(properties);
            var codec = getRedisCodec(properties.getCodec());
            if (properties.getMode().isAsync()) {
                AsyncPoolRegistry.registerAsyncPoolBean(registry, clientBeanName, client, properties, beanName, uri,
                        codec);
            } else {
                CommonsPoolRegistry.registerCommonsPoolBean(registry, clientBeanName, client, properties, beanName, uri,
                        codec);
            }
        }

        private void registerClusterClientBeans(RedisClusterClientProperties properties) {
            ClientResources.Builder builder = ClientResources.builder();
            if (properties.getIoThreads() > 0) {
                builder.ioThreadPoolSize(properties.getIoThreads());
            }
            if (properties.getComputationThreads() > 0) {
                builder.computationThreadPoolSize(properties.getComputationThreads());
            }
            String beanName;
            if (StringUtil.isNotBlank(properties.getBeanName())) {
                beanName = properties.getBeanName();
            } else if (StringUtil.isNotBlank(properties.getName())) {
                beanName = properties.getName() + "RedisClusterClient";
            } else {
                beanName = "redisClusterClient";
            }
            RedisClusterClient client;
            if (properties.getUris() != null && !properties.getUris().isEmpty()) {
                var uris = properties.getUris().stream().map(RedisURI::create).toList();
                client = RedisClusterClient.create(builder.build(), uris);
            } else if (properties.getUri() != null) {
                client = RedisClusterClient.create(builder.build(), RedisClusterURIUtil.toRedisURIs(properties.getUri()));
            } else {
                var uri = createUri(properties);
                client = RedisClusterClient.create(builder.build(), uri);
            }
            if (properties.getTopologyRefresh() != null) {
                var optionsBuilder = ClusterTopologyRefreshOptions.builder();
                var topologyRefresh = properties.getTopologyRefresh();
                var anySetup = false;
                if (topologyRefresh.isPeriodicRefreshEnabled()) {
                    anySetup = true;
                    if (topologyRefresh.getRefreshPeriod() != null && topologyRefresh.getRefreshPeriod().toNanos() > 0) {
                        optionsBuilder.enablePeriodicRefresh(topologyRefresh.getRefreshPeriod());
                    } else {
                        optionsBuilder.enablePeriodicRefresh();
                    }
                }
                if (topologyRefresh.isEnableAllAdaptiveRefreshTriggers()) {
                    anySetup = true;
                    optionsBuilder.enableAllAdaptiveRefreshTriggers();
                } else if (topologyRefresh.getAdaptiveRefreshTriggers() != null && topologyRefresh.getAdaptiveRefreshTriggers().length > 0) {
                    anySetup = true;
                    optionsBuilder.enableAdaptiveRefreshTrigger(topologyRefresh.getAdaptiveRefreshTriggers());
                }
                if (topologyRefresh.getAdaptiveRefreshTriggersTimeout() != null && topologyRefresh.getAdaptiveRefreshTriggersTimeout().toNanos() > 0) {
                    anySetup = true;
                    optionsBuilder.adaptiveRefreshTriggersTimeout(topologyRefresh.getAdaptiveRefreshTriggersTimeout());
                }
                if (anySetup) {
                    client.setOptions(ClusterClientOptions.builder(client.getOptions())
                            .topologyRefreshOptions(optionsBuilder.build())
                            .build());
                }
            }
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
            var credentials = RedisCredentials.just(properties.getUsername(), properties.getPassword());
            redisUri.setCredentialsProvider(new StaticCredentialsProvider(credentials));
            return redisUri;
        }

        private void registerConnectionBean(String clientBeanName, RedisClusterClient client,
                                            RedisConnectionProperties properties) {
            var beanName = Optional.ofNullable(properties.getBeanName())
                    .orElseGet(() -> properties.getName() + "RedisClusterConnection");
            var codec = getRedisCodec(properties.getCodec());
            if (properties.getType() == null) {
                properties.setType(RedisConnectionType.NORMAL);
            }
            switch (properties.getType()) {
                case NORMAL -> {
                    var beanDefinition = BeanDefinitionBuilder
                            .genericBeanDefinition(StatefulRedisClusterConnection.class, () -> client.connect(codec))
                            .addDependsOn(clientBeanName).setPrimary(properties.isPrimary()).getBeanDefinition();
                    registry.registerBeanDefinition(beanName, beanDefinition);
                }
                case PUBSUB -> {
                    var beanDefinition = BeanDefinitionBuilder
                            .genericBeanDefinition(StatefulRedisClusterPubSubConnection.class,
                                    () -> client.connectPubSub(codec))
                            .addDependsOn(clientBeanName).setPrimary(properties.isPrimary()).getBeanDefinition();
                    registry.registerBeanDefinition(beanName, beanDefinition);
                }
                case SENTINEL, MASTER_REPLICA ->
                    throw new IllegalArgumentException(properties.getType() + "is unsupported in RedisCluster");
            }
        }

    }

}
