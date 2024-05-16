package com.github.fmjsjx.myboot.autoconfigure.rocketmq;

import java.time.Duration;

import org.apache.rocketmq.client.AccessChannel;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.remoting.protocol.heartbeat.MessageModel;
import org.springframework.lang.NonNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Properties class for consumer.
 */
@Getter
@Setter
@ToString
public class ConsumerProperties implements ConfigProperties {

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
     * The consumer type.
     * <p>
     * The default is {@code push}.
     */
    private ConsumerType type = ConsumerType.PUSH;

    /**
     * The namespace.
     *
     * @deprecated please use {@link #namespaceV2} instead
     */
    @Deprecated
    private String namespace;
    /**
     * The namespace v2.
     *
     * @since 3.2.3
     */
    private String namespaceV2;

    @NonNull
    private String groupId;

    private MessageModel messageModel;
    /**
     * The default is {@code 20}.
     */
    private Integer consumeThreadMin;

    /**
     * The default is {@code 20}.
     */
    private Integer consumeThreadMax;
    /**
     * The default is {@code 1}.
     */
    private Integer consumeMessageBatchMaxSize;

    /**
     * The maximum re-consume times.
     */
    private Integer maxReconsumeTimes;

    /**
     * The timeout duration for consume.
     */
    private Duration consumeTimeout;

    /**
     * The suspend time.
     */
    private Duration suspendTime;

    /**
     * The {@link ConsumeFromWhere} enumeration.
     */
    private ConsumeFromWhere consumeFromWhere;

    /**
     * <p>
     * Only works on LitePullConsumer.
     * <p>
     * The default is {@code true}.
     */
    private Boolean autoCommit;
    /**
     * <p>
     * Only works on LitePullConsumer.
     * <p>
     * The default is {@code 5S}.
     */
    private Duration autoCommitInterval;
    /**
     * <p>
     * Only works on LitePullConsumer.
     * <p>
     * The default is {@code 20}.
     */
    private Integer pullThreadNums;
    /**
     * <p>
     * Only works on LitePullConsumer.
     * <p>
     * The default is {@code 10}.
     */
    private Integer pullBatchSize;
    /**
     * <p>
     * Only works on LitePullConsumer.
     * <p>
     * The default is {@code 5S}.
     */
    private Duration pollTimeout;

}
