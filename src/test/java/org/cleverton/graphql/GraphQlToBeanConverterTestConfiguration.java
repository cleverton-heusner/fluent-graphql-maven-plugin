package org.cleverton.graphql;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class GraphQlToBeanConverterTestConfiguration {

    protected static final String EXPECTED_GET_POST_QUERY_FILE = "expected-get-post-query";
    protected static final String EXPECTED_AUTHOR_QUERY_FILE = "expected-author-query";
    protected static final String EXPECTED_COMMENTS_QUERY_FILE = "expected-comments-query";
    protected static final String EXPECTED_FRIENDS_QUERY_FILE = "expected-friends-query";
    protected static final String EXPECTED_MUTUAL_FRIENDS_QUERY_FILE = "expected-mutual-friends-query";
    protected static final String EXPECTED_POPULARITY_QUERY_FILE = "expected-popularity-query";

    protected String loadFileContent(final String fileName) throws IOException {
        try (final InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName)) {
            assert is != null;
            return new String(is.readAllBytes(), StandardCharsets.UTF_8)
                    .replace("\r\n", "\n");
        }
    }
}
