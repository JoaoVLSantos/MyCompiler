package br.edu.ufape.ast.routines;

import br.edu.ufape.ast.Node;
import br.edu.ufape.ast.stmt.VarDeclNode;

public class ParameterNode extends Node {

    public final VarDeclNode.Type type;
    public final String name;

    public ParameterNode(
            int line,
            int column,
            VarDeclNode.Type type,
            String name
    ) {

        super(line, column);

        this.type = type;
        this.name = name;
    }

    @Override
    public String toString() {
    return type + " " + name;
    }    
}