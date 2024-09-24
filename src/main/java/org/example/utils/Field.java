package org.example.utils;

import static org.example.utils.Capitalizer.capitalize;

public class Field {

    private static final String INDENTATION = "    ";
    private static final String BLANK_SPACE = " ";
    private static final String STATEMENT_DELIMITER = ";";

    private String name;
    private String modifier;
    private String type;

    public static class Builder {
        private final Field method;

        public Builder() {
            this.method = new Field();
        }

        public Builder named(final String name) {
            method.name = name;
            return this;
        }

        public Builder addPrivateModifier() {
            method.modifier = "private";
            return this;
        }

        public Builder addPrivateFinalModifier() {
            method.modifier = "private final";
            return this;
        }

        public Builder withBooleanType() {
            method.type = "boolean";
            return this;
        }

        public Builder withType(final String type) {
            method.type = capitalize(type);
            return this;
        }

        public Field build() {
            return method;
        }
    }

    public String writeToConsole() {
        return new StringBuilder("\n")
                .append(INDENTATION)
                .append(modifier)
                .append(BLANK_SPACE)
                .append(type)
                .append(BLANK_SPACE)
                .append(name)
                .append(STATEMENT_DELIMITER)
                .toString();
    }
}