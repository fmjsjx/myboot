package com.github.fmjsjx.myboot.autoconfigure.kafka;

import java.time.Duration;
import java.util.Properties;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.jspecify.annotations.NonNull;
import org.springframework.util.unit.DataSize;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Properties class for consumer.
 */
@Getter
@Setter
@ToString
public class ConsumerProperties {

    /**
     * The name.
     */
    @NonNull
    private String name;
    /**
     * The default is <code>"${name}KafkaConsumer"</code>.
     */
    private String beanName;

    /**
     * The default is
     * {@code org.apache.kafka.common.serialization.StringDeserializer}.
     */
    private Class<? extends Deserializer<?>> keyDeserializer = StringDeserializer.class;
    /**
     * The default is
     * {@code org.apache.kafka.common.serialization.StringDeserializer}.
     */
    private Class<? extends Deserializer<?>> valueDeserializer = StringDeserializer.class;
    /**
     * The bootstrap servers.
     */
    @NonNull
    private String bootstrapServers;
    /**
     * The group id.
     */
    @NonNull
    private String groupId;
    /**
     * The group instance id.
     */
    private String groupInstanceId;
    /**
     * The default is {@code 1B}.
     */
    private DataSize fetchMinSize;
    /**
     * The default is {@code 512MB}.
     */
    private DataSize fetchMaxSize;
    /**
     * The default is {@code 3S}.
     */
    private Duration heartbeatInterval;
    /**
     * The default is {@code 10S}.
     */
    private Duration sessionTimeout;
    /**
     * The default is {@code latest}.
     */
    private AutoOffsetReset autoOffsetReset;
    /**
     * The default if {@code 1MB}.
     */
    private DataSize maxPartitionFetchSize;
    /**
     * The default is {@code true}.
     */
    private Boolean enableAutoCommit;
    /**
     * Other producer configurations.
     */
    private Properties configs;
}
