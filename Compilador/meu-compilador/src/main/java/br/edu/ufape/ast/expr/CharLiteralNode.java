package br.edu.ufape.ast.expr;

public class CharLiteralNode extends ExprNode {

    public final char value;

    public CharLiteralNode(int line, int column, String lexeme) {

        super(line, column);

        this.value = lexeme.charAt(0);
    }

    @Override
    public String toString() {
        return "CharLiteralNode('" + value + "')";
    }
}