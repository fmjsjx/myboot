package com.github.fmjsjx.myboot.autoconfigure.redis;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.SoftReferenceObjectPool;

/**
 * Enumeration for {@code REDIS/Lettuce} pool modes.
 */
public enum RedisPoolMode {

    /**
     * Mode for Synchronous Connection Pooling.
     * <p>
     * Using {@link GenericObjectPool}.
     */
    SYNC,
    /**
     * Mode for Synchronous Connection Pooling.
     * <p>
     * Using {@link SoftReferenceObjectPool}.
     */
    SYNC_SOFT_REFERENCE,
    /**
     * Mode for Asynchronous Connection Pooling.
     */
    ASYNC

}
