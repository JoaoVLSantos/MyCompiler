package br.edu.ufape.ast.expr;

public class BooleanLiteralNode extends ExprNode {

    public final boolean value;

    public BooleanLiteralNode(int line, int column, boolean value) {

        super(line, column);

        this.value = value;
    }

    @Override
    public String toString() {
        return "BooleanLiteralNode(" + value + ")";
    }
}