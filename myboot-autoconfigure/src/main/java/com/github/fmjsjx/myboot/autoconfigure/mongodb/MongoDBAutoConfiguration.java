package com.github.fmjsjx.myboot.autoconfigure.mongodb;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadFactory;
import java.util.stream.Collectors;

import com.github.fmjsjx.myboot.autoconfigure.mongodb.MongoDBProperties.DriverType;
import com.github.fmjsjx.myboot.autoconfigure.mongodb.MongoDBProperties.MongoClientProperties;
import com.mongodb.MongoClientSettings;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.IoHandlerFactory;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * Auto Configuration for MongoDB.
 */
@Slf4j
@AutoConfiguration
@ConditionalOnClass(MongoClientSettings.class)
@EnableConfigurationProperties(MongoDBProperties.class)
public class MongoDBAutoConfiguration {

    /**
     * Returns new {@link MongoDBRegistryProcessor} instance.
     *
     * @return new {@code MongoDBRegisteryProcessor} instance
     */
    @Bean
    public static MongoDBRegistryProcessor mongodbRegistryProcessor() {
        return new MongoDBRegistryProcessor();
    }

    private static final Object NETTY_LIBRARY_LOCK = new Object();
    private static volatile NettyLibrary nettyLibrary;

    private static final boolean isNettyNativeAvailable(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            return isNettyNativeAvailable(clazz);
        } catch (Exception e) {
            return false;
        }
    }

    private static final boolean isNettyNativeAvailable(Class<?> clazz)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return (Boolean) clazz.getDeclaredMethod("isAvailable").invoke(null);
    }

    @SuppressWarnings("unchecked")
    private static final <T extends SocketChannel> Class<T> socketChannelClass(String className) {
        try {
            return (Class<T>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private static final <T extends EventLoopGroup> T nativeEventLoopGroup(String className,
                                                                           ThreadFactory threadFactory) {
        try {
            var clazz = (Class<T>) Class.forName(className);
            var constructor = clazz.getConstructor(ThreadFactory.class);
            return constructor.newInstance(threadFactory);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings({"unchecked"})
    private static <T extends EventLoopGroup> T ioEventLoopGroup(String handleClassName, ThreadFactory threadFactory) {
        try {
            var factoryClass = Class.forName("io.netty.channel.IoHandlerFactory");
            var clazz = Class.forName(handleClassName);
            var factory = (IoHandlerFactory) clazz.getMethod("newFactory").invoke(null);
            var eventLoopClass = (Class<T>) Class.forName("io.netty.channel.MultiThreadIoEventLoopGroup");
            var constructor = eventLoopClass.getConstructor(ThreadFactory.class, factoryClass);
            return constructor.newInstance(threadFactory, factory);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static NettyLibrary getNettyLibrary() {
        if (nettyLibrary == null) {
            synchronized (NETTY_LIBRARY_LOCK) {
                if (nettyLibrary == null) {
                    var threadFactory = new DefaultThreadFactory("mongodb-stream", true);
                    if (isNettyNativeAvailable("io.netty.channel.uring.IoUring")) {
                        var eventLoopGroup = ioEventLoopGroup(
                                "io.netty.channel.uring.IoUringIoHandler", threadFactory);
                        nettyLibrary = new NettyLibrary(eventLoopGroup,
                                socketChannelClass("io.netty.channel.uring.IoUringSocketChannel"));
                    } else if (isNettyNativeAvailable("io.netty.channel.epoll.Epoll")) {
                        EventLoopGroup eventLoopGroup;
                        try {
                            eventLoopGroup = ioEventLoopGroup(
                                    "io.netty.channel.epoll.EpollIoHandler", threadFactory);
                        } catch (Exception e) {
                            eventLoopGroup = nativeEventLoopGroup(
                                    "io.netty.channel.epoll.EpollEventLoopGroup", threadFactory);
                        }
                        nettyLibrary = new NettyLibrary(eventLoopGroup,
                                socketChannelClass("io.netty.channel.epoll.EpollSocketChannel"));
                    } else if (isNettyNativeAvailable("io.netty.channel.kqueue.KQueue")) {
                        EventLoopGroup eventLoopGroup;
                        try {
                            eventLoopGroup = ioEventLoopGroup(
                                    "io.netty.channel.kqueue.KQueueIoHandler", threadFactory);
                        } catch (Exception e) {
                            eventLoopGroup = nativeEventLoopGroup(
                                    "io.netty.channel.kqueue.KQueueEventLoopGroup", threadFactory);
                        }
                        nettyLibrary = new NettyLibrary(eventLoopGroup,
                                socketChannelClass("io.netty.channel.kqueue.KQueueSocketChannel"));
                    } else {
                        EventLoopGroup eventLoopGroup;
                        try {
                            eventLoopGroup = ioEventLoopGroup(
                                    "io.netty.channel.nio.NioIoHandler", threadFactory);
                        } catch (Exception e) {
                            eventLoopGroup = nativeEventLoopGroup(
                                    "io.netty.channel.nio.NioEventLoopGroup", threadFactory);
                        }
                        nettyLibrary = new NettyLibrary(eventLoopGroup, socketChannelClass("io.netty.channel.nio.NioSocketChannel"));
                    }
                }
            }
        }
        return nettyLibrary;
    }

    record NettyLibrary(EventLoopGroup eventLoopGroup, Class<? extends SocketChannel> socketChannelClass) {
    }

    /**
     * Registry processor for MongoDB.
     */

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class MongoDBRegistryProcessor implements EnvironmentAware, BeanDefinitionRegistryPostProcessor {

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
            var bindResult = Binder.get(environment).bind(MongoDBProperties.CONFIG_PREFIX, MongoDBProperties.class);
            if (bindResult.isBound()) {
                Optional.ofNullable(bindResult.get().getClients()).ifPresent(this::registerClients);
            }
        }

        private void registerClients(List<MongoClientProperties> clients) {
            var groups = clients.stream().collect(Collectors.groupingBy(MongoClientProperties::getDriver));
            var syncGroup = groups.get(DriverType.SYNC);
            if (syncGroup != null) {
                if (syncGroup.size() == 1) {
                    syncGroup.getFirst().setPrimary(true);
                }
                syncGroup.forEach(config -> {
                    log.debug("Register sync client >>> {}", config);
                    SyncMongoClientRegistry.register(registry, config);
                });
            }
            var reactivestreamsGroup = groups.get(DriverType.REACTIVESTREAMS);
            if (reactivestreamsGroup != null) {
                if (reactivestreamsGroup.size() == 1) {
                    reactivestreamsGroup.getFirst().setPrimary(true);
                }
                reactivestreamsGroup.forEach(config -> {
                    log.debug("Register reactivestreams client >>> {}", config);
                    ReactivestreamsMongoClientRegistry.register(registry, config);
                });
            }
        }
    }

}
