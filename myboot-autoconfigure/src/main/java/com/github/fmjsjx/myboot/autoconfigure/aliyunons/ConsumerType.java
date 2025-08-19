package com.github.fmjsjx.myboot.autoconfigure.aliyunons;

/**
 * Enumerations of consumer types.
 *
 * @deprecated Deprecated since 3.6 and may be removed in future version.
 */
@Deprecated(since = "3.6", forRemoval = true)
public enum ConsumerType {

    /**
     * Normal consumer.
     */
    NORMAL,
    /**
     * Batch consumer.
     */
    BATCH,
    /**
     * Ordered consumer.
     */
    ORDERED,
    /**
     * Pull consumer.
     */
    PULL

}
