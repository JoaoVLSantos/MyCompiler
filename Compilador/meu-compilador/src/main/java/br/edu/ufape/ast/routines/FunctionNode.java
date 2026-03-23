package br.edu.ufape.ast.routines;

import java.util.List;

import br.edu.ufape.ast.Node;
import br.edu.ufape.ast.stmt.BlockNode;
import br.edu.ufape.ast.stmt.VarDeclNode;

public class FunctionNode extends Node {

    public final String name;
    public final VarDeclNode.Type returnType;
    public final List<ParameterNode> params;
    public final BlockNode body;

    public FunctionNode(
            int line,
            int column,
            String name,
            VarDeclNode.Type returnType,
            List<ParameterNode> params,
            BlockNode body
    ) {

        super(line, column);

        this.name = name;
        this.returnType = returnType;
        this.params = params;
        this.body = body;
    }
    
    @Override
    public String toString() {

    StringBuilder paramsStr = new StringBuilder();

    for (ParameterNode p : params) {
        paramsStr.append(p).append(", ");
    }

    if (!params.isEmpty()) {
        paramsStr.setLength(paramsStr.length() - 2);
    }

    return "Function " + name +
           "(" + paramsStr + ")" +
           " : " + returnType;
    }
}