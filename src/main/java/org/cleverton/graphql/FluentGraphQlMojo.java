package org.cleverton.graphql;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import javax.inject.Inject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.io.File.separatorChar;

@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class FluentGraphQlMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    @Parameter(defaultValue = "${project.basedir}/src/main/resources/", required = true)
    private File graphqlQueryFile;

    @Parameter(defaultValue = "${project.build.directory}/generated-sources", required = true)
    private File generatedSourcePath;

    @Parameter(property = "graphQlFileName", required = true)
    private String graphQlFileName;

    @Inject
    private GraphQlToBeanConverter graphQlToBeanConverter;

    @Override
    public void execute() {

        final String graphQlQuery = loadGraphQlQueryFromFile(graphqlQueryFile);
        final String graphQlSourcesPackageName = createGraphQlSourcesPackageName();
        final String formattedSourcesPackageName = formatSourcesPackageName(graphQlSourcesPackageName);
        final File graphQlSourcesPackage = new File(generatedSourcePath, formattedSourcesPackageName);
        final boolean isPackageCreated = graphQlSourcesPackage.mkdirs();

        if (isPackageCreated) {
            final var queriesGroupedByRootQuery = graphQlToBeanConverter.convert(
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

    private String loadGraphQlQueryFromFile(final File graphqlQueryFile) {
        try {
            return Files.readString(Paths.get(graphqlQueryFile.getPath(), graphQlFileName));
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
        return graphQlSourcesPackage.getPath() + separatorChar + queryName + ".java";
    }
}