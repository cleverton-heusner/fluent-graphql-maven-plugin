package org.cleverton.graphql;

public enum BooleanString {

    TRUE("true"),
    FALSE("false");

    private final String value;

    BooleanString(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
