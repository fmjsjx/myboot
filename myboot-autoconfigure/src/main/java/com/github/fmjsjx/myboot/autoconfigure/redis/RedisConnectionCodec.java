package com.github.fmjsjx.myboot.autoconfigure.redis;

/**
 * Enumeration for {@code REDIS/Lettuce} connection encode/decode modes.
 */
public enum RedisConnectionCodec {

    /**
     * String on {@code UTF-8}.
     */
    UTF8,
    /**
     * String on {@code US-ASCII}.
     */
    ASCII,
    /**
     * Byte array.
     */
    BYTE_ARRAY

}
