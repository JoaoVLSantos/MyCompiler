package br.edu.ufape.semantic;

import java.util.*;

public class SymbolTable {

    private final Deque<Map<String, Symbol>> scopes = new ArrayDeque<>();

    private final List<Map<String, Symbol>> allScopes = new ArrayList<>();

    public void enterScope() {

        Map<String, Symbol> scope = new LinkedHashMap<>();

        scopes.push(scope);

        allScopes.add(scope);
    }

    public void exitScope() {

        if (scopes.isEmpty()) {
            throw new RuntimeException("Erro interno: tentativa de sair de escopo inexistente");
        }

        scopes.pop();
    }

    public void declare(Symbol symbol) {

        Map<String, Symbol> current = scopes.peek();

        if (current == null) {
            throw new RuntimeException("Erro interno: nenhum escopo ativo");
        }

        if (current.containsKey(symbol.getName())) {
            throw new RuntimeException(
                    "Erro semântico: símbolo já declarado no escopo: "
                            + symbol.getName()
            );
        }

        current.put(symbol.getName(), symbol);
    }

    public Symbol lookup(String name) {

        for (Map<String, Symbol> scope : scopes) {

            Symbol symbol = scope.get(name);

            if (symbol != null) {
                return symbol;
            }
        }

        throw new RuntimeException(
                "Erro semântico: símbolo não declarado: " + name
        );
    }

    public boolean exists(String name) {

        for (Map<String, Symbol> scope : scopes) {

            if (scope.containsKey(name)) {
                return true;
            }
        }

        return false;
    }


    public boolean existsInCurrentScope(String name) {

        Map<String, Symbol> current = scopes.peek();

        return current != null && current.containsKey(name);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        int level = 1;

        for (Map<String, Symbol> scope : allScopes) {

            if (scope.isEmpty()) {
                continue;
            }

            sb.append("Escopo ").append(level++).append("\n");

            for (Symbol s : scope.values()) {

                sb.append("  ")
                  .append(s.getName())
                  .append(" : ")
                  .append(s.getType());

                if (s instanceof FunctionSymbol fn) {
                    sb.append(" routine=").append(fn.getRoutine()).append(" params=").append(fn.getParams());
                }

                sb.append("\n");
            }

            sb.append("\n");
        }

        return sb.toString();
    }
}