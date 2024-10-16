package com.github.fmjsjx.myboot.autoconfigure.aliyunons;

import com.aliyun.openservices.ons.api.MQType;

/**
 * An interface defines common getters for configuration properties.
 */
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
     * The default is {@code "${name}ONSConsumer"} or {@code "${name}ONSProducer"}.
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
     * the group id.
     * 
     * @return the group id
     */
    String getGroupId();

    /**
     * the MQ type.
     * 
     * @return the MQ type
     */
    MQType getMqType();

}
