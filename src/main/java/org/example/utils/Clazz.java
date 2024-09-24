package org.example.utils;

import static org.example.utils.Capitalizer.capitalize;

public class Clazz {

    private static final String BLANK_SPACE = " ";

    private String name;
    private String modifier;

    public static class Builder {
        private final Clazz method;

        public Builder() {
            this.method = new Clazz();
        }

        public Builder named(final String name) {
            method.name = name;
            return this;
        }

        public Builder addPublicModifier() {
            method.modifier = "public";
            return this;
        }

        public Clazz build() {
            return method;
        }
    }

    public String writeToConsole() {
        return new StringBuilder()
                .append(modifier)
                .append(BLANK_SPACE)
                .append("class")
                .append(BLANK_SPACE)
                .append(capitalize(name))
                .toString();
    }
}