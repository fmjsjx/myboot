package com.github.fmjsjx.myboot.autoconfigure.rocketmq;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Configuration properties class for RocketMQ.
 */
@Getter
@Setter
@ToString
@ConfigurationProperties(RocketMQProperties.CONFIG_PREFIX)
public class RocketMQProperties {

    static final String CONFIG_PREFIX = "myboot.rocketmq";

    /**
     * The producers.
     */
    private List<ProducerProperties> producers;
    /**
     * The consumers.
     */
    private List<ConsumerProperties> consumers;

}
