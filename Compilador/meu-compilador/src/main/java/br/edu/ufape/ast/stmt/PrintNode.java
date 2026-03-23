package br.edu.ufape.ast.stmt;

import br.edu.ufape.ast.expr.ExprNode;

public class PrintNode extends StmtNode {

    public final ExprNode expression;

    public PrintNode(int line, int column,
                     ExprNode expression) {

        super(line, column);

        this.expression = expression;
    }

    @Override
    public String toString() {
        return "PrintNode{" +
                "expression=" + expression +
                '}';
    }
}