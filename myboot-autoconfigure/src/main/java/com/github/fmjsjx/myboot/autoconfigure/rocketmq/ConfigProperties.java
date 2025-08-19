package com.github.fmjsjx.myboot.autoconfigure.rocketmq;

import org.apache.rocketmq.client.AccessChannel;

/**
 * An interface defines commons getters for configuration properties.
 *
 * @deprecated Deprecated since 3.6 and may be removed in future version.
 */
@Deprecated(since = "3.6", forRemoval = true)
public interface ConfigProperties {

    /**
     * The name.
     * 
     * @return the name
     */
    String getName();

    /**
     * The bean name.
     * <p>
     * The default value is {@code "${name}RocketMQConsumer"} or
     * {@code "${name}RocketMQProducer"}
     * 
     * @return the bean name
     */
    String getBeanName();

    /**
     * The name service address.
     * 
     * @return the name service address
     */
    String getNamesrvAddr();

    /**
     * The access key.
     * 
     * @return the access key
     */
    String getAccessKey();

    /**
     * The secret key.
     * 
     * @return the secret key
     */
    String getSecretKey();

    /**
     * The secret token.
     * 
     * @return the secret token
     */
    String getSecretToken();

    /**
     * The access channel.
     * 
     * @return the access channel
     */
    AccessChannel getAccessChannel();

    /**
     * The group id.
     * 
     * @return the group id
     */
    String getGroupId();

}
