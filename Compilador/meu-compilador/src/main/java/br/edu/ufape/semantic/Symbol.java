package br.edu.ufape.semantic;

public class Symbol {

    protected final String name;
    protected final Type type;

    public Symbol(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public boolean isFunction() {
        return false;
    }

    public boolean isProcedure() {
        return false;
    }

    @Override
    public String toString() {
        return name + " : " + type;
    }
}