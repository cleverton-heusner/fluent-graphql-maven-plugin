package org.cleverton.graphql;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import static java.io.File.separatorChar;

@Mojo(name = "graphql-to-bean", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class GraphQlToBeanMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    @Parameter(defaultValue = "${project.basedir}/src/main/resources/query.graphql", required = true)
    private File graphqlQueryFile;

    @Parameter(defaultValue = "${project.build.directory}/generated-sources", required = true)
    private File generatedSourcePath;

    @Override
    public void execute() {

        final String graphQlQuery = loadGraphQlQueryFromFile(graphqlQueryFile);
        final String graphQlSourcesPackageName = createGraphQlSourcesPackageName();
        final String formattedSourcesPackageName = formatSourcesPackageName(graphQlSourcesPackageName);
        final File graphQlSourcesPackage = new File(generatedSourcePath, formattedSourcesPackageName);
        final boolean isPackageCreated = graphQlSourcesPackage.mkdirs();

        if (isPackageCreated) {
            final var queriesGroupedByRootQuery = new GraphQlToBeanConverter().convert(
                    graphQlQuery,
                    graphQlSourcesPackageName
            );
            queriesGroupedByRootQuery.forEach((rootQueryName, queries) ->
                    queries.forEach((queryName, query) -> writeGraphQlQuerySource(
                            graphQlSourcesPackage,
                            queryName,
                            query)
                    )
            );
        }
    }

    private static String loadGraphQlQueryFromFile(final File graphqlQueryFile) {
        try {
            return Files.readString(graphqlQueryFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String createGraphQlSourcesPackageName() {
        return project.getGroupId() + ".graphql.queries";
    }

    private String formatSourcesPackageName(final String sourcesPackageName) {
        return sourcesPackageName.replace('.', separatorChar);
    }

    private void writeGraphQlQuerySource(final File graphQlSourcesPackage,
                                         final String queryName,
                                         final String query) {
        try (final var writer = new FileWriter(getSourceFullPath(graphQlSourcesPackage, queryName))) {
            writer.write(query);
        } catch (final IOException e) {
            throw new RuntimeException("Erro ao gravar o arquivo", e);
        }
    }

    private String getSourceFullPath(final File graphQlSourcesPackage,
                                     final String queryName) {
        return graphQlSourcesPackage + String.valueOf(separatorChar) + queryName + ".java";
    }
}