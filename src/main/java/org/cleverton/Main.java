package org.cleverton;

import org.cleverton.graphql.GraphQlToBeanConverter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Map;

import static java.io.File.separatorChar;

public class Main {

    public static void main(String[] args) {
        final String query = loadGraphQlQueryFromFile();
        final String graphQlSourcesPackageName = "org.cleverton.graphql.queries";
        final String formattedSourcesPackageName = formatSourcesPackageName(graphQlSourcesPackageName);
        final File graphQlSourcesPackage = new File(getGeneratedSourcePath(), formattedSourcesPackageName);
        final boolean isPackageCreated = graphQlSourcesPackage.mkdirs();

        if (isPackageCreated) {
            final Map<String, Map<String, String>> queries = new GraphQlToBeanConverter().convert(
                    query,
                    graphQlSourcesPackageName
            );
            queries.forEach((className, queryList) ->
                    queryList.forEach((sourceFileName, sourceFile) -> {
                        try (final var writer = new FileWriter(getFullyQualifiedNameOfGraphQlSource(
                                graphQlSourcesPackage,
                                sourceFileName))
                        ) {
                            writer.write(sourceFile);
                        } catch (IOException e) {
                            throw new RuntimeException("Erro ao gravar o arquivo", e);
                        }
                    })
            );
        }
    }

    private static String formatSourcesPackageName(final String sourcesPackageName) {
        return sourcesPackageName.replace('.', separatorChar);
    }

    private static String getFullyQualifiedNameOfGraphQlSource(final File graphQlSourcesPackage,
                                                               final String sourceFileName) {
        return graphQlSourcesPackage + String.valueOf(separatorChar) + sourceFileName + ".java";
    }

    private static File getGeneratedSourcePath() {
        return Paths.get("target", "generated-sources").toFile();
    }

    private static String loadGraphQlQueryFromFile() {
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
}