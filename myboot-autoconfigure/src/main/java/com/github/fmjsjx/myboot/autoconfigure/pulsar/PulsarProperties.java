package com.github.fmjsjx.myboot.autoconfigure.pulsar;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * Configuration properties class for Pulsar.
 */
@Getter
@Setter
@ToString
@ConfigurationProperties(PulsarProperties.CONFIG_PREFIX)
public class PulsarProperties {

    static final String CONFIG_PREFIX = "myboot.pulsar";

    /**
     * The clients.
     */
    @NestedConfigurationProperty
    private List<PulsarClientProperties> clients;

}
