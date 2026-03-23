package br.edu.ufape.ast.stmt;

import java.util.List;

import br.edu.ufape.ast.expr.ExprNode;

public class CallStmtNode extends StmtNode {

    public final String name;
    public final List<ExprNode> args;

    public CallStmtNode(int line, int column,
                        String name,
                        List<ExprNode> args) {

        super(line, column);

        this.name = name;
        this.args = args;
    }

    @Override
    public String toString() {
    return name + args;
    }
}