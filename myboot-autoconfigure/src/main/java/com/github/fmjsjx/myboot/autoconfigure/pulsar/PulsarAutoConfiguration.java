package com.github.fmjsjx.myboot.autoconfigure.pulsar;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.apache.pulsar.client.api.ClientBuilder;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.PulsarClientException.UnsupportedAuthenticationException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Auto-Configuration class for Pulsar.
 */
@AutoConfiguration
@EnableConfigurationProperties(PulsarProperties.class)
@ConditionalOnClass(PulsarClient.class)
public class PulsarAutoConfiguration {

    /**
     * Returns new {@link PulsarRegistryProcessor} instance.
     *
     * @return new {@code PulsarRegistryProcessor} instance
     */
    @Bean
    public static PulsarRegistryProcessor pulsarRegistryProcessor() {
        return new PulsarRegistryProcessor();
    }

    /**
     * Registry processor for Pulsar.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class PulsarRegistryProcessor implements EnvironmentAware, BeanDefinitionRegistryPostProcessor {

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
            var bindResult = Binder.get(environment).bind(PulsarProperties.CONFIG_PREFIX, PulsarProperties.class);
            if (bindResult.isBound()) {
                var properties = bindResult.get();
                if (properties.getClients() != null) {
                    properties.getClients().forEach(this::registerClientBean);
                }
            }
        }

        private void registerClientBean(PulsarClientProperties config) {
            String beanName = Optional.ofNullable(config.getBeanName())
                    .orElseGet(() -> config.getName() + "PulsarClient");
            var builder = PulsarClient.builder();
            configClientBuilder(config, beanName, builder);
            Supplier<PulsarClient> beanFactory = () -> {
                try {
                    return builder.build();
                } catch (PulsarClientException e) {
                    throw unexpectedErrorOccurs(beanName, e);
                }
            };
            registry.registerBeanDefinition(beanName,
                    BeanDefinitionBuilder.genericBeanDefinition(PulsarClient.class, beanFactory)
                            .setDestroyMethodName("close").getBeanDefinition());
        }

        @SuppressWarnings("deprecation")
        void configClientBuilder(PulsarClientProperties config, String beanName, ClientBuilder builder) {
            Optional.ofNullable(config.getServiceUrl()).ifPresent(builder::serviceUrl);
            Optional.ofNullable(config.getListenerName()).ifPresent(builder::listenerName);
            Optional.ofNullable(config.getAuthPluginClassName()).ifPresent(authPluginClassName -> {
                try {
                    if (config.getAuthParams() != null) {
                        builder.authentication(authPluginClassName, config.getAuthParams());
                    } else {
                        builder.authentication(authPluginClassName, config.getAuthParamsString());
                    }
                } catch (UnsupportedAuthenticationException e) {
                    throw unexpectedErrorOccurs(beanName, e);
                }
            });
            Optional.ofNullable(config.getOperationTimeout())
                    .ifPresent(ot -> builder.operationTimeout((int) ot.toMillis(), TimeUnit.MILLISECONDS));
            Optional.ofNullable(config.getStatsInterval())
                    .ifPresent(si -> builder.statsInterval(si.toSeconds(), TimeUnit.SECONDS));
            Optional.ofNullable(config.getNumIoThreads()).ifPresent(builder::ioThreads);
            Optional.ofNullable(config.getNumListenerThreads()).ifPresent(builder::listenerThreads);
            Optional.ofNullable(config.getConnectionsPerBroker()).ifPresent(builder::connectionsPerBroker);
            Optional.ofNullable(config.getUseTcpNoDelay()).ifPresent(builder::enableTcpNoDelay);
            Optional.ofNullable(config.getTlsTrustCertsFilePath()).ifPresent(builder::tlsTrustCertsFilePath);
            Optional.ofNullable(config.getTlsAllowInsecureConnection()).ifPresent(builder::allowTlsInsecureConnection);
            Optional.ofNullable(config.getTlsHostnameVerificationEnable()).ifPresent(builder::enableTlsHostnameVerification);
            Optional.ofNullable(config.getUseKeyStoreTls()).ifPresent(builder::useKeyStoreTls);
            Optional.ofNullable(config.getSslProvider()).ifPresent(builder::sslProvider);
            Optional.ofNullable(config.getTlsTrustStoreType()).ifPresent(builder::tlsTrustStoreType);
            Optional.ofNullable(config.getTlsTrustStorePath()).ifPresent(builder::tlsTrustStorePath);
            Optional.ofNullable(config.getTlsTrustStorePassword()).ifPresent(builder::tlsTrustStorePassword);
            Optional.ofNullable(config.getTlsCiphers()).filter(s -> !s.isEmpty()).ifPresent(builder::tlsCiphers);
            Optional.ofNullable(config.getTlsProtocols()).filter(s -> !s.isEmpty()).ifPresent(builder::tlsProtocols);
            Optional.ofNullable(config.getConcurrentLookupRequest()).ifPresent(builder::maxConcurrentLookupRequests);
            Optional.ofNullable(config.getMaxLookupRequest()).ifPresent(builder::maxLookupRequests);
            Optional.ofNullable(config.getMaxLookupRedirects()).ifPresent(builder::maxLookupRedirects);
            Optional.ofNullable(config.getMaxNumberOfRejectedRequestPerConnection())
                    .ifPresent(builder::maxNumberOfRejectedRequestPerConnection);
            Optional.ofNullable(config.getKeepAliveInterval())
                    .ifPresent(kai -> builder.keepAliveInterval((int) kai.toSeconds(), TimeUnit.SECONDS));
            Optional.ofNullable(config.getConnectionTimeout())
                    .ifPresent(ct -> builder.connectionTimeout((int) ct.toMillis(), TimeUnit.MILLISECONDS));
            Optional.ofNullable(config.getDefaultBackoffIntervalNanos())
                    .ifPresent(dbin -> builder.startingBackoffInterval(dbin.toNanos(), TimeUnit.NANOSECONDS));
            Optional.ofNullable(config.getMaxBackoffIntervalNanos())
                    .ifPresent(mbi -> builder.maxBackoffInterval(mbi.toNanos(), TimeUnit.NANOSECONDS));
        }

        private static final BeanCreationException unexpectedErrorOccurs(String beanName, Throwable cause) {
            return new BeanCreationException(beanName, "Unexpected error occurs when create pulsar client", cause);
        }
    }

}
