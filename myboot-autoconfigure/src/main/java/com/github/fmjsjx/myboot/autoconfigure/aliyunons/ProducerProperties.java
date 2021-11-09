package com.github.fmjsjx.myboot.autoconfigure.aliyunons;

import java.time.Duration;

import org.springframework.lang.NonNull;

import com.aliyun.openservices.ons.api.MQType;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionChecker;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The properties class for producer.
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
    @NonNull
    private String accessKey;
    @NonNull
    private String secretKey;

    private String secretToken;

    private MQType mqType;

    /**
     * The producer type.
     * <p>
     * The default is {@code normal}.
     */
    private ProducerType type = ProducerType.NORMAL;

    private String groupId;

    /**
     * The timeout duration for sending messages.
     */
    private Duration sendMsgTimeout;

    /**
     * The check immunity time.
     */
    private Duration checkImmunityTime;

    /**
     * The transaction checker class.
     */
    private Class<? extends LocalTransactionChecker> transactionCheckerClass;

}
