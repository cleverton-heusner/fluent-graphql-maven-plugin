package org.cleverton.graphql;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class GraphQlToBeanConverterTestConfiguration {

    protected String loadFileContent(final String fileName) throws IOException {
        try (final InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName)) {
            assert is != null;
            return new String(is.readAllBytes(), StandardCharsets.UTF_8)
                    .replace("\r\n", "\n");
        }
    }
}
