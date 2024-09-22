package org.example;

public class QueryNodeClass {

    public static final String ROOT_NODE_NAME = "";
    private static final String INDENTATION = "    ";

    private final StringBuilder value;

    public QueryNodeClass(final String nodeName) {
        value = new StringBuilder("public class ")
                .append(nodeName)
                .append(" {\n\n");
    }

    public void addSubTree(final String nodeName) {
        value.append(INDENTATION)
                .append("private ")
                .append(nodeName)
                .append(" ")
                .append(nodeName)
                .append(";\n");
    }

    public void addParentNode(final String parentNodeName) {
        if (Constructor.isNotRootNode(parentNodeName)) {
            addSubTree(parentNodeName);
        }
    }

    public void addLeafNode(final String leafNodeName) {
        value.append(INDENTATION)
                .append("private boolean ")
                .append(leafNodeName)
                .append(";\n");
    }

    public QueryNodeClass addConstructor(final Constructor constructor) {
        value.append(constructor.getValue());
        return this;
    }

    public void close() {
        value.append("\n}");
    }

    @Override
    public String toString() {
        return value.toString();
    }

    public static class Constructor {

        private final StringBuilder value;

        public StringBuilder getValue() {
            return value;
        }

        public Constructor(final String nodeName) {
            value = new StringBuilder("\n")
                    .append(INDENTATION)
                    .append("public ")
                    .append(nodeName)
                    .append("(");
        }

        public void initializeParentNode(final String nodeName) {
            if (isNotRootNode(nodeName)) {
                value.append("final ")
                        .append(nodeName)
                        .append(" ")
                        .append(nodeName);
            }

            value.append(") {\n");

            if (isNotRootNode(nodeName)) {
                value.append(INDENTATION + INDENTATION)
                        .append("this.")
                        .append(nodeName)
                        .append(" = ")
                        .append(nodeName)
                        .append(";\n");
            }
        }

        public static boolean isNotRootNode(final String nodeName) {
            return !ROOT_NODE_NAME.equals(nodeName);
        }

        public void instantiateNode(final String nodeName) {
            value.append(INDENTATION + INDENTATION)
                    .append(nodeName)
                    .append(" = new ")
                    .append(nodeName)
                    .append("(this);\n");
        }

        public void close() {
            value.append(INDENTATION)
                    .append("}");
        }

        @Override
        public String toString() {
            return value.toString();
        }
    }
}