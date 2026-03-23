package br.edu.ufape.ast.routines;

import java.util.List;

import br.edu.ufape.ast.Node;
import br.edu.ufape.ast.stmt.BlockNode;

public class ProcedureNode extends Node {

    public final String name;
    public final List<ParameterNode> params;
    public final BlockNode body;

    public ProcedureNode(
            int line,
            int column,
            String name,
            List<ParameterNode> params,
            BlockNode body
    ) {

        super(line, column);

        this.name = name;
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

    return "Procedure " + name +
           "(" + paramsStr + ")";
    }
}