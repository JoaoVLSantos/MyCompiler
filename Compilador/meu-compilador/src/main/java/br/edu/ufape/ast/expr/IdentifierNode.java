package br.edu.ufape.ast.expr;

public class IdentifierNode extends ExprNode {

    public final String name;

    public IdentifierNode(int line, int column, String name) {

        super(line, column);

        this.name = name;
    }
    
    @Override
    public String toString() {
    return name;
    }
}