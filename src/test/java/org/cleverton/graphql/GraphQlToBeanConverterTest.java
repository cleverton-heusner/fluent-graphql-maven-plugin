package org.cleverton.graphql;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class GraphQlToBeanConverterTest extends GraphQlToBeanConverterTestConfiguration {

    private GraphQlToBeanConverter graphQlToBeanConverter;

    @BeforeEach
    void setUp() {
        graphQlToBeanConverter = new GraphQlToBeanConverter();
    }

    @Test
    void when_GraphQlQueryIsInformed_then_parsedToJavaClasses() throws IOException {

        // Arrange
        final String expectedGetPostQuery = loadFileContent(EXPECTED_GET_POST_QUERY_FILE);
        final String expectedAuthorQuery = loadFileContent(EXPECTED_AUTHOR_QUERY_FILE);
        final String expectedCommentsQuery = loadFileContent(EXPECTED_COMMENTS_QUERY_FILE);
        final String expectedFriendsQuery = loadFileContent(EXPECTED_FRIENDS_QUERY_FILE);
        final String expectedMutualFriendsQuery = loadFileContent(EXPECTED_MUTUAL_FRIENDS_QUERY_FILE);
        final String expectedPopularityQuery = loadFileContent(EXPECTED_POPULARITY_QUERY_FILE);
        final String graphQlQuery = loadFileContent("test-query.graphql");
        final String graphQlSourcesPackageName = "org.cleverton.graphql.queries";

        // Act
        final var queries = graphQlToBeanConverter.convert(graphQlQuery, graphQlSourcesPackageName);

        // Assert
        final var rootQuery = queries.get("getPost");
        final String actualGetPostQuery = rootQuery.get("GetPostQuery");
        final String actualAuthorQuery = rootQuery.get("Author");
        final String actualCommentsQuery = rootQuery.get("Comments");
        final String actualFriendsQuery = rootQuery.get("Friends");
        final String actualMutualFriendsQuery = rootQuery.get("MutualFriends");
        final String actualPopularityQuery = rootQuery.get("Popularity");

        assertThat(actualGetPostQuery).isEqualTo(expectedGetPostQuery);
        assertThat(actualAuthorQuery).isEqualTo(expectedAuthorQuery);
        assertThat(actualCommentsQuery).isEqualTo(expectedCommentsQuery);
        assertThat(actualFriendsQuery).isEqualTo(expectedFriendsQuery);
        assertThat(actualMutualFriendsQuery).isEqualTo(expectedMutualFriendsQuery);
        assertThat(actualPopularityQuery).isEqualTo(expectedPopularityQuery);
    }
}