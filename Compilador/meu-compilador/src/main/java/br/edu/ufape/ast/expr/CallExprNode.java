package br.edu.ufape.ast.expr;

import java.util.List;

public class CallExprNode extends ExprNode {

    public final String name;
    public final List<ExprNode> args;

    public CallExprNode(int line, int column,
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