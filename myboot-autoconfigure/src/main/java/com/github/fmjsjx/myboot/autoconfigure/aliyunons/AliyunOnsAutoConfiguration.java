package com.github.fmjsjx.myboot.autoconfigure.aliyunons;

import java.lang.reflect.InvocationTargetException;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.PullConsumer;
import com.aliyun.openservices.ons.api.batch.BatchConsumer;
import com.aliyun.openservices.ons.api.order.OrderConsumer;
import com.aliyun.openservices.ons.api.order.OrderProducer;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionChecker;
import com.aliyun.openservices.ons.api.transaction.TransactionProducer;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The auto-configuration class for ALIYUN ONS.
 */
@Slf4j
@Configuration
@ConditionalOnClass(ONSFactory.class)
@EnableConfigurationProperties(AliyunOnsProperties.class)
public class AliyunOnsAutoConfiguration {

    /**
     * Returns new {@link OnsRegistryProcessor} instance.
     * 
     * @return new {@code OnsRegistryProcessor} instance
     */
    @Bean
    public static OnsRegistryProcessor onsRegistryProcessor() {
        return new OnsRegistryProcessor();
    }

    /**
     * Registry processor class for ALIYUN-ONS.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class OnsRegistryProcessor implements EnvironmentAware, BeanDefinitionRegistryPostProcessor {

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
            var bindResult = Binder.get(environment).bind(AliyunOnsProperties.CONFIG_PREFIX, AliyunOnsProperties.class);
            if (bindResult.isBound()) {
                var onsProps = bindResult.get();
                Optional.ofNullable(onsProps.getProducers())
                        .ifPresent(configs -> configs.forEach(this::registerProducer));
                Optional.ofNullable(onsProps.getConsumers())
                        .ifPresent(configs -> configs.forEach(this::registerConsumer));
            }
        }

        private void registerProducer(ProducerProperties config) {
            var name = config.getName();
            var beanName = Optional.ofNullable(config.getBeanName()).orElseGet(() -> name + "ONSProducer");
            Properties properties = new Properties();
            setBaseProperties(config, properties);
            if (config.getSendMsgTimeout() != null) {
                properties.setProperty(PropertyKeyConst.SendMsgTimeoutMillis,
                        String.valueOf(config.getSendMsgTimeout().toMillis()));
            }
            if (config.getCheckImmunityTime() != null) {
                properties.setProperty(PropertyKeyConst.CheckImmunityTimeInSeconds,
                        String.valueOf(config.getCheckImmunityTime().toSeconds()));
            }
            switch (config.getType()) {
                case NORMAL -> {
                    var beanDefinition = BeanDefinitionBuilder
                            .genericBeanDefinition(Producer.class, () -> ONSFactory.createProducer(properties))
                            .setScope(BeanDefinition.SCOPE_SINGLETON).setInitMethodName("start")
                            .setDestroyMethodName("shutdown").getBeanDefinition();
                    log.debug("Register Aliyun Ons Producer {}: {}", beanName, beanDefinition);
                    registry.registerBeanDefinition(beanName, beanDefinition);
                }
                case ORDER -> {
                    var beanDefinition = BeanDefinitionBuilder
                            .genericBeanDefinition(OrderProducer.class, () -> ONSFactory.createOrderProducer(properties))
                            .setScope(BeanDefinition.SCOPE_SINGLETON).setInitMethodName("start")
                            .setDestroyMethodName("shutdown").getBeanDefinition();
                    log.debug("Register Aliyun Ons OrderProducer {}: {}", beanName, beanDefinition);
                    registry.registerBeanDefinition(beanName, beanDefinition);
                }
                case TRANSACTION -> {
                    if (config.getTransactionCheckerClass() == null) {
                        throw new NoSuchElementException("libcommons.aliyun-ons." + name + ".transaction-checker-class");
                    }
                    Class<? extends LocalTransactionChecker> transactionCheckerClass = config.getTransactionCheckerClass();
                    var beanDefinition = BeanDefinitionBuilder
                            .genericBeanDefinition(TransactionProducer.class,
                                    () -> ONSFactory.createTransactionProducer(properties,
                                            newInstance(transactionCheckerClass)))
                            .setScope(BeanDefinition.SCOPE_SINGLETON).setInitMethodName("start")
                            .setDestroyMethodName("shutdown").getBeanDefinition();
                    log.debug("Register Aliyun Ons TransactionProducer {}: {}", beanName, beanDefinition);
                    registry.registerBeanDefinition(beanName, beanDefinition);
                }
            }

        }

        private <T> T newInstance(Class<? extends T> clazz) {
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                throw new RuntimeException(e);
            }
        }

        private void setBaseProperties(ConfigProperties config, Properties properties) {
            properties.setProperty(PropertyKeyConst.NAMESRV_ADDR, config.getNamesrvAddr());
            properties.setProperty(PropertyKeyConst.AccessKey, config.getAccessKey());
            properties.setProperty(PropertyKeyConst.SecretKey, config.getSecretKey());
            if (config.getSecretToken() != null) {
                properties.setProperty(PropertyKeyConst.SecurityToken, config.getSecretToken());
            }
            if (config.getMqType() != null) {
                properties.setProperty(PropertyKeyConst.MQType, config.getMqType().name());
            }
            if (config.getGroupId() != null) {
                properties.setProperty(PropertyKeyConst.GROUP_ID, config.getGroupId());
            }
        }

        private void registerConsumer(ConsumerProperties config) {
            var name = config.getName();
            var beanName = Optional.ofNullable(config.getBeanName()).orElseGet(() -> name + "ONSConsumer");
            Properties properties = new Properties();
            setBaseProperties(config, properties);
            if (config.getMessageModel() != null) {
                properties.setProperty(PropertyKeyConst.MessageModel, config.getMessageModel().name());
            }
            if (config.getConsumeThreadNums() != null) {
                properties.setProperty(PropertyKeyConst.ConsumeThreadNums, config.getConsumeThreadNums().toString());
            }
            if (config.getMaxReconsumeTimes() != null) {
                properties.setProperty(PropertyKeyConst.MaxReconsumeTimes, config.getMaxReconsumeTimes().toString());
            }
            if (config.getConsumeTimeout() != null) {
                properties.setProperty(PropertyKeyConst.ConsumeTimeout,
                        String.valueOf(config.getConsumeTimeout().toMinutes()));
            }
            if (config.getSuspendTime() != null) {
                properties.setProperty(PropertyKeyConst.SuspendTimeMillis,
                        String.valueOf(config.getSuspendTime().toMillis()));
            }
            if (config.getMaxCachedMessageAmount() != null) {
                properties.setProperty(PropertyKeyConst.MaxCachedMessageAmount,
                        config.getMaxCachedMessageAmount().toString());
            }
            if (config.getMaxCachedMessageSize() != null) {
                var mib = Math.min(2048, Math.max(16, config.getMaxCachedMessageSize().toMegabytes()));
                properties.setProperty(PropertyKeyConst.MaxCachedMessageSizeInMiB, String.valueOf(mib));
            }
            switch (config.getType()) {
                case NORMAL -> {
                    var beanDefinition = BeanDefinitionBuilder
                            .genericBeanDefinition(Consumer.class, () -> ONSFactory.createConsumer(properties))
                            .setScope(BeanDefinition.SCOPE_SINGLETON).setDestroyMethodName("shutdown").getBeanDefinition();
                    log.debug("Register Aliyun Ons Consumer {}: {}", beanName, beanDefinition);
                    registry.registerBeanDefinition(beanName, beanDefinition);
                }
                case BATCH -> {
                    var consumeMessageBatchMaxSize = 32;
                    if (config.getConsumeMessageBatchMaxSize() != null) {
                        var size = config.getConsumeMessageBatchMaxSize().intValue();
                        consumeMessageBatchMaxSize = Math.max(Math.min(32, size), 1);
                    }
                    properties.setProperty(PropertyKeyConst.ConsumeMessageBatchMaxSize,
                            String.valueOf(consumeMessageBatchMaxSize));
                    var beanDefinition = BeanDefinitionBuilder
                            .genericBeanDefinition(BatchConsumer.class, () -> ONSFactory.createBatchConsumer(properties))
                            .setScope(BeanDefinition.SCOPE_SINGLETON).setDestroyMethodName("shutdown").getBeanDefinition();
                    log.debug("Register Aliyun Ons BatchConsumer {}: {}", beanName, beanDefinition);
                    registry.registerBeanDefinition(beanName, beanDefinition);
                }
                case ORDERED -> {
                    var beanDefinition = BeanDefinitionBuilder
                            .genericBeanDefinition(OrderConsumer.class, () -> ONSFactory.createOrderedConsumer(properties))
                            .setScope(BeanDefinition.SCOPE_SINGLETON).setDestroyMethodName("shutdown").getBeanDefinition();
                    log.debug("Register Aliyun Ons OrderConsumer {}: {}", beanName, beanDefinition);
                    registry.registerBeanDefinition(beanName, beanDefinition);
                }
                case PULL -> {
                    if (config.getAutoCommit() != null) {
                        properties.setProperty(PropertyKeyConst.AUTO_COMMIT, config.getAutoCommit().toString());
                    }
                    if (config.getAutoCommitInterval() != null) {
                        properties.setProperty(PropertyKeyConst.AUTO_COMMIT_INTERVAL_MILLIS,
                                String.valueOf(config.getAutoCommitInterval().toMillis()));
                    }
                    if (config.getPollTimeout() != null) {
                        properties.setProperty(PropertyKeyConst.POLL_TIMEOUT_MILLIS,
                                String.valueOf(config.getPollTimeout().toMillis()));
                    }
                    var beanDefinition = BeanDefinitionBuilder
                            .genericBeanDefinition(PullConsumer.class, () -> ONSFactory.createPullConsumer(properties))
                            .setScope(BeanDefinition.SCOPE_SINGLETON).setDestroyMethodName("shutdown").getBeanDefinition();
                    log.debug("Register Aliyun Ons PullConsumer {}: {}", beanName, beanDefinition);
                    registry.registerBeanDefinition(beanName, beanDefinition);
                }
            }
        }

    }

}
