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
    SYNC(false),
    /**
     * Mode for Synchronous Connection Pooling.
     * <p>
     * Using {@link SoftReferenceObjectPool}.
     */
    SYNC_SOFT_REFERENCE(false),
    /**
     * Mode for Asynchronous Connection Pooling.
     */
    ASYNC(true),
    /**
     * Mode for Asynchronous Connection Pooling.
     * <p>
     * Using {@link AsyncPoolPlus}.
     */
    ASYNC_PLUS(true),;

    private final boolean async;

    private RedisPoolMode(boolean async) {
        this.async = async;
    }

    boolean isAsync() {
        return async;
    }

}
