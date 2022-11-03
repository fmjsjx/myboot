package com.github.fmjsjx.myboot.autoconfigure.kafka;

import java.util.Properties;

import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.lang.NonNull;
import org.springframework.util.unit.DataSize;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Properties class for producer.
 */
@Getter
@Setter
@ToString
public class ProducerProperties {

    /**
     * The name.
     */
    @NonNull
    private String name;
    /**
     * The default is <code>"${name}KafkaProducer"</code>.
     */
    private String beanName;

    /**
     * The default is
     * {@code org.apache.kafka.common.serialization.StringSerializer}.
     */
    private Class<? extends Serializer<?>> keySerializer = StringSerializer.class;
    /**
     * The default is
     * {@code org.apache.kafka.common.serialization.StringSerializer}.
     */
    private Class<? extends Serializer<?>> valueSerializer = StringSerializer.class;

    /**
     * The default is {@code 1}.
     */
    private String acks;

    /**
     * The bootstrap servers.
     */
    @NonNull
    private String bootstrapServers;
    /**
     * The default is {@code 32MB}.
     */
    private DataSize bufferMemory;
    /**
     * The default is {@code none}.
     */
    private CompressionType compressionType;
    /**
     * The default is {@code Integer.MAX_VALUE}.
     */
    private Integer retries;
    /**
     * Other producer configurations.
     */
    @NestedConfigurationProperty
    private Properties configs;
}
