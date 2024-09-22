package org.example;

import graphql.language.Document;
import graphql.language.Field;
import graphql.language.OperationDefinition;
import graphql.language.Selection;
import graphql.parser.Parser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

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

        final var queryNodeClass = new QueryNodeClass(node.getName());
        final var queryNodeClassConstructor = new QueryNodeClass.Constructor(node.getName());
        queryNodeClassConstructor.initializeParentNode(parentNodeName);

        for (final Selection<?> selection : node.getSelectionSet().getSelections()) {
            if (selection instanceof final Field n) {
                if (isSubTree(n)) {
                    generateQueryNodeClass(n, node.getName());

                    queryNodeClass.addSubTree(n.getName());
                    queryNodeClass.addParentNode(parentNodeName);
                    queryNodeClassConstructor.instantiateNode(n.getName());
                }
                else {
                    queryNodeClass.addLeafNode(n.getName());
                }
            }
        }

        queryNodeClassConstructor.close();
        queryNodeClass.addConstructor(queryNodeClassConstructor)
                .close();

        System.out.println(queryNodeClass);
    }

    private static boolean isSubTree(final Field node) {
        return node.getSelectionSet() != null;
    }
}