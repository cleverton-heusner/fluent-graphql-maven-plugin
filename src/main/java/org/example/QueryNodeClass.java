package org.example;

import org.example.utils.Field;
import org.example.utils.Method;

import java.util.List;

import static org.example.utils.Capitalizer.capitalize;

public class QueryNodeClass {

    public static final String ROOT_NODE_NAME = "";
    private static final String SELECT_ALL_FIELDS = "selectAllFields";
    private static final String SELECT_ALL_FIELDS_WITH_PARAMS = SELECT_ALL_FIELDS + "()";

    private String nodeName;
    private final StringBuilder value;

    public String getNodeName() {
        return nodeName;
    }

    public QueryNodeClass(final String nodeName, final String parentNodeName) {
        this.nodeName = nodeName;

        if (isRootNode(parentNodeName)) {
            this.nodeName = this.nodeName + "Query";
        }

        this.nodeName = capitalize(this.nodeName);
        value = new StringBuilder("public class ")
                .append(this.nodeName)
                .append(" {\n");
    }

    public void addSubTree(final String nodeName) {
        value.append(new Field.Builder()
                .addPrivateFinalModifier()
                .withType(nodeName)
                .named(nodeName)
                .build()
                .writeToConsole()
        );
    }

    public void addParentNode(final String parentNodeName) {
        if (!isRootNode(parentNodeName)) {
            addSubTree(parentNodeName);
        }
    }

    private boolean isRootNode(final String nodeName) {
        return nodeName.isEmpty();
    }

    public void addLeafNode(final String leafNodeName) {
        value.append(new Field.Builder()
                .addPrivateModifier()
                .withBooleanType()
                .named(leafNodeName)
                .build()
                .writeToConsole()
        );
    }

    public QueryNodeClass addLeafNodeSelectorMethods(final List<String> leafNodeNames) {
        leafNodeNames.forEach(leafNodeName ->
            value.append(new Method.Builder()
                    .addPublicModifier()
                    .returns(nodeName)
                    .named("select" + capitalize(leafNodeName))
                    .noParams()
                    .addStatement(leafNodeName, "true")
                    .addStatementReturning("this")
                    .build()
                    .writeToConsole()
            )
        );

        return this;
    }

    public QueryNodeClass addLeafNodeSkipperMethods(final List<String> leafNodeNames) {
        leafNodeNames.forEach(leafNodeName ->
                value.append(new Method.Builder()
                        .addPublicModifier()
                        .returns(nodeName)
                        .named("skip" + capitalize(leafNodeName))
                        .noParams()
                        .addStatement(leafNodeName, "false")
                        .addStatementReturning("this")
                        .build()
                        .writeToConsole()
                )
        );

        return this;
    }

    public QueryNodeClass addAllNodesSelectorMethod(final List<String> leafNodeNames,
                                                    final List<String> subTreesNames) {
        final var builder = new Method.Builder()
                .addPublicModifier()
                .returns(nodeName)
                .named(SELECT_ALL_FIELDS)
                .noParams();

        leafNodeNames.forEach(nodeName -> builder.addStatement(nodeName, "true"));
        subTreesNames.forEach(subTreeName -> builder.addStatement(
                subTreeName +
                "." +
                SELECT_ALL_FIELDS_WITH_PARAMS)
        );

        value.append(builder.addStatementReturning("this")
                .build()
                .writeToConsole()
        );

        return this;
    }

    public QueryNodeClass addSubTreeSelectorMethod(final List<String> subTreesNames) {
        subTreesNames.forEach(subTreesName ->
            value.append(new Method.Builder()
                    .addPublicModifier()
                    .returns(subTreesName)
                    .named("from" + capitalize(subTreesName))
                    .addStatementReturning(subTreesName)
                    .build()
                    .writeToConsole()
            )
        );

        return this;
    }

    public QueryNodeClass addConstructor(final Method constructor) {
        value.append(constructor.writeToConsole());
        return this;
    }

    public void close() {
        value.append("\n}");
    }

    @Override
    public String toString() {
        return value.toString();
    }
}