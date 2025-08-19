package com.github.fmjsjx.myboot.autoconfigure.rocketmq;

/**
 * Enumeration for consumer types.
 *
 * @deprecated Deprecated since 3.6 and may be removed in future version.
 */
@Deprecated(since = "3.6", forRemoval = true)
public enum ConsumerType {
    /**
     * MQPushConsumer.
     */
    PUSH,
    /**
     * LitePullConsumer.
     */
    LITE_PULL
}
