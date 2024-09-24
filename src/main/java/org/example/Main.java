package org.example;

import graphql.language.Document;
import graphql.language.Field;
import graphql.language.OperationDefinition;
import graphql.language.Selection;
import graphql.parser.Parser;
import org.example.utils.Method;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.example.utils.Capitalizer.capitalize;

public class Main {

    public static void main(String[] args) {
        String query = loadGraphQlQueryFile();
        Document document = new Parser().parseDocument(query);
        document.getDefinitionsOfType(OperationDefinition.class)
                .forEach(Main::iterateQuery);
    }

    private static String loadGraphQlQueryFile() {
        try (InputStream inputStream = Main.class.getClassLoader()
                .getResourceAsStream("query.graphql")) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Arquivo não encontrado!");
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    private static void iterateQuery(final OperationDefinition queryDefinition) {
        queryDefinition.getSelectionSet()
                .getSelections()
                .stream()
                .filter(node -> node instanceof Field)
                .filter(n -> isSubTree((Field) n))
                .findFirst()
                .map(rootNode -> (Field) rootNode)
                .ifPresent(node -> generateQueryNodeClass(
                        node,
                        QueryNodeClass.ROOT_NODE_NAME)
                );
    }

    private static void generateQueryNodeClass(final Field node,
                                               final String parentNodeName) {

        final List<String> leafNodesNames = new ArrayList<>();
        final List<String> subTreesNames = new ArrayList<>();
        final var queryNodeClass = new QueryNodeClass(node.getName(), parentNodeName);

        final var queryNodeClassConstructorBuilder = new Method.Builder()
                .addPublicModifier()
                .withParams(parentNodeName)
                .named(queryNodeClass.getNodeName());

        if (!parentNodeName.isEmpty()) {
            queryNodeClassConstructorBuilder.addStatement("this." + parentNodeName, parentNodeName);
        }

        for (final Selection<?> selection : node.getSelectionSet().getSelections()) {
            if (selection instanceof final Field n) {
                if (isSubTree(n)) {
                    generateQueryNodeClass(n, node.getName());

                    queryNodeClass.addSubTree(n.getName());
                    subTreesNames.add(n.getName());
                    queryNodeClass.addParentNode(parentNodeName);
                    queryNodeClassConstructorBuilder.addStatement(n.getName() +
                            " = new " +
                            capitalize(n.getName()) +
                            "(this)");
                }
                else {
                    queryNodeClass.addLeafNode(n.getName());
                    leafNodesNames.add(n.getName());
                }
            }
        }

        queryNodeClass.addConstructor(queryNodeClassConstructorBuilder.build())
                .addLeafNodeSelectorMethods(leafNodesNames)
                .addLeafNodeSkipperMethods(leafNodesNames)
                .addAllNodesSelectorMethod(leafNodesNames, subTreesNames)
                .addSubTreeSelectorMethod(subTreesNames)
                .close();

        System.out.println(queryNodeClass);
    }

    private static boolean isSubTree(final Field node) {
        return node.getSelectionSet() != null;
    }
}