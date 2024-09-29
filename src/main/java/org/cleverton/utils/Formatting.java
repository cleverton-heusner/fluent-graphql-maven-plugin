package org.cleverton.utils;

public enum Formatting {

    INDENTATION("    "),
    DOUBLE_INDENTATION("        ");

    private final String value;

    Formatting(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
