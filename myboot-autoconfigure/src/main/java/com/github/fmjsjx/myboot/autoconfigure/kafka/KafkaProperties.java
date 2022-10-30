package com.github.fmjsjx.myboot.autoconfigure.kafka;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * Configuration properties class for Kafka.
 */
@Getter
@Setter
@ToString
@ConfigurationProperties(KafkaProperties.CONFIG_PREFIX)
public class KafkaProperties {

    static final String CONFIG_PREFIX = "myboot.kafka";

    /**
     * The producers.
     */
    @NestedConfigurationProperty
    private List<ProducerProperties> producers;
    /**
     * The consumers.
     */
    @NestedConfigurationProperty
    private List<ConsumerProperties> consumers;

}
