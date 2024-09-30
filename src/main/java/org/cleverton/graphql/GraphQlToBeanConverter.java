package org.cleverton.graphql;

import graphql.language.Field;
import graphql.language.OperationDefinition;
import graphql.parser.Parser;

import java.util.HashMap;
import java.util.Map;

import static org.cleverton.graphql.GraphQlNode.ROOT_NODE_NAME;
import static org.cleverton.utils.Capitalizer.capitalize;

public class GraphQlToBeanConverter {

    private String groupId;
    private final Map<String, Map<String, String>> graphQlNodesClasses;
    private String currentRootNodeName;

    public GraphQlToBeanConverter() {
        graphQlNodesClasses = new HashMap<>();
    }

    public Map<String, Map<String, String>> convert(final String graphQlQuery, final String groupId) {
        this.groupId = groupId;

        new Parser().parseDocument(graphQlQuery)
                .getDefinitionsOfType(OperationDefinition.class)
                .forEach(queryDefinition -> queryDefinition.getSelectionSet()
                        .getSelectionsOfType(Field.class)
                        .stream()
                        .findFirst()
                        .ifPresent(node -> {
                            currentRootNodeName = node.getName();
                            graphQlNodesClasses.put(currentRootNodeName, new HashMap<>());
                            convertGraphQlQueryToBeans(node, ROOT_NODE_NAME);
                        })
                );

        return graphQlNodesClasses;
    }

    private void convertGraphQlQueryToBeans(final Field node,
                                            final String parentNodeName) {
        final var graphQlNode = new GraphQlNode(node.getName(), parentNodeName, groupId, currentRootNodeName)
                .addParentNode();

        for (final var selection : node.getSelectionSet().getSelections()) {
            if (selection instanceof final Field graphQlQueryNode) {
                if (nodeHasChildren(graphQlQueryNode)) {
                    convertGraphQlQueryToBeans(graphQlQueryNode, node.getName());

                    graphQlNode.addSubTree(graphQlQueryNode.getName());
                    graphQlNode.getConstructorBuilder()
                            .addStatement(graphQlQueryNode.getName() +
                                    " = new " +
                                    capitalize(graphQlQueryNode.getName()) +
                                    "(this)"
                            );
                }
                else {
                    graphQlNode.addLeafNode(graphQlQueryNode.getName());
                }
            }
        }

        final String renderedGraphQlQuery = renderGraphQlQuery(graphQlNode);
        graphQlNodesClasses.get(currentRootNodeName).put(
                graphQlNode.getNodeName(), renderedGraphQlQuery
        );
    }

    private boolean nodeHasChildren(final Field node) {
        return !node.getChildren().isEmpty();
    }

    private String renderGraphQlQuery(final GraphQlNode graphQlNode) {
        return graphQlNode.addConstructor()
                .addLeafNodeSelectorMethods()
                .addLeafNodeSkipperMethods()
                .addAllNodesSelectorMethod()
                .addSubTreeSelectorMethod()
                .addNodeSelectionEndMethod()
                .render();
    }
}