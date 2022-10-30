package com.github.fmjsjx.myboot.autoconfigure.aliyunons;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * The configuration properties class for ALIYUN ONS.
 */
@Getter
@Setter
@ToString
@ConfigurationProperties(AliyunOnsProperties.CONFIG_PREFIX)
public class AliyunOnsProperties {

    static final String CONFIG_PREFIX = "myboot.aliyun-ons";

    /**
     * Producers.
     */
    @NestedConfigurationProperty
    private List<ProducerProperties> producers;

    /**
     * Consumers.
     */
    @NestedConfigurationProperty
    private List<ConsumerProperties> consumers;

}
