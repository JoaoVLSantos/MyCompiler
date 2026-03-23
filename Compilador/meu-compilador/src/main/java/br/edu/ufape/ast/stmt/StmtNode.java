package br.edu.ufape.ast.stmt;

import br.edu.ufape.ast.Node;

public abstract class StmtNode extends Node {

    protected StmtNode(int line, int column) {
        super(line, column);
    }
}