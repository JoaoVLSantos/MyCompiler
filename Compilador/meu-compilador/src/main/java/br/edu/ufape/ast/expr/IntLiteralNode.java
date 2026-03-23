package br.edu.ufape.ast.expr;

public class IntLiteralNode extends ExprNode {

    public final int value;

    public IntLiteralNode(int line, int column, String lexeme) {

        super(line, column);

        this.value = Integer.parseInt(lexeme);
    }

    @Override
    public String toString() {
        return "IntLiteralNode(" + value + ")";
    }
}