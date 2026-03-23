package br.edu.ufape.semantic;

import java.util.List;

public class FunctionSymbol extends Symbol {

    private final List<Type> params;
    private String routine;

    public FunctionSymbol(String name, Type returnType, List<Type> params, String routine ) {

        super(name, returnType);
        this.params = params;
        this.routine = routine;
    }

    public List<Type> getParams() {
        return params;
    }

    public String getRoutine() {
        return routine;
    }

    @Override
    public boolean isFunction() {
        return type != Type.VOID;

    }
    
    @Override
    public boolean isProcedure() {
        return type == Type.VOID;
    }

    @Override
    public String toString() {

        return name +
                "(" + params + ")" +
                " : " + type;
    }
}