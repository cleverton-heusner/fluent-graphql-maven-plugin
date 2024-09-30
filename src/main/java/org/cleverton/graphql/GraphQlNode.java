package org.cleverton.graphql;

import org.cleverton.utils.Clazz;
import org.cleverton.utils.Field;
import org.cleverton.utils.Method;
import org.cleverton.utils.Package;

import java.util.ArrayList;
import java.util.List;

import static org.cleverton.utils.Capitalizer.capitalize;

public class GraphQlNode {

    public static final String ROOT_NODE_NAME = "";
    private static final String THIS = "this";
    private static final String TRUE = "true";
    private static final String QUERY_SUFFIX = "Query";
    private static final String SELECT_ALL_FIELDS = "selectAllFields";
    private static final String SELECT_ALL_FIELDS_WITH_PARAMS = SELECT_ALL_FIELDS + "()";

    private String nodeName;
    private String parentNodeName;
    private final String packageName;
    private final String rootNodeName;
    private final List<String> leafNodeNames;
    private final List<String> subTreesNames;
    private final Clazz.Builder classBuilder;
    private final Method.Builder constructorBuilder;

    public GraphQlNode(final String nodeName,
                       final String parentNodeName,
                       final String packageName,
                       final String rootNodeName) {

        this.nodeName = nodeName;
        this.parentNodeName = parentNodeName;
        this.packageName = packageName;
        this.rootNodeName = rootNodeName;
        this.leafNodeNames = new ArrayList<>();
        this.subTreesNames = new ArrayList<>();

        addQuerySuffixToParentNode();
        this.nodeName = capitalize(this.nodeName);
        classBuilder = createClassBuilder();
        classBuilder.addPackage(createPackage());
        constructorBuilder = createConstructorBuilder();

        if (hasParentNode()) {
            constructorBuilder.withStatement(
                    THIS + "." + this.parentNodeName,
                    this.parentNodeName
            );
        }
    }

    private void addQuerySuffixToParentNode() {
        if (nodeName.equals(rootNodeName)) {
            nodeName = nodeName + QUERY_SUFFIX;
        }
        else if (parentNodeName.equals(rootNodeName)) {
            parentNodeName = parentNodeName + QUERY_SUFFIX;
        }
    }

    private Clazz.Builder createClassBuilder() {
        return new Clazz.Builder().addPublicModifier().named(this.nodeName);
    }

    private Package createPackage() {
        return new Package.Builder().withName(packageName).build();
    }

    private Method.Builder createConstructorBuilder() {
        return new Method.Builder()
                .withPublicModifier()
                .withParams(this.parentNodeName)
                .withName(this.nodeName);
    }

    public Method.Builder getConstructorBuilder() {
        return constructorBuilder;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void addSubTree(final String subTreeName) {
        addNodeWithDescendants(subTreeName);
        subTreesNames.add(subTreeName);
    }

    public GraphQlNode addParentNode() {
        if (hasParentNode()) {
            addNodeWithDescendants(parentNodeName);
        }

        return this;
    }

    private void addNodeWithDescendants(final String nodeName) {
        classBuilder.addField(new Field.Builder().withPrivateFinalModifier()
                .withType(nodeName)
                .withName(nodeName)
                .build()
        );
    }

    public boolean hasParentNode() {
        return !this.parentNodeName.isEmpty();
    }

    public void addLeafNode(final String leafNodeName) {
        classBuilder.addField(new Field.Builder().withPrivateModifier()
                .withBooleanType()
                .withName(leafNodeName)
                .build());
        leafNodeNames.add(leafNodeName);
    }

    public GraphQlNode addLeafNodeSelectorMethods() {
        leafNodeNames.forEach(leafNodeName -> classBuilder.addMethod(
                new Method.Builder().withPublicModifier()
                        .withReturn(nodeName)
                        .withName("select" + capitalize(leafNodeName))
                        .withoutParams()
                        .withStatement(leafNodeName, TRUE)
                        .withStatementReturning(THIS)
                        .build()
                )
        );

        return this;
    }

    public GraphQlNode addLeafNodeSkipperMethods() {
        leafNodeNames.forEach(leafNodeName -> classBuilder.addMethod(new Method.Builder()
                        .withPublicModifier()
                        .withReturn(nodeName)
                        .withName("skip" + capitalize(leafNodeName))
                        .withoutParams()
                        .withStatement(leafNodeName, "false")
                        .withStatementReturning(THIS)
                        .build()
                )
        );

        return this;
    }

    public GraphQlNode addAllNodesSelectorMethod() {
        final var builder = new Method.Builder()
                .withPublicModifier()
                .withReturn(nodeName)
                .withName(SELECT_ALL_FIELDS)
                .withoutParams();

        leafNodeNames.forEach(nodeName -> builder.withStatement(nodeName, TRUE));
        subTreesNames.forEach(subTreeName -> builder.withStatement(
                subTreeName +
                "." +
                SELECT_ALL_FIELDS_WITH_PARAMS)
        );

        classBuilder.addMethod(builder.withStatementReturning(THIS)
                .build()
        );

        return this;
    }

    public GraphQlNode addSubTreeSelectorMethod() {
        subTreesNames.forEach(subTreesName ->
                        classBuilder.addMethod(new Method.Builder()
                                .withPublicModifier()
                                .withReturn(subTreesName)
                                .withName("from" + capitalize(subTreesName))
                                .withStatementReturning(subTreesName)
                                .build()
                        )
        );

        return this;
    }

    public GraphQlNode addConstructor() {
        classBuilder.addMethod(constructorBuilder.build());
        return this;
    }

    public GraphQlNode addNodeSelectionEndMethod() {
        final var builder = new Method.Builder().withPublicModifier();

        if (hasParentNode()) {
            builder.withReturn(parentNodeName)
                    .withName("end" + capitalize(nodeName) + "Selection")
                    .withStatementReturning(parentNodeName);
        }
        else {
            builder.withReturn(nodeName)
                    .withName("endSelection")
                    .withStatementReturning(THIS);
        }

        classBuilder.addMethod(builder.build());
        return this;
    }

    public String render() {
        return classBuilder.build()
                .render();
    }
}