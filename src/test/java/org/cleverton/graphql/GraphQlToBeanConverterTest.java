package org.cleverton.graphql;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class GraphQlToBeanConverterTest extends GraphQlToBeanConverterTestConfiguration {

    private GraphQlToBeanConverter graphQlToBeanConverter;

    @BeforeEach
    void setUp() {
        graphQlToBeanConverter = new GraphQlToBeanConverter();
    }

    @Test
    void when_GraphQlQueryIsInformed_then_parsedToJavaClasses() throws IOException {

        // Arrange
        final String expectedGetPostQuery = loadFileContent("expected-get-post-query.txt");
        final String graphQlQuery = loadFileContent("test-query.graphql");
        final String graphQlSourcesPackageName = "org.cleverton.graphql.queries";

        // Act
        final var queries = graphQlToBeanConverter.convert(graphQlQuery, graphQlSourcesPackageName);

        // Assert
        final String actualGetPostQuery = queries.get("getPost").get("GetPostQuery");
        Assertions.assertThat(actualGetPostQuery).isEqualTo(expectedGetPostQuery);
    }
}