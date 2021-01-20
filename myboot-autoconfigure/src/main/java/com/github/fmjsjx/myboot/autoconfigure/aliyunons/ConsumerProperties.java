package com.github.fmjsjx.myboot.autoconfigure.aliyunons;

import java.time.Duration;

import org.springframework.lang.NonNull;
import org.springframework.util.unit.DataSize;

import com.aliyun.openservices.ons.api.MQType;
import com.aliyun.openservices.ons.api.impl.rocketmq.ONSChannel;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.protocol.heartbeat.MessageModel;

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
    @NonNull
    private String accessKey;
    @NonNull
    private String secretKey;

    private String secretToken;
    private ONSChannel onsChannel;
    private MQType mqType;
    /**
     * The consumer type.
     * <p>
     * The default is {@code normal}.
     */
    private ConsumerType type = ConsumerType.NORMAL;
    @NonNull
    private String groupId;
    /**
     * The message model.
     */
    private MessageModel messageModel;
    /**
     * The default is {@code 20}.
     */
    private Integer consumeThreadNums;
    /**
     * The default is {@code 16}.
     */
    private Integer maxReconsumeTimes;
    /**
     * The default is {@code 15M}.
     */
    private Duration consumeTimeout;
    /**
     * Only works on OrderedConsumer.
     */
    private Duration suspendTime;
    /**
     * The default is {@code 1000}.
     */
    private Integer maxCachedMessageAmount;
    /**
     * The default is {@code 512MB}. (For PullConsumer is {@code 100MB}.)
     */
    private DataSize maxCachedMessageSize;
    /**
     * The default is {@code 32}.
     */
    private Integer consumeMessageBatchMaxSize;
    /**
     * The default is {@code true}.
     */
    private Boolean autoCommit;
    /**
     * The default is {@code 5S}.
     */
    private Duration autoCommitInterval;
    /**
     * The default is {@code 5S}.
     */
    private Duration pollTimeout;

}
