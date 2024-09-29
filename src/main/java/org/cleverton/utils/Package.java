package org.cleverton.utils;

public class Package {

    private static final String BLANK_SPACE = " ";

    private String name;

    public static class Builder {
        private final Package pack;

        public Builder() {
            this.pack = new Package();
        }

        public Builder withName(final String name) {
            pack.name = name;
            return this;
        }

        public Package build() {
            return pack;
        }
    }

    public String render() {
        return new StringBuilder("package")
                .append(BLANK_SPACE)
                .append(name)
                .append(";")
                .toString();
    }
}