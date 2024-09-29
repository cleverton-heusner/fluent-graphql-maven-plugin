package org.cleverton.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.cleverton.utils.Capitalizer.capitalize;

public class Clazz {

    private String name;
    private String modifier;
    private final List<Field> fields;
    private final List<Method> methods;
    private Package pack;

    public Clazz() {
        fields = new ArrayList<>();
        methods = new ArrayList<>();
    }

    public static class Builder {

        private final Clazz clazz;

        public Builder() {
            this.clazz = new Clazz();
        }

        public Builder named(final String name) {
            clazz.name = name;
            return this;
        }

        public Builder addPublicModifier() {
            clazz.modifier = "public";
            return this;
        }

        public Clazz build() {
            return clazz;
        }

        public void addPackage(final Package pack) {
            this.clazz.pack = pack;
        }

        public void addField(final Field field) {
            clazz.fields.add(field);
        }

        public void addMethod(final Method method) {
            clazz.methods.add(method);
        }
    }

    public String render() {
        final StringBuilder classOutput = new StringBuilder(pack.render())
                .append("\n\n")
                .append(modifier)
                .append(" class ")
                .append(capitalize(name))
                .append(" {")
                .append("\n");

        return classOutput.append(renderFields())
                .append(renderMethods())
                .append("\n}")
                .toString();
    }

    private String renderFields() {
        return fields.stream()
                .map(Field::render)
                .collect(Collectors.joining());
    }

    private String renderMethods() {
        return methods.stream()
                .map(Method::render)
                .collect(Collectors.joining());
    }
}