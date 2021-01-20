package com.github.fmjsjx.myboot.autoconfigure.kafka;

/**
 * Enumerations for compression types.
 */
public enum CompressionType {

    /**
     * None.
     */
    NONE("none"),
    /**
     * GZIP.
     */
    GZIP("gzip"),
    /**
     * SNAPPY.
     */
    SNAPPY("snappy"),
    /**
     * LZ4.
     */
    LZ4("lz4"),
    /**
     * ZSTD.
     */
    ZSTD("zstd");

    private final String value;

    private CompressionType(String value) {
        this.value = value;
    }

    /**
     * Returns the value.
     * 
     * @return the value
     */
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name() + "(" + value + ")";
    }

}
