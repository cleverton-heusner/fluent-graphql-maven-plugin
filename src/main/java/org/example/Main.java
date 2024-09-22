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
                .filter(field -> field instanceof Field)
                .filter(fieldWithChildren -> ((Field) fieldWithChildren).getSelectionSet() != null)
                .findFirst()
                .map(rootField -> (Field) rootField)
                .ifPresent(Main::convertQueryToBean);
    }

    private static void convertQueryToBean(final Field field) {
        String childFieldWithDescendantsName = "";
        final StringBuilder clazz = new StringBuilder("public class ");
        clazz.append(field.getName())
                .append(" {\n");

        for (final Selection<?> selection : field.getSelectionSet().getSelections()) {
            if (selection instanceof final Field f) {
                if (f.getSelectionSet() != null) {
                    childFieldWithDescendantsName = f.getName();
                    convertQueryToBean(f);
                    clazz.append("private ")
                            .append(childFieldWithDescendantsName)
                            .append(" ")
                            .append(childFieldWithDescendantsName)
                            .append(";\n");
                }
                else {
                    clazz.append("private boolean ")
                            .append(f.getName())
                            .append(";\n");
                }
            }
        }

        clazz.append("}");

        System.out.println(clazz);
    }
}