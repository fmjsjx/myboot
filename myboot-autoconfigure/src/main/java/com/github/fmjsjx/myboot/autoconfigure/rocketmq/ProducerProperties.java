package com.github.fmjsjx.myboot.autoconfigure.rocketmq;

import java.time.Duration;

import org.apache.rocketmq.client.AccessChannel;
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
public class ProducerProperties implements ConfigProperties {

    @NonNull
    private String name;
    private String beanName;

    @NonNull
    private String namesrvAddr;

    private String accessKey;

    private String secretKey;

    private String secretToken;

    private AccessChannel accessChannel;

    /**
     * The producer type.
     * <p>
     * The default is {@code default}.
     */
    private ProducerType type = ProducerType.DEFAULT;

    /**
     * The timeout duration for sending messages.
     */
    private Duration sendMsgTimeout;
    /**
     * The namespace.
     */
    private String namespace;

    private String groupId;

    /**
     * The default is {@code 4KB}.
     */
    private DataSize compressMsgBodyOverHowmuch;
    /**
     * The default is {@code 2}.
     */
    private Integer retryTimesWhenSendFailed;
    /**
     * The default is {@code false}.
     */
    private Boolean retryAnotherBrokerWhenNotStoreOK;
    /**
     * The default is {@code 4MB}.
     */
    private DataSize maxMessageSize;
}
