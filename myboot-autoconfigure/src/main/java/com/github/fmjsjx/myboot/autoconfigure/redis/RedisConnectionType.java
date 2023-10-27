package com.github.fmjsjx.myboot.autoconfigure.redis;

/**
 * Enumeration for {@code REDIS/Lettuce} connection types.
 */
public enum RedisConnectionType {

    /**
     * Normal connection.
     */
    NORMAL,
    /**
     * PUBSUB connection.
     */
    PUBSUB,
    /**
     * SENTINEL connection.
     */
    SENTINEL,
    /**
     * MasterReplica connection.
     */
    MASTER_REPLICA,

}
