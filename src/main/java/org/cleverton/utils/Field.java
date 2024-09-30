package org.cleverton.utils;

import static org.cleverton.utils.Capitalizer.capitalize;

public class Field {

    private static final String INDENTATION = "    ";
    private static final String BLANK_SPACE = " ";
    private static final String STATEMENT_DELIMITER = ";";

    private String name;
    private String modifier;
    private String type;

    public static class Builder {
        private final Field field;

        public Builder() {
            this.field = new Field();
        }

        public Builder withName(final String name) {
            field.name = name;
            return this;
        }

        public Builder withPrivateModifier() {
            field.modifier = "private";
            return this;
        }

        public Builder withPrivateFinalModifier() {
            field.modifier = "private final";
            return this;
        }

        public Builder withBooleanType() {
            field.type = "boolean";
            return this;
        }

        public Builder withType(final String type) {
            field.type = capitalize(type);
            return this;
        }

        public Field build() {
            return field;
        }
    }

    public String getName() {
        return name;
    }

    public String render() {
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