package br.edu.ufape.ast.expr;

import br.edu.ufape.ast.Node;

public abstract class ExprNode extends Node {

    protected ExprNode(int line, int column) {
        super(line, column);
    }
}