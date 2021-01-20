package com.github.fmjsjx.myboot.autoconfigure.kafka;

/**
 * Enumerations for auto-offset reset.
 */
public enum AutoOffsetReset {

    /**
     * Latest.
     */
    LATEST("latest"),
    /**
     * Earliest.
     */
    EARLIEST("earliest"),
    /**
     * None.
     */
    NONE("none");

    private final String value;

    private AutoOffsetReset(String value) {
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
