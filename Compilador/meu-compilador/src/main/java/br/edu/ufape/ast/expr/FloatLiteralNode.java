package br.edu.ufape.ast.expr;

public class FloatLiteralNode extends ExprNode {

    public final double value;

    public FloatLiteralNode(int line, int column, String lexeme) {

        super(line, column);

        this.value = Double.parseDouble(lexeme);
    }

    @Override
    public String toString() {
        return "FloatLiteralNode(" + value + ")";
    }
}