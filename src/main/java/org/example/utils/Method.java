package org.example.utils;

import static org.example.utils.Capitalizer.capitalize;

public class Method {

    private String name;
    private String modifier;
    private String returns;
    private String params;
    private final StringBuilder statements;

    private Method() {
        returns = "";
        params = "";
        statements = new StringBuilder();
    }

    public static class Builder {
        private final Method method;

        public Builder() {
            this.method = new Method();
        }

        public Builder named(final String name) {
            method.name = name;
            return this;
        }

        public Builder noParams() {
            return this;
        }

        public Builder returns(final String returnName) {
            method.returns = capitalize(returnName);
            return this;
        }

        public Builder returns(final Class<?> clazz) {
            method.returns = clazz.getSimpleName();
            return this;
        }

        public Builder addStatement(final String statement) {
            method.statements.append("\n")
                    .append("        ")
                    .append(statement)
                    .append(";");
            return this;
        }

        public Builder addStatement(final String var, final String val) {
            method.statements.append("\n")
                    .append("        ")
                    .append(var)
                    .append(" = ")
                    .append(val)
                    .append(";");
            return this;
        }

        public Builder addStatementReturning(final String statement) {
            method.statements.append("\n")
                    .append("        ")
                    .append("return ")
                    .append(statement)
                    .append(";");
            return this;
        }

        public Builder addPublicModifier() {
            method.modifier = "    public";
            return this;
        }

        public Method build() {
            return method;
        }

        public Builder withParams(final String params) {
            if (!params.isEmpty()) {
                method.params = "final " + capitalize(params) + " " + params;
            }

            return this;
        }
    }

    public String writeToConsole() {
        return new StringBuilder("\n\n").append(modifier)
                .append(returns.isEmpty() ? "" : " ")
                .append(returns)
                .append(" ")
                .append(name)
                .append("(")
                .append(params)
                .append(")")
                .append(" {")
                .append(statements)
                .append("\n")
                .append("    ")
                .append("}")
                .toString();
    }
}